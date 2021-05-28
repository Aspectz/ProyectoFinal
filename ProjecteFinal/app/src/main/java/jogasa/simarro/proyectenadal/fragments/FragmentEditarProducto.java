package jogasa.simarro.proyectenadal.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.activity.CrearPedido;
import jogasa.simarro.proyectenadal.activity.HomeActivity;
import jogasa.simarro.proyectenadal.pojo.Estados;
import jogasa.simarro.proyectenadal.pojo.OrderDetails;
import jogasa.simarro.proyectenadal.pojo.Pedido;
import jogasa.simarro.proyectenadal.pojo.Producto;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class FragmentEditarProducto extends Fragment {

    private Producto producto;
    private Usuario comprador;
    int cantidad=1;

    ImageView favFoto;
    FirebaseFirestore fb=FirebaseFirestore.getInstance();
    FirebaseAuth  firebaseAuth=FirebaseAuth.getInstance();
    String randomKey= UUID.randomUUID().toString();


    public FragmentEditarProducto(Producto producto){
        this.producto=producto;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_editar_producto,container,false);



        TextView nombre=view.findViewById(R.id.editName);
        TextView precio=view.findViewById(R.id.editPrice);
        TextView descripcion=view.findViewById(R.id.editDesc);
        TextView limite=view.findViewById(R.id.editQuantity);

        ImageSlider carousel=view.findViewById(R.id.imagenProducto);
        ImageButton updateProduct=view.findViewById(R.id.updateProduct);
        ImageButton cancelProduct=view.findViewById(R.id.cancelUpdate);
        ImageButton deleteProduct=view.findViewById(R.id.deleteProduct);

        List<SlideModel> foto=new ArrayList<SlideModel>();
        for(int i=0;i<producto.getFotos().size();i++){
            foto.add(new SlideModel(producto.getFotos().get(i), ScaleTypes.CENTER_INSIDE));
        }
        carousel.setImageList(foto);



        nombre.setText(producto.getNombre());
        precio.setText(String.valueOf(producto.getPrecio()));
        descripcion.setText(producto.getDescripcion());
        limite.setText(String.valueOf(producto.getLimiteProducto()));
        //Glide.with(getContext()).load(producto.getFotos().get(0)).into(foto);


        updateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> newProduct=new HashMap<>();
                newProduct.put("nombre",nombre.getText().toString());
                newProduct.put("limiteProducto",Integer.parseInt(limite.getText().toString()));
                newProduct.put("precio",Float.parseFloat(precio.getText().toString()));
                newProduct.put("descripcion",nombre.getText().toString());

                fb.collection("Products").document(producto.getId()).update(newProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent intent=new Intent(getActivity(),HomeActivity.class);
                            startActivity(intent);
                            Snackbar.make(getActivity().findViewById(android.R.id.content),"Product updated successfully",Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        cancelProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),HomeActivity.class);
                startActivity(intent);
            }
        });
        deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fb.collection("Products").document(producto.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent intent=new Intent(getActivity(),HomeActivity.class);
                            startActivity(intent);
                            Snackbar.make(getActivity().findViewById(android.R.id.content),"Product deleted successfully",Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });








        return view;
    }

    private void addToShippingCart(){
        String randomKey= UUID.randomUUID().toString();
        fb.collection("Orders").whereEqualTo("idUser",firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot p : task.getResult()){
                        if(p.toObject(Pedido.class).getEstado()==Estados.CARRITO){
                            fb.collection("OrderDetails").whereEqualTo("idOrder",p.toObject(Pedido.class).getId()).whereEqualTo("idProducto",producto.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(QueryDocumentSnapshot query : task.getResult()){
                                            OrderDetails od=query.toObject(OrderDetails.class);
                                            od.setQuantity(od.getQuantity() + cantidad);
                                            od.setTotalPrice(od.getTotalPrice() + cantidad * producto.getPrecio());
                                            fb.collection("OrderDetails").document(od.getIdOrderDetails()).set(od);
                                            return;
                                        }
                                        OrderDetails newOD=new OrderDetails();
                                        newOD.setIdOrder(p.toObject(Pedido.class).getId());
                                        newOD.setTotalPrice(cantidad*producto.getPrecio());
                                        newOD.setQuantity(cantidad);
                                        newOD.setIdOrderDetails(randomKey);
                                        newOD.setIdProducto(producto.getId());
                                        fb.collection("OrderDetails").document(newOD.getIdOrderDetails()).set(newOD);
                                        return;
                                    }
                                }
                            });
                            return;
                        }
                    }
                    addOrder(Estados.CARRITO);

                }
            }
        });

    }
    private OrderDetails addOrder(Estados estado){
        Pedido toCart=new Pedido(randomKey);
        toCart.setIdUser(firebaseAuth.getCurrentUser().getUid());
        toCart.setEstado(estado);
        OrderDetails orderDetails=new OrderDetails();
        orderDetails.setIdOrder(toCart.getId());
        orderDetails.setIdProducto(producto.getId());
        orderDetails.setQuantity(cantidad);
        orderDetails.setTotalPrice(cantidad*producto.getPrecio());
        orderDetails.setIdOrderDetails(randomKey);
        fb.collection("Orders").document(String.valueOf(toCart.getId())).set(toCart);
        fb.collection("OrderDetails").document(orderDetails.getIdOrderDetails()).set(orderDetails);

        return orderDetails;
    }
}