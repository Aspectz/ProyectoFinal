package jogasa.simarro.proyectenadal.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.adapters.AdaptadorListaShipping;
import jogasa.simarro.proyectenadal.bd.MiBD;

import jogasa.simarro.proyectenadal.pojo.OrderProducto;
import jogasa.simarro.proyectenadal.pojo.Pedido;
import jogasa.simarro.proyectenadal.pojo.Producto;

import jogasa.simarro.proyectenadal.pojo.Usuario;

public class CrearPedido extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private Usuario usuarioLogeado;
    private ArrayList<Producto> productosSeleccionado;
    private ListView listadoCompra;
    private ArrayList<Pedido> pedidos=new ArrayList<Pedido>();
    private ArrayList<Pedido> pedidosAux;
    private ArrayList<Producto> productos;
    private EditText nombreDestinatario, metodoFacturacion,direccionEnvio;
    private MiBD miBD;
    private float price=0;

    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_pedido);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        miBD=MiBD.getInstance(getApplicationContext());
        usuarioLogeado=(Usuario)getIntent().getSerializableExtra("Usuario");
        productosSeleccionado=(ArrayList<Producto>)getIntent().getSerializableExtra("Productos");

        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        navigationView=(NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        //Recoger headerLayout
        View headerLayout=navigationView.getHeaderView(0);
        TextView headerText=(TextView)headerLayout.findViewById(R.id.textHeader);

        headerText.setText(getResources().getString(R.string.hello)+usuarioLogeado.getNombre());

        getSupportActionBar().setTitle("Order");

        //PEDIDOS


        Usuario comprador=(Usuario)getIntent().getSerializableExtra("Usuario");
        listadoCompra=(ListView)findViewById(R.id.listadoCompra);

        nombreDestinatario=(EditText)findViewById(R.id.nombreDestinatario);
        metodoFacturacion=(EditText)findViewById(R.id.metodoFacturacion);
        direccionEnvio=(EditText)findViewById(R.id.direccionEnvio);
        pedidosAux= miBD.getOrderDAO().getPedidos(comprador);
        productos=new ArrayList<Producto>();

        makeOrder();

        listadoCompra.setAdapter(new AdaptadorListaShipping(this,pedidos));

        Button btnComprar=(Button)findViewById(R.id.botonComprarSummary);
        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nombreDestinatario.getText().toString().isEmpty() && !metodoFacturacion.getText().toString().isEmpty() && !direccionEnvio.getText().toString().isEmpty()){
                    Pedido pedido=new Pedido(productos.get(0).getNombre(),metodoFacturacion.getText().toString(),direccionEnvio.getText().toString(), formatDate(),price,productos);
                    pedido.setFinished(true);
                    miBD.getOrderDAO().insertOrderToClient(pedido,usuarioLogeado);

                    ArrayList<Pedido> pedABorrar=miBD.getOrderDAO().getPedidos(usuarioLogeado);

                    for(Pedido p1 : pedABorrar){
                        if(!p1.isFinished()){
                            miBD.getOrderDAO().delete(p1);
                        }
                    }
                    Intent home=new Intent(CrearPedido.this,MainActivity.class);
                    home.putExtra("Usuario",usuarioLogeado);
                    startActivity(home);
                }else{
                    Toast.makeText(CrearPedido.this, "Los campos son obligatorios", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        makeOrder();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.homeItem:
                Intent home=new Intent(CrearPedido.this,MainActivity.class);
                home.putExtra("Usuario",usuarioLogeado);
                startActivity(home);
                break;
            case R.id.orderItem:
                Intent listaPedidos=new Intent(CrearPedido.this, ListaPedidos.class);
                listaPedidos.putExtra("Usuario",usuarioLogeado);
                startActivity(listaPedidos);
                break;
            case R.id.buyAgainItem:
                Intent buyagain=new Intent(CrearPedido.this,VolverAcomprarActivity.class);
                buyagain.putExtra("Usuario",usuarioLogeado);
                startActivity(buyagain);
                break;
            case R.id.accountItem:
                Intent micuenta=new Intent(CrearPedido.this,MiCuentaActivity.class);
                micuenta.putExtra("Usuario",usuarioLogeado);
                startActivity(micuenta);
                break;
            case R.id.logOut:
                AuthUI.getInstance().signOut(this);
                FirebaseAuth.getInstance().signOut();
                Intent cerrarSession=new Intent(CrearPedido.this,LoginActivity.class);
                startActivity(cerrarSession);
                break;
            case R.id.options:
                Intent options=new Intent(CrearPedido.this,SettingsActivity.class);
                startActivity(options);
                break;
            default:
                return false;
        }
        drawerLayout.closeDrawers();
        return false;
    }
    private String formatDate(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(cal.getTime());
        String formatted = format1.format(cal.getTime());
        return formatted;
    }
    private void makeOrder(){

        for( Pedido p1 : pedidosAux){
            if(!p1.isFinished()){
                OrderProducto aux=new OrderProducto();
                aux.setIdOrder(p1.getId());
                try {
                    aux=(OrderProducto) miBD.getOrderProductsDAO().search(aux);
                    Producto prodAux=new Producto();
                    prodAux.setId(aux.getIdProducto());
                    p1.getProductos().add((Producto) miBD.getProductDAO().search(prodAux));
                    pedidos.add(p1);
                    productos.add((Producto)miBD.getProductDAO().search(prodAux));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        for(int i=0;i<pedidos.size();i++)
            price+=pedidos.get(i).getCantidadPedido()*pedidos.get(i).getProductos().get(0).getPrecio();

        DecimalFormat df = new DecimalFormat("0.00");

        TextView priceTotal=(TextView)findViewById(R.id.totalPriceSummary);
        priceTotal.setText(String.valueOf(df.format(price)));


    }
}