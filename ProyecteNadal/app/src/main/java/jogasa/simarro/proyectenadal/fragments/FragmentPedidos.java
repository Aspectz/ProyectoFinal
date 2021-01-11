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
import jogasa.simarro.proyectenadal.pojo.Pedido;
import jogasa.simarro.proyectenadal.pojo.Producto;
import jogasa.simarro.proyectenadal.pojo.Tienda;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class FragmentPedidos extends Fragment implements AdapterView.OnItemClickListener {
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
        listaPedidos.setOnItemClickListener(this);
    }

    public void mostrarProductos() {
        ArrayList<Pedido> pedidos = ((Usuario)getActivity().getIntent().getSerializableExtra("Usuario")).getPedidos();
        listaPedidos.setAdapter(new AdaptadorPedidos(this, pedidos));
    }

    @Override
    public void onStart() {
        super.onStart();
        mostrarProductos();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       /* Producto seleccionado = (Producto) grid.getAdapter().getItem(position);

        Intent visualizar = new Intent(getActivity(), VisualizarProductoActivity.class);

        visualizar.putExtra("Producto", seleccionado);
        visualizar.putExtra("Usuario", (Usuario) getActivity().getIntent().getSerializableExtra("Usuario"));

        startActivity(visualizar);*/


    }
}