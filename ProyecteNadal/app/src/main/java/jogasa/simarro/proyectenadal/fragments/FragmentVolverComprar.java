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

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.adapters.AdaptadorPedidos;
import jogasa.simarro.proyectenadal.adapters.AdaptadorVolverComprar;
import jogasa.simarro.proyectenadal.pojo.Pedido;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class FragmentVolverComprar extends Fragment {
    public GridView listaPedidos;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_fragment_volver_comprar, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listaPedidos = (GridView) getView().findViewById(R.id.gridProductos);

    }

    public void mostrarProductos() {
        ArrayList<Pedido> pedidos = ((Usuario)getActivity().getIntent().getSerializableExtra("Usuario")).getPedidos();
        listaPedidos.setAdapter(new AdaptadorVolverComprar(this, pedidos));
    }

    @Override
    public void onStart() {
        super.onStart();
        mostrarProductos();
    }
}