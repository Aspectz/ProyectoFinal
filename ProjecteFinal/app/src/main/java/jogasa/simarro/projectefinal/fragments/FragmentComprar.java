package jogasa.simarro.projectefinal.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;



import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jogasa.simarro.projectefinal.R;
import jogasa.simarro.projectefinal.activity.CrearPedido;
import jogasa.simarro.projectefinal.activity.HomeActivity;
import jogasa.simarro.projectefinal.pojo.Estados;
import jogasa.simarro.projectefinal.pojo.Favorites;
import jogasa.simarro.projectefinal.pojo.OrderDetails;
import jogasa.simarro.projectefinal.pojo.Pedido;
import jogasa.simarro.projectefinal.pojo.Producto;
import jogasa.simarro.projectefinal.pojo.Usuario;

public class FragmentComprar extends Fragment {

    private Producto producto;
    int cantidad = 1;

    ImageView favFoto;
    FirebaseFirestore fb = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String randomKey = UUID.randomUUID().toString();


    public  FragmentComprar(Producto producto) {
        this.producto = producto;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comprar, container, false);


        TextView nombre = view.findViewById(R.id.nombreProducto);
        TextView precio = view.findViewById(R.id.precioProducto);
        TextView descripcion = view.findViewById(R.id.descripcionProducto);
        ImageSlider carousel = view.findViewById(R.id.imagenProducto);

        List<SlideModel> foto = new ArrayList<SlideModel>();
        for (int i = 0; i < producto.getFotos().size(); i++) {
            foto.add(new SlideModel(producto.getFotos().get(i), ScaleTypes.CENTER_INSIDE));
        }
        carousel.setImageList(foto);


        Spinner cantity = view.findViewById(R.id.cantitySpinner);
        favFoto = view.findViewById(R.id.isFavBtn);
        fb.collection("Favorites").whereEqualTo("idUser", firebaseAuth.getCurrentUser().getUid()).whereEqualTo("idProduct", producto.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        if (doc.exists()) {
                            favFoto.setImageResource(R.drawable.estrellafav);
                        } else {
                            favFoto.setImageResource(R.drawable.estrella);
                        }
                    }
                }
            }
        });

        final ArrayList<String> productMaxCantity = new ArrayList<String>();
        //ADD MAX VALUES TO SPINNER
        for (int i = 1; i <= producto.getLimiteProducto(); i++) {
            productMaxCantity.add(String.valueOf(i));
        }


        final ArrayAdapter<String> adapterCantity = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, productMaxCantity);

        cantity.setAdapter(adapterCantity);

        cantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cantidad = Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        nombre.setText(producto.getNombre());
        precio.setText(String.valueOf(producto.getPrecio()) + "€/Kg");
        descripcion.setText(producto.getDescripcion());
        //Glide.with(getContext()).load(producto.getFotos().get(0)).into(foto);


        Button botonComprar = (Button) view.findViewById(R.id.comprarButton);

        botonComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botonComprar.setClickable(false);
                addOrder(Estados.PROCESANDO, "buy");
            }
        });

        Button btnAddtoShipping = (Button) view.findViewById(R.id.addToShoppingCartBtn);
        btnAddtoShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnAddtoShipping.setClickable(false);
                addToShippingCart();
            }
        });


        favFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fb.collection("Favorites").whereEqualTo("idUser", firebaseAuth.getCurrentUser().getUid()).whereEqualTo("idProduct", producto.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    if (doc.exists()) {
                                        favFoto.setImageResource(R.drawable.estrella);
                                        fb.collection("Favorites").document(producto.getId()).delete();
                                    }
                                }
                            } else {
                                favFoto.setImageResource(R.drawable.estrellafav);
                                Map<String, Object> mapa = new HashMap<>();
                                mapa.put("idUser", firebaseAuth.getCurrentUser().getUid());
                                mapa.put("idProduct", producto.getId());
                                fb.collection("Favorites").document(producto.getId()).set(mapa);
                            }

                        }
                    }
                });
            }
        });
        return view;
    }

    private void addToShippingCart() {


        String randomKey = UUID.randomUUID().toString();

        fb.collection("Orders").whereEqualTo("idUser", firebaseAuth.getCurrentUser().getUid()).whereEqualTo("estado", Estados.CARRITO).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            fb.collection("OrderDetails").whereEqualTo("idProducto", producto.getId()).whereEqualTo("idOrder", queryDocumentSnapshot.toObject(Pedido.class).getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (!task.getResult().isEmpty()) {
                                            for (QueryDocumentSnapshot queryDocumentSnapshot1 : task.getResult()) {
                                                OrderDetails od = queryDocumentSnapshot1.toObject(OrderDetails.class);
                                                od.setQuantity(od.getQuantity() + cantidad);
                                                od.setTotalPrice(od.getTotalPrice() + cantidad * producto.getPrecio());
                                                fb.collection("OrderDetails").document(od.getIdOrderDetails()).set(od).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Intent addtoshipping = new Intent(getActivity(), HomeActivity.class);
                                                        startActivity(addtoshipping);
                                                    }
                                                });
                                            }
                                        } else {
                                            //Entrará quan no hi ha ningun producte igual al mateix pedido
                                            OrderDetails newOD = new OrderDetails();
                                            newOD.setIdOrder(queryDocumentSnapshot.toObject(Pedido.class).getId());
                                            newOD.setTotalPrice(cantidad * producto.getPrecio());
                                            newOD.setQuantity(cantidad);
                                            newOD.setIdOrderDetails(randomKey);
                                            newOD.setIdProducto(producto.getId());
                                            fb.collection("OrderDetails").document(newOD.getIdOrderDetails()).set(newOD).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Intent addtoshipping = new Intent(getActivity(), HomeActivity.class);
                                                    startActivity(addtoshipping);
                                                }
                                            });
                                            ;
                                        }
                                    }
                                }
                            });
                        }
                    } else {
                        //entra quan no hi ha ningun carrito
                        Pedido p = new Pedido();
                        p.setEstado(Estados.CARRITO);
                        p.setIdUser(firebaseAuth.getCurrentUser().getUid());
                        OrderDetails od = new OrderDetails();
                        od.setIdOrder(p.getId());
                        od.setIdOrderDetails(randomKey);
                        od.setIdProducto(producto.getId());
                        od.setQuantity(cantidad);
                        od.setTotalPrice(cantidad * producto.getPrecio());
                        fb.collection("Orders").document(String.valueOf(p.getId())).set(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                fb.collection("OrderDetails").document(od.getIdOrderDetails()).set(od).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent addtoshipping = new Intent(getActivity(), HomeActivity.class);
                                        startActivity(addtoshipping);
                                    }
                                });
                            }
                        });
                    }

                }
            }
        });


      /*fb.collection("Orders").whereEqualTo("idUser",firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                    addOrder(Estados.CARRITO,"a");

                }
            }
        });*/

    }

    private void addOrder(Estados estado, String option) {
        Pedido toCart = new Pedido();
        toCart.setIdUser(firebaseAuth.getCurrentUser().getUid());
        toCart.setEstado(estado);
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setIdOrder(toCart.getId());
        orderDetails.setIdProducto(producto.getId());
        orderDetails.setQuantity(cantidad);
        orderDetails.setTotalPrice(cantidad * producto.getPrecio());
        orderDetails.setIdOrderDetails(randomKey);
        fb.collection("Orders").document(String.valueOf(toCart.getId())).set(toCart);
        fb.collection("OrderDetails").document(orderDetails.getIdOrderDetails()).set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    if (option.equalsIgnoreCase("buy")) {
                        Intent crearPedido = new Intent(getActivity(), CrearPedido.class);
                        crearPedido.putExtra("Option", "Buy");
                        crearPedido.putExtra("OrderDetail",orderDetails);
                        startActivity(crearPedido);
                    }
                }

            }
        });

    }
}