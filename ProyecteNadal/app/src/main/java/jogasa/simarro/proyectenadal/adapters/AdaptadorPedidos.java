package jogasa.simarro.proyectenadal.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.pojo.OrderDetails;
import jogasa.simarro.proyectenadal.pojo.Pedido;
import jogasa.simarro.proyectenadal.pojo.Producto;

public class AdaptadorPedidos extends ArrayAdapter {

    Activity context;
    ArrayList<OrderDetails> pedidos;
    FirebaseFirestore fb= FirebaseFirestore.getInstance();
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
        ImageView imagen=(ImageView) item.findViewById(R.id.pedidoFoto);
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
                    fechaPedido.setText(fecha+":"+task.getResult().toObject(Pedido.class).getFechacreacionPedido());
                }
            }
        });
        return item;
    }
}