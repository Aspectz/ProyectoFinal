package jogasa.simarro.proyectenadal.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.util.Calendar;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.fragments.FragmentInicio;
import jogasa.simarro.proyectenadal.fragments.FragmentMiCuenta;
import jogasa.simarro.proyectenadal.fragments.FragmentPedidos;
import jogasa.simarro.proyectenadal.fragments.FragmentVolverComprar;
import jogasa.simarro.proyectenadal.pojo.Pedido;
import jogasa.simarro.proyectenadal.pojo.Producto;
import jogasa.simarro.proyectenadal.pojo.Tienda;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class CrearPedido extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private Usuario usuarioLogeado;
    private Producto productoSeleccionado;

    private EditText nombreDestinatario, metodoFacturacion,direccionEnvio;


    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_pedido);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        usuarioLogeado=(Usuario)getIntent().getSerializableExtra("Usuario");
        productoSeleccionado=(Producto)getIntent().getSerializableExtra("Producto");

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

        headerText.setText("Hello, "+usuarioLogeado.getNombre());

        getSupportActionBar().setTitle("Order");

        //PEDIDOS

        TextView nombreProducto=(TextView)findViewById(R.id.nombreProducto);
        TextView precio=(TextView)findViewById(R.id.precioProducto);
        TextView descripcion=(TextView)findViewById(R.id.descripcionProducto);

        nombreProducto.setText(productoSeleccionado.getNombre());
        precio.setText(String.valueOf(productoSeleccionado.getPrecio()));
        descripcion.setText(productoSeleccionado.getDescripcion());

        nombreDestinatario=(EditText)findViewById(R.id.nombreDestinatario);
        metodoFacturacion=(EditText)findViewById(R.id.metodoFacturacion);
        direccionEnvio=(EditText)findViewById(R.id.direccionEnvio);


        Button btnComprar=(Button)findViewById(R.id.btnComprar);
        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nombreDestinatario.getText().toString().isEmpty() && !metodoFacturacion.getText().toString().isEmpty() && !direccionEnvio.getText().toString().isEmpty()){
                    Pedido pedido=new Pedido(nombreDestinatario.getText().toString(),metodoFacturacion.getText().toString(),direccionEnvio.getText().toString(), Calendar.getInstance(),productoSeleccionado.getPrecio(),productoSeleccionado);
                    usuarioLogeado.anadirPedido(pedido);
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
            default:
                return false;
        }
        drawerLayout.closeDrawers();
        return false;
    }
}