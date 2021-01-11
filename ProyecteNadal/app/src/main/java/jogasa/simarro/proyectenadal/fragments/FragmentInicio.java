package jogasa.simarro.proyectenadal.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.activity.VisualizarProductoActivity;
import jogasa.simarro.proyectenadal.adapters.AdapterProductos;
import jogasa.simarro.proyectenadal.pojo.Producto;
import jogasa.simarro.proyectenadal.pojo.Tienda;
import jogasa.simarro.proyectenadal.pojo.Usuario;


public class FragmentInicio extends Fragment implements AdapterView.OnItemClickListener {
    public GridView grid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=  inflater.inflate(R.layout.activity_fragment_inicio,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        grid=(GridView)getView().findViewById(R.id.gridProductos);
        grid.setOnItemClickListener(this);
    }
    public void mostrarProductos(){
        ArrayList<Producto> productos= Tienda.getProductos();
        grid.setAdapter(new AdapterProductos(this,productos));
    }

    @Override
    public void onStart() {
        super.onStart();
        mostrarProductos();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Producto seleccionado=(Producto)grid.getAdapter().getItem(position);

        Intent visualizar=new Intent(getActivity(), VisualizarProductoActivity.class);

        visualizar.putExtra("Producto",seleccionado);
        visualizar.putExtra("Usuario",(Usuario)getActivity().getIntent().getSerializableExtra("Usuario"));

        startActivity(visualizar);



    }
}