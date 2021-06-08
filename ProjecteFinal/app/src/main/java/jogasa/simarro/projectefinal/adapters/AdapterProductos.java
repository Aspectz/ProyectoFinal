package jogasa.simarro.projectefinal.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


import jogasa.simarro.projectefinal.pojo.Producto;
import jogasa.simarro.projectefinal.R;

public class AdapterProductos extends ArrayAdapter {

    Activity context;
    TextView nombre, precio;
    ImageView foto;
    ArrayList<Producto> productos;
    FirebaseFirestore fb = FirebaseFirestore.getInstance();

    public AdapterProductos(Activity context, ArrayList<Producto> productos) {

        super(context, R.layout.adapter_productos, productos);
        this.context = context;
        this.productos = productos;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.adapter_productos, null);
        nombre = (TextView) item.findViewById(R.id.textNombre);
        precio = (TextView) item.findViewById(R.id.textPrecio);
        foto = (ImageView) item.findViewById(R.id.textFoto);

        nombre.setText(productos.get(position).getNombre());
        precio.setText(String.valueOf(productos.get(position).getPrecio()) + "€/Kg");

        Glide.with(getContext()).load(productos.get(position).getFotos().get(0)).into(foto);


        //foto.setImageResource(productos.get(position).getFoto());

        return item;
    }

}