package jogasa.simarro.proyectenadal.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.activity.CrearPedido;
import jogasa.simarro.proyectenadal.adapters.AdaptadorListaShipping;
import jogasa.simarro.proyectenadal.pojo.Pedido;
import jogasa.simarro.proyectenadal.pojo.PedidoSinCompletar;
import jogasa.simarro.proyectenadal.pojo.Producto;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class FragmentShippingCart extends Fragment {

    private ListView shippingCartList;
    private int cantidad=0;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_fragment_shipping_cart, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        TextView totalPrice=(TextView)getView().findViewById(R.id.totalPrice);
        final ArrayList<PedidoSinCompletar> pedidosSinCompletar = ((Usuario)getActivity().getIntent().getSerializableExtra("Usuario")).getPedidosSinCompletar();
        final ArrayList<Producto> productos=new ArrayList<Producto>();
        for(PedidoSinCompletar p : pedidosSinCompletar){
            productos.add(p.getProducto());
            cantidad+=p.getCantidad();
        }

        float price=0;
        for(int i=0;i<pedidosSinCompletar.size();i++){
//            price+=pedidosSinCompletar.get(i).getCantidad()*pedidosSinCompletar.get(i).getProducto().getPrecio();
        }
        DecimalFormat df = new DecimalFormat("0.00");

        totalPrice.setText(String.valueOf(df.format(price)));
        shippingCartList=(ListView)getActivity().findViewById(R.id.shippingCartListView);
        shippingCartList.setAdapter(new AdaptadorListaShipping(this,pedidosSinCompletar));


        Button botonComprar=(Button)getActivity().findViewById(R.id.botonComprarShipping);

        botonComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent crearPedido=new Intent(getActivity(), CrearPedido.class);
                crearPedido.putExtra("Productos",productos);
                crearPedido.putExtra("Cantidad",cantidad);
                crearPedido.putExtra("Usuario",(Usuario)getActivity().getIntent().getSerializableExtra("Usuario"));
                startActivity(crearPedido);
            }
        });

    }
}