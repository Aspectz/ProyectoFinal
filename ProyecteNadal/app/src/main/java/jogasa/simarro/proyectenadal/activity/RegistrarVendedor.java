package jogasa.simarro.proyectenadal.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.fragments.FragmentAnadirProducto;
import jogasa.simarro.proyectenadal.fragments.FragmentInicio;
import jogasa.simarro.proyectenadal.fragments.FragmentRegistrarVendedor;
import jogasa.simarro.proyectenadal.fragments.FragmentTusProductos;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class RegistrarVendedor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private Usuario usuarioLogeado = new Usuario();

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FirebaseAuth fb = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        String language = pref.getString("idioma", "esp");

        if (language.compareTo("esp") == 0) setAppLocale("es");
        if (language.compareTo("eng") == 0) setAppLocale("en");


        setContentView(R.layout.activity_registrar_vendedor);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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

        db.collection("Users").document(fb.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        usuarioLogeado = doc.toObject(Usuario.class);
                        headerText.setText(getResources().getString(R.string.hello) + usuarioLogeado.getNombre());
                    }
                }
            }
        });

        if (savedInstanceState == null) {
            getSupportActionBar().setTitle(R.string.home);
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new FragmentRegistrarVendedor());
            fragmentTransaction.commit();
        }

    }
    private void setAppLocale(String locale) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration config = res.getConfiguration();

        config.setLocale(new Locale(locale.toLowerCase()));
        res.updateConfiguration(config, dm);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.orderItem:
                Intent listaPedidos = new Intent(RegistrarVendedor.this, ListaPedidos.class);
                startActivity(listaPedidos);
                break;
            case R.id.buyAgainItem:
                Intent buyagain = new Intent(RegistrarVendedor.this, VolverAcomprarActivity.class);
                startActivity(buyagain);
                break;
            case R.id.accountItem:
                Intent micuenta = new Intent(RegistrarVendedor.this, MiCuentaActivity.class);
                startActivity(micuenta);
                break;
            case R.id.logOut:
                AuthUI.getInstance().signOut(this);
                FirebaseAuth.getInstance().signOut();
                Intent cerrarSession = new Intent(RegistrarVendedor.this, LoginActivity.class);
                startActivity(cerrarSession);
                break;
            case R.id.options:
                Intent options = new Intent(RegistrarVendedor.this, SettingsActivity.class);
                startActivity(options);
                break;
            case R.id.aboutUs:
                Intent aboutUs = new Intent(RegistrarVendedor.this, AboutUsActivity.class);
                startActivity(aboutUs);
                break;
            default:
                return false;
        }
        drawerLayout.closeDrawers();
        return false;
    }

}