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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import java.lang.reflect.Field;
import java.util.ArrayList;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.activity.CrearPedido;
import jogasa.simarro.proyectenadal.activity.MainActivity;
import jogasa.simarro.proyectenadal.pojo.PedidoSinCompletar;
import jogasa.simarro.proyectenadal.pojo.Producto;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class FragmentComprar extends Fragment {

    private Producto producto;
    private Usuario comprador;
    int cantidad;
    ImageView favFoto;
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
        Spinner cantity=view.findViewById(R.id.cantitySpinner);
        favFoto=view.findViewById(R.id.isFavBtn);

       // if(producto.isFav()) favFoto.setImageResource(R.drawable.estrellafav);
        //else favFoto.setImageResource(R.drawable.estrella);

        final ArrayList<String> productMaxCantity=new ArrayList<String>();

        /*for(int i=1;i<=producto.getLimiteProducto();i++){
            productMaxCantity.add(String.valueOf(i));
        }*/


        final ArrayAdapter<String>adapterCantity=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,productMaxCantity);

        cantity.setAdapter(adapterCantity);

        cantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cantidad=Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /*nombre.setText(producto.getNombre());
        precio.setText(String.valueOf(producto.getPrecio())+"€/Kg");
        descripcion.setText(producto.getDescripcion());
        foto.setImageResource(producto.getFoto());*/

        Button botonComprar=(Button)view.findViewById(R.id.comprarButton);

        botonComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent crearPedido=new Intent(getActivity(), CrearPedido.class);
               // crearPedido.putExtra("Producto",producto);
                crearPedido.putExtra("Cantidad",cantidad);
                crearPedido.putExtra("Usuario",comprador);
                startActivity(crearPedido);
            }
        });

        Button btnAddtoShipping=(Button)view.findViewById(R.id.addToShoppingCartBtn);
        btnAddtoShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addtoshipping=new Intent(getActivity(), MainActivity.class);
                //addtoshipping.putExtra("Producto",producto);
                addtoshipping.putExtra("Usuario",comprador);
               // comprador.getPedidosSinCompletar().add(new PedidoSinCompletar(producto.getNombre(),producto,cantidad));
                removeDuplicates(comprador.getPedidosSinCompletar());
                startActivity(addtoshipping);
            }
        });

        favFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if(producto.isFav()){
                    favFoto.setImageResource(R.drawable.estrella);
                    producto.setFav(!producto.isFav());

                }
                else{
                    favFoto.setImageResource(R.drawable.estrellafav);
                    producto.setFav(!producto.isFav());

                }*/
            }
        });

        return view;
    }

    private void removeDuplicates(ArrayList<PedidoSinCompletar> pedidosSinCompletar){

        for(int i=0;i<pedidosSinCompletar.size();i++){
            for(int j=i+1;j<pedidosSinCompletar.size();j++){
                if(pedidosSinCompletar.get(i).getNombre().equals(pedidosSinCompletar.get(j).getNombre())){
                    pedidosSinCompletar.get(i).setCantidad(pedidosSinCompletar.get(i).getCantidad()+pedidosSinCompletar.get(j).getCantidad());
                    pedidosSinCompletar.remove(pedidosSinCompletar.get(j));
                }
            }
        }
    }
}