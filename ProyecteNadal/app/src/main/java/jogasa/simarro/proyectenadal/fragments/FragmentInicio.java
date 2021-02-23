package jogasa.simarro.proyectenadal.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.util.ArrayList;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.activity.VisualizarProductoActivity;
import jogasa.simarro.proyectenadal.adapters.AdapterProductos;
import jogasa.simarro.proyectenadal.bd.MiBD;
import jogasa.simarro.proyectenadal.pojo.Producto;
import jogasa.simarro.proyectenadal.pojo.Usuario;


public class FragmentInicio extends Fragment implements AdapterView.OnItemClickListener {
    public GridView grid;
    ArrayList<Producto> productos=new ArrayList<Producto>();

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

        grid.setAdapter(new AdapterProductos(this,productos));

    }
    public void mostrarProductos() throws ParseException {
        ArrayList<Producto> productos= MiBD.getInstance(getContext()).getProductDAO().getAll();
        for(Producto p : productos){
            if(p.getNombre().equals("Banana")) p.setFoto(R.drawable.banana);
            if(p.getNombre().equals("Aguacate"))p.setFoto(R.drawable.aguacate);
            if(p.getNombre().equals("Limon"))p.setFoto(R.drawable.limon);
            if(p.getNombre().equals("Cereza"))p.setFoto(R.drawable.cereza);
            if(p.getNombre().equals("Fresa"))p.setFoto(R.drawable.fresa);
            if(p.getNombre().equals("Naranja"))p.setFoto(R.drawable.naranja);
            if(p.getNombre().equals("Manzana"))p.setFoto(R.drawable.manzana);
            if(p.getNombre().equals("Arandano"))p.setFoto(R.drawable.arandano);
            if(p.getNombre().equals("Pepino"))p.setFoto(R.drawable.pepino);
        }



        grid.setAdapter(new AdapterProductos(this,productos));
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mostrarProductos();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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