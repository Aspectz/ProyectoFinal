package jogasa.simarro.projectefinal.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;

import jogasa.simarro.projectefinal.R;
import jogasa.simarro.projectefinal.pojo.OrderDetails;
import jogasa.simarro.projectefinal.pojo.Pedido;
import jogasa.simarro.projectefinal.pojo.Producto;

public class AdaptadorManageOrders extends ArrayAdapter {

    Activity context;
    ArrayList<OrderDetails> od;
    FirebaseFirestore fb= FirebaseFirestore.getInstance();
    DecimalFormat df = new DecimalFormat("0.00");
    public AdaptadorManageOrders(Activity context,ArrayList<OrderDetails> orderDetails){
        super(context, R.layout.adaptador_manage_orders,orderDetails);
        this.context=context;
        this.od=orderDetails;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View item=inflater.inflate(R.layout.adaptador_manage_orders,null);

        TextView nombreProducto=(TextView) item.findViewById(R.id.orderNameOrder);
        TextView fechaPedido=(TextView) item.findViewById(R.id.orderDate);
        TextView estado=(TextView) item.findViewById(R.id.orderState);
        TextView nombreCliente=(TextView)item.findViewById(R.id.orderClientName);
        TextView totalPrice=(TextView)item.findViewById(R.id.orderTotalPrice);
        ImageView imagen=(ImageView) item.findViewById(R.id.pedidoFoto);




        fb.collection("Products").document(od.get(position).getIdProducto()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    nombreProducto.setText(task.getResult().toObject(Producto.class).getNombre());
                    Glide.with(getContext()).load(task.getResult().toObject(Producto.class).getFotos().get(0)).into(imagen);
                }
            }
        });
        totalPrice.setText(df.format(od.get(position).getTotalPrice()));
        fb.collection("Orders").document(String.valueOf(od.get(position).getIdOrder())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    nombreCliente.setText(task.getResult().toObject(Pedido.class).getDireccion().getNombre());
                    fechaPedido.setText(task.getResult().toObject(Pedido.class).getFecha());
                    estado.setText(task.getResult().toObject(Pedido.class).getEstado().toString());
                }
            }
        });
        return item;
    }
}