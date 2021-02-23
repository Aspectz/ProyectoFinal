package jogasa.simarro.proyectenadal.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.pojo.Pedido;
import jogasa.simarro.proyectenadal.pojo.PedidoSinCompletar;
import jogasa.simarro.proyectenadal.pojo.Producto;

public class AdaptadorListaShipping extends ArrayAdapter {

    Activity context;
    ArrayList<Pedido> pedidos=new ArrayList<Pedido>();
    public AdaptadorListaShipping(Fragment context, ArrayList<Pedido> pedidos){
        super(context.getActivity(), R.layout.activity_adaptador_lista_shipping,pedidos);
        this.context=context.getActivity();
        this.pedidos=pedidos;
    }
    public AdaptadorListaShipping(Activity context, ArrayList<Pedido> pedidos){
        super(context, R.layout.activity_adaptador_lista_shipping,pedidos);
        this.context=context;
        this.pedidos=pedidos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View item=inflater.inflate(R.layout.activity_adaptador_lista_shipping,null);

        Producto producto=pedidos.get(position).getProductos().get(0);

        TextView nombreProducto=(TextView) item.findViewById(R.id.nombreProductoShipping);
        TextView cantidad=(TextView) item.findViewById(R.id.cantidadProductoShipping);
        ImageView imagen=(ImageView) item.findViewById(R.id.pedidoFotoShipping);
        TextView precioUnico=(TextView)item.findViewById(R.id.precioProductoShipping);
        TextView finalPrice=(TextView)item.findViewById(R.id.priceFinalShipping);

         DecimalFormat df = new DecimalFormat("0.00");
        String nombre=producto.getNombre();

        if(nombre.equals("Banana")) imagen.setImageResource(R.drawable.banana);
        if(nombre.equals("Aguacate"))imagen.setImageResource(R.drawable.aguacate);
        if(nombre.equals("Limon"))imagen.setImageResource(R.drawable.limon);
        if(nombre.equals("Cereza"))imagen.setImageResource(R.drawable.cereza);
        if(nombre.equals("Fresa"))imagen.setImageResource(R.drawable.fresa);
        if(nombre.equals("Naranja"))imagen.setImageResource(R.drawable.naranja);
        if(nombre.equals("Manzana"))imagen.setImageResource(R.drawable.manzana);
        if(nombre.equals("Arandano"))imagen.setImageResource(R.drawable.arandano);
        if(nombre.equals("Pepino"))imagen.setImageResource(R.drawable.pepino);

        nombreProducto.setText(producto.getNombre());
        cantidad.setText(String.valueOf(pedidos.get(position).getCantidadPedido()));
        float precioFinal;



        precioFinal=pedidos.get(position).getProductos().get(0).getPrecio()*pedidos.get(position).getCantidadPedido();
        finalPrice.setText(df.format(precioFinal));
        precioUnico.setText(String.valueOf(pedidos.get(position).getProductos().get(0).getPrecio())+"/kg");


        return item;
}

}