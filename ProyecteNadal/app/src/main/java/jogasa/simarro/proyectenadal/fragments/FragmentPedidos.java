package jogasa.simarro.proyectenadal.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.adapters.AdaptadorPedidos;
import jogasa.simarro.proyectenadal.adapters.AdapterProductos;
import jogasa.simarro.proyectenadal.bd.MiBD;
import jogasa.simarro.proyectenadal.pojo.Pedido;
import jogasa.simarro.proyectenadal.pojo.Producto;
import jogasa.simarro.proyectenadal.pojo.Tienda;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class FragmentPedidos extends Fragment {
    public ListView listaPedidos;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_fragment_pedidos, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listaPedidos = (ListView) getView().findViewById(R.id.listaPedidos);

    }

    public void mostrarProductos() {
        Usuario comprador=(Usuario)getActivity().getIntent().getSerializableExtra("Usuario");


        ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
        ArrayList<Pedido> listaAux= MiBD.getInstance(getContext()).getOrderDAO().getPedidos(comprador);
        for( Pedido p : listaAux){
            if(p.isFinished()){
                pedidos.add(p);
            }
        }
        listaPedidos.setAdapter(new AdaptadorPedidos(this, pedidos));
    }

    @Override
    public void onStart() {
        super.onStart();
        mostrarProductos();
    }


}