package jogasa.simarro.projectefinal.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;



import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import jogasa.simarro.projectefinal.adapters.AdaptadorListaShipping;
import jogasa.simarro.projectefinal.dialog.DireccionDialog;
import jogasa.simarro.projectefinal.pojo.Direccion;
import jogasa.simarro.projectefinal.pojo.Estados;
import jogasa.simarro.projectefinal.pojo.OrderDetails;
import jogasa.simarro.projectefinal.pojo.Pedido;
import jogasa.simarro.projectefinal.R;



public class CrearPedido extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private ListView listadoCompra;
    private EditText metodoFacturacion;
    private FirebaseFirestore fb = FirebaseFirestore.getInstance();
    private float price = 0;
    private TextView priceTotal;
    String randomKey = UUID.randomUUID().toString();
    private Direccion direccion;
    private SmartMaterialSpinner<Direccion> direccionSpinner;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_pedido);

        direccionSpinner=findViewById(R.id.direccionEnvio);
        List<Direccion> direcciones=new ArrayList<>();
        //pepe@yopmail.com


        DireccionDialog dg = new DireccionDialog(this);

        fb.collection("Address").whereEqualTo("idUser", firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot address : task.getResult()) {
                            Direccion d = address.toObject(Direccion.class);
                            direcciones.add(d);
                        }
                    direccionSpinner.setItem(direcciones);

                    direccionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                direccion=direcciones.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
        });

        Button addnew=findViewById(R.id.addShippingAddress);
        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dg.show();
                Button guardar = (Button) dg.findViewById(R.id.saveAddress);
                TextView cancelar = (TextView) dg.findViewById(R.id.cancelAddress);
                EditText nombre = (EditText) dg.findViewById(R.id.addressNombre);
                EditText cp = (EditText) dg.findViewById(R.id.addressCP);
                EditText provincia = (EditText) dg.findViewById(R.id.addressProvincia);
                EditText pais = (EditText) dg.findViewById(R.id.addressPais);
                EditText direccionET = (EditText) dg.findViewById(R.id.addressDireccion);
                EditText poblacion = (EditText) dg.findViewById(R.id.addressPoblacion);

                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        direccion = new Direccion(nombre.getText().toString(), direccionET.getText().toString(),
                                cp.getText().toString(), poblacion.getText().toString(), provincia.getText().toString(), pais.getText().toString());
                        direccion.setIdUser(firebaseAuth.getCurrentUser().getUid());
                        direccion.setIdAddress(randomKey);
                        direcciones.add(direccion);
                        fb.collection("Address").document(randomKey).set(direccion).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    recreate();
                                    dg.dismiss();
                                }
                            }
                        });
                    }
                });
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dg.dismiss();
                    }
                });

            }
        });



        priceTotal = (TextView) findViewById(R.id.totalPrice);
        String option = getIntent().getExtras().getSerializable("Option").toString();

        //PEDIDOS
        ImageButton back = (ImageButton) findViewById(R.id.backAddPhoto);
        listadoCompra = (ListView) findViewById(R.id.listadoCompra);
        metodoFacturacion = (EditText) findViewById(R.id.metodoFacturacion);
        // direccionEnvio = (EditText) findViewById(R.id.direccionEnvio);


        Button btnComprar = (Button) findViewById(R.id.botonComprarSummary);
        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!metodoFacturacion.getText().toString().isEmpty() && direccionSpinner.getSelectedItem()!=null) {
                    if (option.equals("Buy")) {
                        fb.collection("Orders").whereEqualTo("estado",Estados.PROCESANDO).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot q : task.getResult()){
                                        buy(q.toObject(OrderDetails.class));
                                    }
                                }
                            }
                        });
                    }
                    if (option.equals("Cart")) {
                        buyFromCart();

                    }
                } else {
                    Toast.makeText(CrearPedido.this, getResources().getString(R.string.fieldsRequired), Toast.LENGTH_SHORT).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CrearPedido.this, ShippingCart.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        OrderDetails od=(OrderDetails) getIntent().getSerializableExtra("OrderDetail");
        if(od!=null){
            fb.collection("OrderDetails").document(od.getIdOrderDetails()).delete();
            fb.collection("Orders").document(String.valueOf(od.getIdOrder())).delete();
        }

    }

    private void buyFromCart() {
        fb.collection("Orders").whereEqualTo("estado", Estados.CARRITO).whereEqualTo("idUser", firebaseAuth.getCurrentUser().getUid()).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot pedido : task.getResult()) {

                        Map<String, Object> pd = new HashMap<>();
                        Pedido pedido1 = pedido.toObject(Pedido.class);
                        pd.put("metodoFacturacion", metodoFacturacion.getText().toString());
                        pd.put("estado", Estados.ESPERANDO);
                        pd.put("fecha", new Date(System.currentTimeMillis()).toString());
                        pd.put("id", pedido1.getId());
                        pd.put("idUser", pedido1.getIdUser());
                        pd.put("direccion", direccion);
                        fb.collection("Orders").document(String.valueOf(pedido1.getId())).set(pd).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Intent home = new Intent(CrearPedido.this, HomeActivity.class);
                                    startActivity(home);
                                }
                            }
                        });

                    }
                }
            }
        });
    }


    private void buy(OrderDetails orderDetails) {
        fb.collection("Orders").document(String.valueOf(orderDetails.getIdOrder())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Pedido pedido = task.getResult().toObject(Pedido.class);

                Log.d("Pepe", "compraDirecta");

                Map<String, Object> pd = new HashMap<>();
                Pedido pedido1 = task.getResult().toObject(Pedido.class);
                pd.put("metodoFacturacion", metodoFacturacion.getText().toString());
                pd.put("estado", Estados.ESPERANDO);
                pd.put("fecha", new Date(System.currentTimeMillis()).toString());
                pd.put("id", pedido1.getId());
                pd.put("idUser", pedido1.getIdUser());
                pd.put("direccion", direccion);

                //  pd.put("nombre",pedido1.getNombre());

                fb.collection("Orders").document(String.valueOf(orderDetails.getIdOrder())).set(pd).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent home = new Intent(CrearPedido.this, HomeActivity.class);
                            startActivity(home);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DecimalFormat df = new DecimalFormat("0.00");

        ArrayList<OrderDetails> orderDetailsList = new ArrayList<OrderDetails>();
        Activity activity = this;
        String option = getIntent().getExtras().getSerializable("Option").toString();
        if (option.equals("Buy")) {
            OrderDetails orderDetails = (OrderDetails) getIntent().getSerializableExtra("OrderDetail");
            fb.collection("OrderDetails").document(orderDetails.getIdOrderDetails()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        priceTotal.setText(String.valueOf(df.format(task.getResult().toObject(OrderDetails.class).getTotalPrice())));
                        orderDetailsList.add(task.getResult().toObject(OrderDetails.class));
                        listadoCompra.setAdapter(new AdaptadorListaShipping(activity, orderDetailsList));
                    }
                }
            });
        }
        if (option.equals("Cart")) {
            fb.collection("Orders").whereEqualTo("estado", Estados.CARRITO).whereEqualTo("idUser", firebaseAuth.getCurrentUser().getUid()).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot pedido : task.getResult()) {
                            fb.collection("OrderDetails").whereEqualTo("idOrder", pedido.toObject(Pedido.class).getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot od : task.getResult()) {
                                            price += od.toObject(OrderDetails.class).getTotalPrice();
                                            orderDetailsList.add(od.toObject(OrderDetails.class));
                                        }
                                        listadoCompra.setAdapter(new AdaptadorListaShipping(activity, orderDetailsList));
                                        priceTotal.setText(String.valueOf(df.format(price)));
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.homeItem:
                Intent home = new Intent(CrearPedido.this, HomeActivity.class);
                startActivity(home);
                break;
            case R.id.orderItem:
                Intent listaPedidos = new Intent(CrearPedido.this, ListaPedidos.class);
                startActivity(listaPedidos);
                break;
            case R.id.accountItem:
                Intent micuenta = new Intent(CrearPedido.this, MiCuentaActivity.class);
                startActivity(micuenta);
                break;
            case R.id.logOut:
                AuthUI.getInstance().signOut(this);
                FirebaseAuth.getInstance().signOut();
                Intent cerrarSession = new Intent(CrearPedido.this, LoginActivity.class);
                startActivity(cerrarSession);
                break;
            case R.id.options:
                Intent options = new Intent(CrearPedido.this, SettingsActivity.class);
                startActivity(options);
                break;
            default:
                return false;
        }
        drawerLayout.closeDrawers();
        return false;
    }

}