package jogasa.simarro.proyectenadal.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.fragments.FragmentInicio;

import jogasa.simarro.proyectenadal.pojo.Producto;
import jogasa.simarro.proyectenadal.pojo.Tienda;
import jogasa.simarro.proyectenadal.pojo.Usuario;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private Usuario usuarioLogeado;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        usuarioLogeado=(Usuario)getIntent().getSerializableExtra("Usuario");


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



        if(savedInstanceState==null){


            getSupportActionBar().setTitle(R.string.home);
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new FragmentInicio());
            fragmentTransaction.commit();
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.orderItem:
                Intent listaPedidos=new Intent(MainActivity.this, ListaPedidos.class);
                listaPedidos.putExtra("Usuario",usuarioLogeado);
                startActivity(listaPedidos);

                break;
            case R.id.buyAgainItem:
                Intent buyagain=new Intent(MainActivity.this,VolverAcomprarActivity.class);
                buyagain.putExtra("Usuario",usuarioLogeado);
                startActivity(buyagain);
                break;
            case R.id.accountItem:
                Intent micuenta=new Intent(MainActivity.this,MiCuentaActivity.class);
                micuenta.putExtra("Usuario",usuarioLogeado);
                startActivity(micuenta);
                break;
            case R.id.logOut:
                Intent cerrarSession=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(cerrarSession);
            default:
                return false;
        }
        drawerLayout.closeDrawers();
        return false;
    }
}