package jogasa.simarro.proyectenadal.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.pojo.FruitShopAPIService;
import jogasa.simarro.proyectenadal.pojo.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterProductos extends ArrayAdapter {

    Activity context;
    TextView nombre,precio;
    ImageView foto;
    ArrayList<Producto> productos;

    public AdapterProductos(Fragment context, ArrayList<Producto> productos){
        super(context.getActivity(), R.layout.activity_adapter_productos,productos);
        this.context=context.getActivity();
        this.productos=productos;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View item=inflater.inflate(R.layout.activity_adapter_productos,null);
        Toast.makeText(context, "adap", Toast.LENGTH_SHORT).show();
         nombre=(TextView)item.findViewById(R.id.textNombre);
         precio=(TextView)item.findViewById(R.id.textPrecio);
         foto=(ImageView)item.findViewById(R.id.textFoto);


       nombre.setText(productos.get(position).getName());
       // precio.setText(String.valueOf(productos.get(position).getPrice())+"€/Kg");
        //foto.setImageResource(productos.get(position).getFoto());

        return item;
    }
}