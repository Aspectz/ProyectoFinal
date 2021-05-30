package jogasa.simarro.projectefinal.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.StructuredQuery;

import java.text.DecimalFormat;
import java.util.ArrayList;

import jogasa.simarro.projectefinal.R;
import jogasa.simarro.projectefinal.activity.CrearPedido;
import jogasa.simarro.projectefinal.adapters.AdaptadorListaShipping;
import jogasa.simarro.projectefinal.pojo.Estados;
import jogasa.simarro.projectefinal.pojo.OrderDetails;
import jogasa.simarro.projectefinal.pojo.Pedido;

public class FragmentShippingCart extends Fragment {

    private ListView shippingCartList;
    private int cantidad=0;

    private FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    private FirebaseAuth fauth=FirebaseAuth.getInstance();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shipping_cart, container, false);
        return view;
    }
    public void increment(View view){}
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView totalPrice=(TextView)getView().findViewById(R.id.totalPrice);
        ArrayList<OrderDetails> ordersToShipping=new ArrayList<OrderDetails>();
        shippingCartList=(ListView)getActivity().findViewById(R.id.shippingCartListView);


        DecimalFormat df = new DecimalFormat("0.00");



        firebaseFirestore.collection("Orders").whereEqualTo("estado", Estados.CARRITO).whereEqualTo("idUser",fauth.getCurrentUser().getUid()).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot pedido : task.getResult()){
                        firebaseFirestore.collection("OrderDetails").whereEqualTo("idOrder",pedido.toObject(Pedido.class).getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    float price=0;
                                    for(QueryDocumentSnapshot od : task.getResult()){
                                        OrderDetails orderDetails=od.toObject(OrderDetails.class);
                                        ordersToShipping.add(orderDetails);
                                        price+=(Float)orderDetails.getTotalPrice();
                                    }
                                    totalPrice.setText(String.valueOf(df.format(price)));
                                    shippingCartList.setAdapter(new AdaptadorListaShipping(getActivity(),ordersToShipping));
                                    shippingCartList.deferNotifyDataSetChanged();
                                }
                            }
                        });
                    }
                }
            }
        });

        Button botonComprar=(Button)getActivity().findViewById(R.id.botonComprarShipping);

        botonComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent crearPedido=new Intent(getActivity(), CrearPedido.class);
               // crearPedido.putExtra("Productos",productos);

                crearPedido.putExtra("Option","Cart");
                startActivity(crearPedido);
            }
        });

    }
}