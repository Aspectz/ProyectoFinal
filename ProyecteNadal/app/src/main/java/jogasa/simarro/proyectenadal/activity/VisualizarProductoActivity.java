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
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import jogasa.simarro.proyectenadal.R;

import jogasa.simarro.proyectenadal.fragments.FragmentComprar;

import jogasa.simarro.proyectenadal.pojo.Producto;
import jogasa.simarro.proyectenadal.pojo.Usuario;


public class VisualizarProductoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private Usuario usuarioLogeado;
    private Producto productoSeleccionado;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_cuenta);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        usuarioLogeado = (Usuario) getIntent().getSerializableExtra("Usuario");
        productoSeleccionado=(Producto)getIntent().getSerializableExtra("Producto");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);


        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        //Recoger headerLayout
        View headerLayout = navigationView.getHeaderView(0);
        TextView headerText = (TextView) headerLayout.findViewById(R.id.textHeader);

        headerText.setText(getResources().getString(R.string.hello)+usuarioLogeado.getNombre());

       // getSupportActionBar().setTitle(productoSeleccionado.getNombre());

        if (savedInstanceState == null) {
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new FragmentComprar(productoSeleccionado,usuarioLogeado));
            fragmentTransaction.commit();
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.homeItem:
                Intent home = new Intent(VisualizarProductoActivity.this, MainActivity.class);
                home.putExtra("Usuario", usuarioLogeado);
                startActivity(home);
                break;
            case R.id.orderItem:
                Intent listaPedidos=new Intent(VisualizarProductoActivity.this, ListaPedidos.class);
                listaPedidos.putExtra("Usuario",usuarioLogeado);
                startActivity(listaPedidos);

                break;
            case R.id.buyAgainItem:
                Intent buyagain=new Intent(VisualizarProductoActivity.this,VolverAcomprarActivity.class);
                buyagain.putExtra("Usuario",usuarioLogeado);
                startActivity(buyagain);
                break;
            case R.id.accountItem:
                Intent micuenta=new Intent(VisualizarProductoActivity.this,MiCuentaActivity.class);
                micuenta.putExtra("Usuario",usuarioLogeado);
                startActivity(micuenta);
                break;
            case R.id.logOut:
                AuthUI.getInstance().signOut(this);
                FirebaseAuth.getInstance().signOut();
                Intent cerrarSession=new Intent(VisualizarProductoActivity.this,LoginActivity.class);
                startActivity(cerrarSession);
                break;
            default:
                return false;
        }
        drawerLayout.closeDrawers();
        return false;
    }
}