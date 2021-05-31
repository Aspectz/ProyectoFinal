package jogasa.simarro.projectefinal.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jogasa.simarro.projectefinal.R;
import jogasa.simarro.projectefinal.activity.HomeActivity;
import jogasa.simarro.projectefinal.pojo.Estados;
import jogasa.simarro.projectefinal.pojo.OrderDetails;
import jogasa.simarro.projectefinal.pojo.Pedido;
import jogasa.simarro.projectefinal.pojo.Producto;
import jogasa.simarro.projectefinal.pojo.Usuario;

import static android.app.Activity.RESULT_OK;

public class FragmentEditarProducto extends Fragment {

    private Producto producto;
    private ImageSlider carousel;
    FirebaseFirestore fb = FirebaseFirestore.getInstance();

    public FragmentEditarProducto(Producto producto) {
        this.producto = producto;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_producto, container, false);


        TextView nombre = view.findViewById(R.id.editName);
        TextView precio = view.findViewById(R.id.editPrice);
        TextView descripcion = view.findViewById(R.id.editDesc);
        TextView limite = view.findViewById(R.id.editQuantity);


        carousel = view.findViewById(R.id.imagenProducto);
        Button updateProduct = view.findViewById(R.id.updateProduct);
        Button deleteProduct = view.findViewById(R.id.deleteProduct);

        List<SlideModel> foto = new ArrayList<SlideModel>();
        for (int i = 0; i < producto.getFotos().size(); i++) {
            foto.add(new SlideModel(producto.getFotos().get(i), ScaleTypes.CENTER_INSIDE));
        }
        carousel.setImageList(foto);

        nombre.setText(producto.getNombre());
        precio.setText(String.valueOf(producto.getPrecio()));
        descripcion.setText(producto.getDescripcion());
        limite.setText(String.valueOf(producto.getLimiteProducto()));

        updateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> newProduct = new HashMap<>();
                newProduct.put("nombre", nombre.getText().toString());
                newProduct.put("limiteProducto", Integer.parseInt(limite.getText().toString()));
                newProduct.put("precio", Float.parseFloat(precio.getText().toString()));
                newProduct.put("descripcion", nombre.getText().toString());

                fb.collection("Products").document(producto.getId()).update(newProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                            Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.productUpdated), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fb.collection("Products").document(producto.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                            Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.productDeleted), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        return view;
    }
}
