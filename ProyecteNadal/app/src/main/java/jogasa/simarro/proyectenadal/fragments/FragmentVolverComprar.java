package jogasa.simarro.proyectenadal.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.adapters.AdaptadorVolverComprar;
import jogasa.simarro.proyectenadal.pojo.Pedido;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class FragmentVolverComprar extends Fragment implements AdapterView.OnItemClickListener {
    public GridView listaPedidos;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_volver_comprar, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listaPedidos = (GridView) getView().findViewById(R.id.gridProductos);
        listaPedidos.setOnItemClickListener(this);

    }

    public void mostrarProductos() {
        Usuario comprador=(Usuario)getActivity().getIntent().getSerializableExtra("Usuario");
        ArrayList<Pedido> pedidos = new ArrayList<Pedido>();


        listaPedidos.setAdapter(new AdaptadorVolverComprar(this, pedidos));
    }

    @Override
    public void onStart() {
        super.onStart();
        mostrarProductos();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       /* Pedido seleccionado = (Pedido) listaPedidos.getAdapter().getItem(position);

        Producto prod=(Producto)seleccionado.getProductos().get(0);


        Intent visualizar = new Intent(getActivity(), VisualizarProductoActivity.class);

        visualizar.putExtra("Producto", seleccionado);
        visualizar.putExtra("Usuario", (Usuario) getActivity().getIntent().getSerializableExtra("Usuario"));

        startActivity(visualizar);*/


    }
}