package jogasa.simarro.proyectenadal.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.pojo.Producto;

public class AdapterProductos extends ArrayAdapter {

    Activity context;
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
        TextView nombre=(TextView)item.findViewById(R.id.textNombre);
        TextView precio=(TextView)item.findViewById(R.id.textPrecio);
        ImageView foto=(ImageView)item.findViewById(R.id.textFoto);

        nombre.setText(productos.get(position).getNombre());
        precio.setText(String.valueOf(productos.get(position).getPrecio())+"€/Kg");
        foto.setImageResource(productos.get(position).getFoto());

        return item;
    }
}