package jogasa.simarro.proyectenadal.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


import java.util.ArrayList;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.activity.ListaPedidos;
import jogasa.simarro.proyectenadal.activity.MiCuentaActivity;
import jogasa.simarro.proyectenadal.bd.MiBD;
import jogasa.simarro.proyectenadal.pojo.Pedido;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class FragmentMiCuenta extends Fragment {

    private Usuario user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.activity_fragment_mi_cuenta,container,false);

        TextView nombre=(TextView)view.findViewById(R.id.name);
        TextView email=(TextView)view.findViewById(R.id.email);
        TextView lastAddress=(TextView)view.findViewById(R.id.lastAddress);

        user=(Usuario)getActivity().getIntent().getSerializableExtra("Usuario");

        String name=getString(R.string.names);

        String emails=getString(R.string.emails);
        String address=getString(R.string.lastShippingAddress);

        Pedido ultimoPedido=(Pedido)MiBD.getInstance(getContext()).getOrderDAO().getPedidos(user).get(MiBD.getInstance(getContext()).getOrderDAO().getPedidos(user).size()-1);

        nombre.setText(name+":"+user.getNombre());
        email.setText(emails+":"+user.getEmail());
        if(!user.getPedidos().isEmpty()){
            lastAddress.setText(address+":"+ultimoPedido.getDireccionEnvio());
        }


        TextView pedidos=(TextView)view.findViewById(R.id.orders);
        pedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listaPedidos=new Intent(getActivity(), ListaPedidos.class);
                listaPedidos.putExtra("Usuario",user);
                startActivity(listaPedidos);
            }
        });


        return view;
    }
}