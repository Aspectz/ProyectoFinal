package jogasa.simarro.proyectenadal.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.activity.CrearPedido;
import jogasa.simarro.proyectenadal.pojo.Producto;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class FragmentComprar extends Fragment {

    private Producto producto;
    private Usuario comprador;

    public FragmentComprar(Producto producto, Usuario usuario){
        this.producto=producto;
        this.comprador=usuario;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.activity_fragment_comprar,container,false);

        TextView nombre=view.findViewById(R.id.nombreProducto);
        TextView precio=view.findViewById(R.id.precioProducto);
        TextView descripcion=view.findViewById(R.id.descripcionProducto);
        ImageView foto=view.findViewById(R.id.imagenProducto);


        nombre.setText(producto.getNombre());
        precio.setText(String.valueOf(producto.getPrecio())+"€/Kg");
        descripcion.setText(producto.getDescripcion());
        foto.setImageResource(producto.getFoto());

        Button boton=(Button)view.findViewById(R.id.comprarButton);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent crearPedido=new Intent(getActivity(), CrearPedido.class);
                crearPedido.putExtra("Producto",producto);
                crearPedido.putExtra("Usuario",comprador);
                startActivity(crearPedido);
            }
        });

        return view;
    }
}