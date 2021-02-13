package jogasa.simarro.proyectenadal.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.activity.VisualizarProductoActivity;
import jogasa.simarro.proyectenadal.adapters.AdapterProductos;
import jogasa.simarro.proyectenadal.pojo.FruitShopAPIService;
import jogasa.simarro.proyectenadal.pojo.Producto;
import jogasa.simarro.proyectenadal.pojo.Tienda;
import jogasa.simarro.proyectenadal.pojo.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FragmentInicio extends Fragment implements AdapterView.OnItemClickListener {
    public GridView grid;
    ArrayList<Producto> productos=new ArrayList<Producto>();
    FruitShopAPIService service;
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

        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://api.predic8.de/shop/").addConverterFactory(GsonConverterFactory.create()).build();

        service=retrofit.create(FruitShopAPIService.class);
        Button button=(Button)getView().findViewById(R.id.botonPrueba);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<Producto> call=service.getComick(12);


                call.enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Call<Producto> call, Response<Producto> response) {
                        Producto producto=response.body();
                        try{

                            if(producto!=null){
                                productos.add(producto);
                                mostrarProductos();
                            }else{
                                Toast.makeText(getContext(), "isNull", Toast.LENGTH_SHORT).show();
                            }
                        }catch(Exception e){
                            Log.e("ERROR",e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<Producto> call, Throwable t) {
                        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        grid.setAdapter(new AdapterProductos(this,productos));

    }
    public void mostrarProductos(){
       // ArrayList<Producto> productos= Tienda.getProductos();
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

        //visualizar.putExtra("Producto",seleccionado);
        visualizar.putExtra("Usuario",(Usuario)getActivity().getIntent().getSerializableExtra("Usuario"));

        startActivity(visualizar);



    }
}