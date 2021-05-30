package jogasa.simarro.projectefinal.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;

import jogasa.simarro.projectefinal.pojo.OrderDetails;
import jogasa.simarro.projectefinal.pojo.Pedido;
import jogasa.simarro.projectefinal.pojo.Producto;
import jogasa.simarro.projectefinal.R;

public class AdaptadorPedidos extends ArrayAdapter {

    Activity context;
    ArrayList<OrderDetails> pedidos;
    FirebaseFirestore fb= FirebaseFirestore.getInstance();
    DecimalFormat df = new DecimalFormat("0.00");
    public AdaptadorPedidos(Activity context, ArrayList<OrderDetails> orderDetails){
        super(context, R.layout.adaptador_pedidos,orderDetails);
        this.context=context;
        this.pedidos=orderDetails;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View item=inflater.inflate(R.layout.adaptador_pedidos,null);

        TextView nombreProducto=(TextView) item.findViewById(R.id.nombreProducto);
        TextView fechaPedido=(TextView) item.findViewById(R.id.fechaPedido);
        TextView idPedido=(TextView) item.findViewById(R.id.idPedido);
        ImageView imagen=(ImageView) item.findViewById(R.id.pedidoFoto);
        TextView  totalPrice=(TextView)item.findViewById(R.id.totalPriceProduct);
        String fecha=getContext().getString(R.string.orderDate);


        fb.collection("Products").document(pedidos.get(position).getIdProducto()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    nombreProducto.setText(task.getResult().toObject(Producto.class).getNombre());
                    Glide.with(getContext()).load(task.getResult().toObject(Producto.class).getFotos().get(0)).into(imagen);
                }
            }
        });

        fb.collection("Orders").document(String.valueOf(pedidos.get(position).getIdOrder())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    fechaPedido.setText(fecha+":"+task.getResult().toObject(Pedido.class).getFecha());
                    idPedido.setText(String.valueOf(task.getResult().toObject(Pedido.class).getId()));
                    totalPrice.setText(df.format(pedidos.get(position).getTotalPrice()));
                }
            }
        });
        return item;
    }
}