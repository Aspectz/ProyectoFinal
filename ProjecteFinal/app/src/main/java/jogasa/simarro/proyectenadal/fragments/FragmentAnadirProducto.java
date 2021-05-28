package jogasa.simarro.proyectenadal.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.activity.HomeActivity;
import jogasa.simarro.proyectenadal.pojo.Producto;

import static android.app.Activity.RESULT_OK;


public class FragmentAnadirProducto extends Fragment {


    private ImageView firstImageView, secondImageView, thirdImageView;
    public Uri firstImage, secondImage, thirdImage;
    private FirebaseStorage storage;
    private FirebaseAuth fb = FirebaseAuth.getInstance();
    private StorageReference storageReference;
    private Button anadirBtn;
    private EditText nombre, desc, precio, limite;
    private FirebaseFirestore firebaseFirestore;
    private final String randomKeyPhoto = UUID.randomUUID().toString();
    private Producto producto = new Producto();
    private String randomKey = UUID.randomUUID().toString();

    private List<Uri> imageList = new ArrayList<Uri>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_anadir_producto, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        anadirBtn = (Button) getView().findViewById(R.id.anadirBoton);
        nombre = (EditText) getView().findViewById(R.id.anadirNombre);
        desc = (EditText) getView().findViewById(R.id.anadirDesc);
        precio = (EditText) getView().findViewById(R.id.anadirPrecio);
        limite = (EditText) getView().findViewById(R.id.anadirLimite);

        firebaseFirestore = FirebaseFirestore.getInstance();

        firstImageView = (ImageView) getView().findViewById(R.id.anadirFoto1);
        secondImageView = (ImageView) getView().findViewById(R.id.anadirFoto2);
        thirdImageView = (ImageView) getView().findViewById(R.id.anadirFoto3);
        firstImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture(1);
            }
        });
        secondImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture(2);
            }
        });
        thirdImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture(3);
            }
        });

        anadirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (firstImage != null || secondImage != null || thirdImage != null) {

                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Uploading .... ");
                    progressDialog.show();
                    anadirBtn.setClickable(false); // disable upload button whilst uploading

                    producto.setNombre(nombre.getText().toString());
                    producto.setDescripcion(desc.getText().toString());
                    producto.setPrecio(Float.parseFloat(precio.getText().toString()));
                    producto.setLimiteProducto(Integer.parseInt(limite.getText().toString()));
                    producto.setIdSupplier(fb.getCurrentUser().getUid());
                    producto.setId(randomKey);

                    final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + randomKeyPhoto);
                    final List<Uri> clonedImageList = new ArrayList<>(imageList);

                    imageList.clear(); // empty old list?
                    int imageListSize = clonedImageList.size();

                    List<Task<Uri>> uploadedImageUrlTasks = new ArrayList<>(imageListSize);

                    for (Uri imageUri : clonedImageList) {
                        final String imageFilename = imageUri.getLastPathSegment();
                        Log.d("upload.onClick()", "Starting upload for \"" + imageFilename + "\"...");

                        StorageReference imageRef = storageReference.child(imageFilename); // Warning: potential for collisions/overwrite
                        UploadTask currentUploadTask = imageRef.putFile(imageUri);

                        Task<Uri> currentUrlTask = currentUploadTask
                                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()) {
                                            Log.d("upload.onClick()", "Upload for \"" + imageFilename + "\" failed!");
                                            throw task.getException(); // rethrow any errors
                                        }

                                        Log.d("upload.onClick()", "Upload for \"" + imageFilename + "\" finished. Fetching download URL...");
                                        return imageRef.getDownloadUrl();
                                    }
                                });

                        uploadedImageUrlTasks.add(currentUrlTask);
                    }


                    Tasks.whenAllComplete(uploadedImageUrlTasks).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                        @Override
                        public void onComplete(@NonNull Task<List<Task<?>>> tasks) {
                            List<Uri> failedUploads = new ArrayList<>();
                            for (Task<?> task : tasks.getResult()) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = (Uri) task.getResult();
                                    producto.getFotos().add(downloadUri.toString());
                                } else {
                                    Uri imageUri = clonedImageList.get(tasks.getResult().indexOf(task));
                                    failedUploads.add(imageUri);
                                    Log.e("upload.onClick()", "Failed to upload/fetch URL for \"" + imageUri.getLastPathSegment() + "\" with exception", task.getException()); // log exception
                                }
                            }
                            progressDialog.dismiss(); // dismiss upload dialog
                            if (failedUploads.size() > 0) {
                                // TODO: Do something with list of failed uploads such as readd to the now empty upload list
                                imageList.addAll(failedUploads);
                                anadirBtn.setClickable(true);
                            } else {
                                firebaseFirestore.collection("Products").document(producto.getId()).set(producto);
                                Intent main = new Intent(getActivity(), HomeActivity.class);
                                startActivity(main);
                            }
                        }
                    });
                }else{
                    Toast.makeText(getContext(), "Debes elegir almenos una imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




      /*anadirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstImage!=null || secondImage!=null || thirdImage!=null){
                    if(firstImage!=null) { uploadPicture(firstImage);}
                    if(secondImage!=null) { uploadPicture(secondImage);}
                    if(thirdImage!=null) { uploadPicture(thirdImage);}

                    Tasks.whenAllComplete()
                    producto.setNombre(nombre.getText().toString());
                    producto.setDescripcion(desc.getText().toString());
                    producto.setPrecio(Float.parseFloat(precio.getText().toString()));
                    producto.setLimiteProducto(Integer.parseInt(limite.getText().toString()));
                    producto.setIdSupplier(fb.getCurrentUser().getUid());
                    producto.setId(randomKey);


                    Log.d("Esiste",producto.getFotos().size()+"");



                    firebaseFirestore.collection("Products").document(producto.getId()).set(producto);


                    //Intent main=new Intent(getActivity(), HomeActivity.class);
                    //startActivity(main);
                }
                else Toast.makeText(getContext(), "Please select and Image", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    private void choosePicture(int picture) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, picture);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Pepe", requestCode + "");
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            firstImage = data.getData();
            firstImageView.setImageURI(firstImage);
            imageList.add(firstImage);
        }
        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            secondImage = data.getData();
            secondImageView.setImageURI(secondImage);
            imageList.add(secondImage);
        }
        if (requestCode == 3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            thirdImage = data.getData();
            thirdImageView.setImageURI(thirdImage);
            imageList.add(thirdImage);
        }
    }

    /*private void uploadPicture(Uri imageUri) {

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Uploading Image...");
        pd.show();
        StorageReference imgref = storageReference.child("images/" + randomKeyPhoto);

        imgref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //LA DIFERENCIA ENTRE ONCOMPLETE Y ONSUCCESS ES QUE ONCOMPLETE S'EXECUTARÁ SEMPRE INCLUS SI FALLA, PERO EL ONSUCCESS SOLS SI NO FALLA, TINDRIEM Q FER UN ONFAILURE DPS DE ONSUCCESS
                //AQUEST METODE HI HA QUE FERLO DINS PERQUE ES CUAN ESTAS AFEGINTLO A LA FIRESTORAGE

                imgref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Image Uploaded.", Snackbar.LENGTH_LONG).show();
                        pd.dismiss();
                        //Log.d("Pepe",task.getResult().toString());
                        producto.getFotos().add(uri.toString());
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Percentatge :" + (int) progressPercent + "%");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }*/
}
