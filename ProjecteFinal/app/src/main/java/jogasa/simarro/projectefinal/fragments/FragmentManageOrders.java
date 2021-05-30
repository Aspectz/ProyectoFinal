package jogasa.simarro.projectefinal.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;


import jogasa.simarro.projectefinal.R;
import jogasa.simarro.projectefinal.adapters.AdaptadorManageOrders;
import jogasa.simarro.projectefinal.adapters.AdaptadorPedidos;
import jogasa.simarro.projectefinal.pojo.Estados;
import jogasa.simarro.projectefinal.pojo.OrderDetails;
import jogasa.simarro.projectefinal.pojo.Pedido;
import jogasa.simarro.projectefinal.pojo.Producto;

public class FragmentManageOrders extends Fragment {
    public ListView listaPedidos;
    private FirebaseFirestore fb=FirebaseFirestore.getInstance();
    private FirebaseAuth fauth=FirebaseAuth.getInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_manage_orders, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("Pepe",getActivity().getTitle()+"");
        listaPedidos = (ListView) getView().findViewById(R.id.listaPedidos);

    }

    public void mostrarProductos() {
        ArrayList<OrderDetails> od = new ArrayList<OrderDetails>();



        fb.collection("Products").whereEqualTo("idSupplier",fauth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot prod : task.getResult()){
                        fb.collection("OrderDetails").whereEqualTo("idProducto",prod.toObject(Producto.class).getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot orderDet : task.getResult()){

                                        fb.collection("Orders").document(String.valueOf(orderDet.toObject(OrderDetails.class).getIdOrder())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){

                                                    if(!task.getResult().toObject(Pedido.class).getEstado().toString().equals(Estados.CARRITO.toString()))
                                                        od.add(orderDet.toObject(OrderDetails.class));
                                                }
                                                listaPedidos.setAdapter(new AdaptadorManageOrders(getActivity(),od));
                                            }
                                        });

                                    }

                                }
                            }
                        });
                    }

                }
            }
        });


       /* fb.collection("OrdersDetails").whereEqualTo("id",fauth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot pe : task.getResult()){
                        if(pe.toObject(Pedido.class).getEstado()!= Estados.CARRITO){
                            pedidos.add(pe.toObject(Pedido.class));
                            fb.collection("OrderDetails").whereEqualTo("idOrder",pe.toObject(Pedido.class).getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for (QueryDocumentSnapshot pd : task.getResult()){
                                            od.add(pe.toObject(OrderDetails.class));
                                        }
                                        listaPedidos.setAdapter(new AdaptadorManageOrders(getActivity(), pedidos,od));
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });*/



    }

    @Override
    public void onStart() {
        super.onStart();
        mostrarProductos();
    }


}