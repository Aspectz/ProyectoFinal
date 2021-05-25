package jogasa.simarro.proyectenadal.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;


import org.json.JSONException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import jogasa.simarro.proyectenadal.R;


import jogasa.simarro.proyectenadal.adapters.AdaptadorListaShipping;
import jogasa.simarro.proyectenadal.api.Config;
import jogasa.simarro.proyectenadal.pojo.Estados;
import jogasa.simarro.proyectenadal.pojo.OrderDetails;
import jogasa.simarro.proyectenadal.pojo.Pedido;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class CrearPedido extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private ListView listadoCompra;
    private EditText nombreDestinatario, metodoFacturacion, direccionEnvio;
    private FirebaseFirestore fb = FirebaseFirestore.getInstance();
    private float price = 0;
    private TextView priceTotal;

    private static final int  PAYPAL_REQUEST_CODE=7171;
    private static PayPalConfiguration config=new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Config.PAYPAL_CLIENT_ID);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_pedido);


        Intent intent=new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);


        priceTotal=(TextView) findViewById(R.id.totalPriceSummary);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        String option = getIntent().getExtras().getSerializable("Option").toString();
        //Recoger headerLayout
        View headerLayout = navigationView.getHeaderView(0);
        TextView headerText = (TextView) headerLayout.findViewById(R.id.textHeader);

        fb.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        Usuario usuarioLogeado = doc.toObject(Usuario.class);
                        headerText.setText(getResources().getString(R.string.hello) + usuarioLogeado.getNombre());
                    }
                }
            }
        });
        getSupportActionBar().setTitle("Order");


        //PEDIDOS
        listadoCompra = (ListView) findViewById(R.id.listadoCompra);
        nombreDestinatario = (EditText) findViewById(R.id.nombreDestinatario);
        metodoFacturacion = (EditText) findViewById(R.id.metodoFacturacion);
        direccionEnvio = (EditText) findViewById(R.id.direccionEnvio);


        Button btnComprar = (Button) findViewById(R.id.botonComprarSummary);
        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nombreDestinatario.getText().toString().isEmpty() && !metodoFacturacion.getText().toString().isEmpty() && !direccionEnvio.getText().toString().isEmpty()) {
                    if (option.equals("Buy")) {
                        OrderDetails orderDetails = (OrderDetails) getIntent().getSerializableExtra("OrderDetail");
                        buy(orderDetails);
                        //processPayment();
                        Intent home = new Intent(CrearPedido.this, HomeActivity.class);
                        startActivity(home);
                    }
                    if (option.equals("Cart")) {
                        buyFromCart();
                       // processPayment();
                       Intent home = new Intent(CrearPedido.this, HomeActivity.class);
                        startActivity(home);
                    }
                } else {
                    Toast.makeText(CrearPedido.this, "Los campos son obligatorios", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void buyFromCart() {
        fb.collection("Orders").whereEqualTo("estado", Estados.CARRITO).whereEqualTo("idUser", firebaseAuth.getCurrentUser().getUid()).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot pedido : task.getResult()) {

                        Map<String,Object> pd=new HashMap<>();
                        Pedido pedido1=pedido.toObject(Pedido.class);
                        pd.put("direccionEnvio",direccionEnvio.getText().toString());
                        pd.put("metodoFacturacion",metodoFacturacion.getText().toString());
                        pd.put("estado",Estados.CARRITO);
                        pd.put("fecha",FieldValue.serverTimestamp());
                        pd.put("id",pedido1.getId());
                        pd.put("idUser",pedido1.getIdUser());
                        pd.put("nombre",pedido1.getNombre());

                       /* pedido1.setEstado(Estados.ESPERANDO);
                        pedido1.setDireccionEnvio(direccionEnvio.getText().toString());
                        pedido1.setMetodoFacturacion(metodoFacturacion.getText().toString());
                        pedido1.setFechacreacionPedido(FieldValue.serverTimestamp());*/
                        fb.collection("Orders").document(String.valueOf(pedido1.getId())).set(pd);
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

                Map<String,Object> pd=new HashMap<>();
                Pedido pedido1=task.getResult().toObject(Pedido.class);
                pd.put("direccionEnvio",direccionEnvio.getText().toString());
                pd.put("metodoFacturacion",metodoFacturacion.getText().toString());
                pd.put("estado",Estados.ESPERANDO);
                pd.put("fecha",FieldValue.serverTimestamp());
                pd.put("id",pedido1.getId());
                pd.put("idUser",pedido1.getIdUser());
                pd.put("nombre",pedido1.getNombre());


               /* pedido.setEstado(Estados.ESPERANDO);
                pedido.setDireccionEnvio(direccionEnvio.getText().toString());
                pedido.setMetodoFacturacion(metodoFacturacion.getText().toString());
                pedido.setFechacreacionPedido(FieldValue.serverTimestamp().toString());*/
                fb.collection("Orders").document(String.valueOf(orderDetails.getIdOrder())).set(pedido);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DecimalFormat df = new DecimalFormat("0.00");

        ArrayList<OrderDetails> orderDetailsList=new ArrayList<OrderDetails>();
        Activity activity=this;
        String option = getIntent().getExtras().getSerializable("Option").toString();
        if (option.equals("Buy")) {
            OrderDetails orderDetails = (OrderDetails) getIntent().getSerializableExtra("OrderDetail");
            fb.collection("OrderDetails").document(orderDetails.getIdOrderDetails()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        priceTotal.setText(String.valueOf(df.format(task.getResult().toObject(OrderDetails.class).getTotalPrice())));
                        orderDetailsList.add(task.getResult().toObject(OrderDetails.class));
                        listadoCompra.setAdapter(new AdaptadorListaShipping(activity,orderDetailsList));
                    }
                }
            });
        }
        if (option.equals("Cart")) {
            fb.collection("Orders").whereEqualTo("estado", Estados.CARRITO).whereEqualTo("idUser", firebaseAuth.getCurrentUser().getUid()).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot pedido : task.getResult()){
                            fb.collection("OrderDetails").whereEqualTo("idOrder",pedido.toObject(Pedido.class).getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(QueryDocumentSnapshot od : task.getResult()){
                                            price+=od.toObject(OrderDetails.class).getTotalPrice();
                                            orderDetailsList.add(od.toObject(OrderDetails.class));
                                        }
                                        listadoCompra.setAdapter(new AdaptadorListaShipping(activity,orderDetailsList));
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
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();



        //OrderDetails orderDetails = (OrderDetails) getIntent().getSerializableExtra("OrderDetail");
        //fb.collection("OrderDetails").document(orderDetails.getIdOrder()+orderDetails.getIdProducto()).delete();
        //fb.collection("Pedidos").document(String.valueOf(orderDetails.getIdOrder())).delete();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, PaymentDetails.class).putExtra("PaymentDetails", paymentDetails).putExtra("PaymentAmount", price));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (resultCode == PaymentActivity.RESULT_CANCELED) {
            Toast.makeText(this, "invalid", Toast.LENGTH_SHORT).show();
        }else if(resultCode==PaymentActivity.RESULT_EXTRAS_INVALID){
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }
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
            case R.id.buyAgainItem:
                Intent buyagain = new Intent(CrearPedido.this, VolverAcomprarActivity.class);
                startActivity(buyagain);
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
            case R.id.aboutUs:
                Intent aboutUs = new Intent(CrearPedido.this, AboutUsActivity.class);
                startActivity(aboutUs);
                break;
            default:
                return false;
        }
        drawerLayout.closeDrawers();
        return false;
    }

    private String formatDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(cal.getTime());
        String formatted = format1.format(cal.getTime());
        return formatted;
    }

    private void processPayment(){
        price= Float.parseFloat(priceTotal.getText().toString());
        PayPalPayment payPalPayment=new PayPalPayment(new BigDecimal(price),"EUR","Payment",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent=new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }
}