package jogasa.simarro.proyectenadal.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.activity.HomeActivity;

import static android.app.Activity.RESULT_OK;


public class FragmentRegistrarVendedor extends Fragment {


    private ImageView productImage;
    public Uri imageUri;
    private FirebaseStorage storage;
    private FirebaseAuth fb=FirebaseAuth.getInstance();
    private StorageReference storageReference;
    private Button anadirBtn,chooseAddressBtn;
    private EditText nombre,desc,precio,limite;
    private FirebaseFirestore firebaseFirestore;
    private final String randomKeyPhoto= UUID.randomUUID().toString();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registrar_vendedor, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        anadirBtn=(Button)getView().findViewById(R.id.anadirVendedorBoton);
        chooseAddressBtn=(Button)getView().findViewById(R.id.chooseAddressBtn);
        nombre=(EditText)getView().findViewById(R.id.anadirNombreCompany);

        firebaseFirestore=FirebaseFirestore.getInstance();

        anadirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Map<String, String> mapa = new HashMap<>();
                mapa.put("id", fb.getCurrentUser().getUid());
                mapa.put("Company", nombre.getText().toString());
                firebaseFirestore.collection("Suppliers").document(fb.getCurrentUser().getUid()).set(mapa);
                Intent intent=new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });


        //Places.initialize(getActivity().getApplicationContext(),"AIzaSyC3l1kctVyosKLCSi4lZkP_cWZzu4uZ5ek");

       /* chooseAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Place.Field> fieldList= Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
                Intent intent=new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(getContext());
                startActivityForResult(intent,100);
            }
        });*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode==RESULT_OK){
            Place place=Autocomplete.getPlaceFromIntent(data);
        }else if(resultCode== AutocompleteActivity.RESULT_ERROR){
            Status status=Autocomplete.getStatusFromIntent(data);
            Snackbar.make(getActivity().findViewById(android.R.id.content),status.getStatusMessage(),Snackbar.LENGTH_LONG).show();
        }
    }
    /*
    private void uploadPicture() {

        String randomKey= UUID.randomUUID().toString();
        Producto p=new Producto(nombre.getText().toString(),desc.getText().toString(),Float.parseFloat(precio.getText().toString()),Integer.parseInt(limite.getText().toString()));
        p.setIdSupplier(fb.getCurrentUser().getUid());
        p.setId(randomKey);

        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setTitle("Uploading Image...");
        pd.show();
        StorageReference imgref=storageReference.child("images/"+randomKeyPhoto);

        imgref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //LA DIFERENCIA ENTRE ONCOMPLETE Y ONSUCCESS ES QUE ONCOMPLETE S'EXECUTARÁ SEMPRE INCLUS SI FALLA, PERO EL ONSUCCESS SOLS SI NO FALLA, TINDRIEM Q FER UN ONFAILURE DPS DE ONSUCCESS
                //AQUEST METODE HI HA QUE FERLO DINS PERQUE ES CUAN ESTAS AFEGINTLO A LA FIRESTORAGE
                imgref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Snackbar.make(getActivity().findViewById(android.R.id.content),"Image Uploaded.",Snackbar.LENGTH_LONG).show();
                            pd.dismiss();
                            p.setFoto(task.getResult().toString());
                            firebaseFirestore.collection("Products").document(p.getId()).set(p);
                            Intent main=new Intent(getActivity(), MainActivity.class);
                            startActivity(main);
                        }
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent=(100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                pd.setMessage("Percentatge :"+(int)progressPercent+"%");
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