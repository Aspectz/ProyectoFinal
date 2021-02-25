package jogasa.simarro.proyectenadal.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.pojo.Pedido;
import jogasa.simarro.proyectenadal.pojo.Producto;

public class AdaptadorPedidos extends ArrayAdapter {

    Activity context;
    ArrayList<Pedido> pedidos;
    public AdaptadorPedidos(Fragment context, ArrayList<Pedido> pedidos){
        super(context.getActivity(), R.layout.adaptador_pedidos,pedidos);
        this.context=context.getActivity();
        this.pedidos=pedidos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View item=inflater.inflate(R.layout.adaptador_pedidos,null);

        TextView nombreProducto=(TextView) item.findViewById(R.id.nombreProducto);
        TextView fechaPedido=(TextView) item.findViewById(R.id.fechaPedido);
        ImageView imagen=(ImageView) item.findViewById(R.id.pedidoFoto);
        String nombre=pedidos.get(position).getNombre();



        String fecha=getContext().getString(R.string.orderDate);
        nombreProducto.setText(pedidos.get(position).getNombre());
        fechaPedido.setText(fecha+":"+pedidos.get(position).getFechacreacionPedido());
        if(nombre.equals("Banana")) imagen.setImageResource(R.drawable.banana);
        if(nombre.equals("Aguacate"))imagen.setImageResource(R.drawable.aguacate);
        if(nombre.equals("Limon"))imagen.setImageResource(R.drawable.limon);
        if(nombre.equals("Cereza"))imagen.setImageResource(R.drawable.cereza);
        if(nombre.equals("Fresa"))imagen.setImageResource(R.drawable.fresa);
        if(nombre.equals("Naranja"))imagen.setImageResource(R.drawable.naranja);
        if(nombre.equals("Manzana"))imagen.setImageResource(R.drawable.manzana);
        if(nombre.equals("Arandano"))imagen.setImageResource(R.drawable.arandano);
        if(nombre.equals("Pepino"))imagen.setImageResource(R.drawable.pepino);

        return item;
    }
}