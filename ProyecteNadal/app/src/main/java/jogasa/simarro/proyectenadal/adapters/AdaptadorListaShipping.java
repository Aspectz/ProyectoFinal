package jogasa.simarro.proyectenadal.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.pojo.OrderDetails;
import jogasa.simarro.proyectenadal.pojo.Producto;

public class AdaptadorListaShipping extends ArrayAdapter {

    Activity context;
    private TextView cantidad,finalPrice;
    private int cantInicial;
    DecimalFormat df = new DecimalFormat("0.00");
    ArrayList<OrderDetails> pedidos = new ArrayList<OrderDetails>();
    FirebaseFirestore fb = FirebaseFirestore.getInstance();

    public AdaptadorListaShipping(Fragment context, ArrayList<OrderDetails> pedidos) {
        super(context.getActivity(), R.layout.adaptador_lista_shipping, pedidos);
        this.context = context.getActivity();
        this.pedidos = pedidos;
    }

    public AdaptadorListaShipping(Activity context, ArrayList<OrderDetails> pedidos) {
        super(context, R.layout.adaptador_lista_shipping, pedidos);
        this.context = context;
        this.pedidos = pedidos;
    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.adaptador_lista_shipping, null);


        TextView nombreProducto = (TextView) item.findViewById(R.id.nombreProductoShipping);
        cantidad = (TextView) item.findViewById(R.id.cantidadProductoShipping);
        ImageView imagen = (ImageView) item.findViewById(R.id.pedidoFotoShipping);
        TextView precioUnico = (TextView) item.findViewById(R.id.precioProductoShipping);
        finalPrice = (TextView) item.findViewById(R.id.priceFinalShipping);
        Button addButon = (Button) item.findViewById(R.id.addOneToCart);
        Button removeButton = (Button) item.findViewById(R.id.removeOneToCart);


        cantInicial=pedidos.get(position).getQuantity();
        cantidad.setText(String.valueOf(pedidos.get(position).getQuantity()));
        finalPrice.setText(df.format(pedidos.get(position).getTotalPrice())+"€");

        fb.collection("Products").document(pedidos.get(position).getIdProducto()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Producto producto = task.getResult().toObject(Producto.class);
                    nombreProducto.setText(producto.getNombre());
                    precioUnico.setText(String.valueOf(producto.getPrecio()) + "/kg");
                    Glide.with(getContext()).load(producto.getFotos().get(0)).into(imagen);
                }
            }
        });

        addButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cantInicial=pedidos.get(position).getQuantity()+1;
                LinearLayout vwParentRow = (LinearLayout)v.getParent();
                TextView child = (TextView)vwParentRow.getChildAt(1);

                LinearLayout vwGrandParent=(LinearLayout)vwParentRow.getParent();
                TextView unitPriceTxt=(TextView)vwGrandParent.getChildAt(2);


                //Get total price in fragment
                TextView finalPrice=context.findViewById(R.id.totalPrice);


                child.setText(String.valueOf(cantInicial));
                addOrRemoveToCart(pedidos.get(position),cantInicial,unitPriceTxt,finalPrice);

            }
        });
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cantInicial=pedidos.get(position).getQuantity()-1;
                LinearLayout vwParentRow = (LinearLayout)v.getParent();
                TextView child = (TextView)vwParentRow.getChildAt(1);


                //Get unit price
                LinearLayout vwGrandParent=(LinearLayout)vwParentRow.getParent();
                TextView unitPriceTxt=(TextView)vwGrandParent.getChildAt(2);
                child.setText(String.valueOf(cantInicial));

                //Get first layout to delete when cantity reach 0
                LinearLayout vwFirstLayout=(LinearLayout)vwParentRow.getParent().getParent();
                TextView finalPrice=context.findViewById(R.id.totalPrice);


                if(cantInicial<=0){
                    vwFirstLayout.setVisibility(View.GONE);
                    fb.collection("OrderDetails").document(pedidos.get(position).getIdOrderDetails()).delete();
                }else{
                    addOrRemoveToCart(pedidos.get(position),cantInicial,unitPriceTxt,finalPrice);
                }

            }
        });
        return item;
    }

    public void addOrRemoveToCart(OrderDetails od,int newCant,TextView unitPriceTxt,TextView finalPrice ) {
        fb.collection("OrderDetails").document(od.getIdOrderDetails()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    fb.collection("Products").document(od.getIdProducto()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                Producto p=task.getResult().toObject(Producto.class);
                                od.setQuantity(newCant);
                                od.setTotalPrice(p.getPrecio()*newCant);
                                unitPriceTxt.setText(df.format(od.getTotalPrice())+"€");
                                fb.collection("OrderDetails").document(od.getIdOrderDetails()).set(od);
                                float fP=0;
                                for(OrderDetails od : pedidos){
                                    fP+=od.getTotalPrice();
                                }
                                finalPrice.setText(df.format(fP));
                            }
                        }
                    });
                }
            }
        });
    }


}