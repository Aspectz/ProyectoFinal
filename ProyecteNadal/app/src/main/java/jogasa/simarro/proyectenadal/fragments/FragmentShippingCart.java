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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.activity.CrearPedido;
import jogasa.simarro.proyectenadal.adapters.AdaptadorListaShipping;
import jogasa.simarro.proyectenadal.bd.MiBD;
import jogasa.simarro.proyectenadal.pojo.OrderProducto;
import jogasa.simarro.proyectenadal.pojo.Pedido;
import jogasa.simarro.proyectenadal.pojo.PedidoSinCompletar;
import jogasa.simarro.proyectenadal.pojo.Producto;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class FragmentShippingCart extends Fragment {

    private ListView shippingCartList;
    private int cantidad=0;
    private MiBD miBD;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        miBD=MiBD.getInstance(getContext());
        View view = inflater.inflate(R.layout.activity_fragment_shipping_cart, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Usuario comprador=((Usuario)getActivity().getIntent().getSerializableExtra("Usuario"));

        TextView totalPrice=(TextView)getView().findViewById(R.id.totalPrice);


        ArrayList<Pedido> allOrders=miBD.getOrderDAO().getPedidos(comprador);

        ArrayList<Pedido> ordersToShipping=new ArrayList<Pedido>();

        TextView emptyCart=(TextView)getView().findViewById(R.id.emptyCart);

        if(!allOrders.isEmpty()){
            float price=0;
            for( Pedido p1 : allOrders){
                if(!p1.isFinished()){
                    OrderProducto aux=new OrderProducto();
                    aux.setIdOrder(p1.getId());
                    try {
                        aux=(OrderProducto) miBD.getOrderProductsDAO().search(aux);
                        Producto prodAux=new Producto();
                        prodAux.setId(aux.getIdProducto());
                        p1.getProductos().add((Producto) miBD.getProductDAO().search(prodAux));
                        ordersToShipping.add(p1);


                        cantidad+=p1.getCantidadPedido();
                        price+=p1.getCantidadPedido()*p1.getProductos().get(0).getPrecio();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            DecimalFormat df = new DecimalFormat("0.00");

            totalPrice.setText(String.valueOf(df.format(price)));
        }else{
            Toast.makeText(getContext(), "empty", Toast.LENGTH_SHORT).show();
            RelativeLayout rl=(RelativeLayout)getView().findViewById(R.id.relativeCart);
            rl.setVisibility(View.VISIBLE);
            emptyCart.setVisibility(View.VISIBLE);
            totalPrice.setText("0");
        }
        shippingCartList=(ListView)getActivity().findViewById(R.id.shippingCartListView);
        shippingCartList.setAdapter(new AdaptadorListaShipping(this,ordersToShipping));


        Button botonComprar=(Button)getActivity().findViewById(R.id.botonComprarShipping);

        botonComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent crearPedido=new Intent(getActivity(), CrearPedido.class);
               // crearPedido.putExtra("Productos",productos);

                crearPedido.putExtra("Usuario",(Usuario)getActivity().getIntent().getSerializableExtra("Usuario"));
                startActivity(crearPedido);
            }
        });

    }
}