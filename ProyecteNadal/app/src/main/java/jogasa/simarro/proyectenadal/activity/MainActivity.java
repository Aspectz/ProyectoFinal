package jogasa.simarro.proyectenadal.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Locale;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.fragments.FragmentInicio;

import jogasa.simarro.proyectenadal.fragments.FragmentNoticias;
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

        SharedPreferences pref =getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        String language=pref.getString("idioma","esp");

        if(language.compareTo("esp")==0)  setAppLocale("es");
        if(language.compareTo("eng")==0)  setAppLocale("en");


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

        headerText.setText(getResources().getString(R.string.hello)+usuarioLogeado.getNombre());


        if(savedInstanceState==null){
            getSupportActionBar().setTitle(R.string.home);
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new FragmentInicio());
            fragmentTransaction.commit();

            BottomNavigationView botom=(BottomNavigationView)findViewById(R.id.bottomBarMain);
            botom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    fragmentTransaction=fragmentManager.beginTransaction();
                    Fragment f=null;


                    if(item.getItemId()==R.id.inicio){
                        fragmentTransaction.replace(R.id.container_fragment,new FragmentInicio());
                    }
                    if(item.getItemId()==R.id.noticias){
                        fragmentTransaction.replace(R.id.container_fragment,new FragmentNoticias());
                    }
                    fragmentTransaction.commit();
                    return true;
                }
            });

        }

    }

    private void setAppLocale(String locale) {
        Resources res=getResources();
        DisplayMetrics dm=res.getDisplayMetrics();
        Configuration config=res.getConfiguration();

        config.setLocale(new Locale(locale.toLowerCase()));
        res.updateConfiguration(config,dm);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
                AuthUI.getInstance().signOut(this);
                FirebaseAuth.getInstance().signOut();
                Intent cerrarSession=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(cerrarSession);
                break;
            case R.id.options:
                Intent options=new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(options);
                break;
            case R.id.aboutUs:
                Intent aboutUs=new Intent(MainActivity.this,AboutUsActivity.class);
                aboutUs.putExtra("Usuario",usuarioLogeado);
                startActivity(aboutUs);
                break;
            default:
                return false;
        }
        drawerLayout.closeDrawers();
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.shoppingCart){
            Intent shipping=new Intent(MainActivity.this,ShippingCart.class);
            shipping.putExtra("Usuario",usuarioLogeado);
            startActivity(shipping);
        }

        return true;
    }
}