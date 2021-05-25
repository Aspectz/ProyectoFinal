package jogasa.simarro.proyectenadal.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.fragments.FragmentAnadirProducto;
import jogasa.simarro.proyectenadal.fragments.FragmentInicio;

import jogasa.simarro.proyectenadal.fragments.FragmentTusProductos;
import jogasa.simarro.proyectenadal.pojo.Estados;
import jogasa.simarro.proyectenadal.pojo.OrderDetails;
import jogasa.simarro.proyectenadal.pojo.Pedido;
import jogasa.simarro.proyectenadal.pojo.Usuario;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView cartCount;
    private ImageView cartIcon;

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
        String notifications=pref.getString("notifications","a");





        if (language.compareTo("esp") == 0) setAppLocale("es");
        if (language.compareTo("eng") == 0) setAppLocale("en");


        setContentView(R.layout.activity_main);

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


            db.collection("Suppliers").document(fb.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_fragment, new FragmentTusProductos());
                            fragmentTransaction.commit();

                            BottomNavigationView botom = (BottomNavigationView) findViewById(R.id.bottomBarMain);
                            botom.setVisibility(View.VISIBLE);
                            botom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                                @Override
                                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                    fragmentTransaction = fragmentManager.beginTransaction();
                                    Fragment f = null;
                                    if (item.getItemId() == R.id.inicio)
                                        fragmentTransaction.replace(R.id.container_fragment, new FragmentTusProductos());
                                    if (item.getItemId() == R.id.anadirProducto)
                                        fragmentTransaction.replace(R.id.container_fragment, new FragmentAnadirProducto());
                                    fragmentTransaction.commit();
                                    return true;
                                }
                            });
                        } else {
                            BottomNavigationView botom = (BottomNavigationView) findViewById(R.id.bottomBarMain);
                            botom.getMenu().clear();
                            botom.inflateMenu(R.menu.menu_cliente);
                            botom.setVisibility(View.VISIBLE);
                            botom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                                @Override
                                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                    fragmentTransaction = fragmentManager.beginTransaction();
                                    Fragment f = null;
                                    if (item.getItemId() == R.id.inicio)
                                        fragmentTransaction.replace(R.id.container_fragment, new FragmentTusProductos());
                                    if (item.getItemId() == R.id.anadirProducto)
                                        fragmentTransaction.replace(R.id.container_fragment, new FragmentAnadirProducto());
                                    fragmentTransaction.commit();
                                    return true;
                                }
                            });

                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_fragment, new FragmentInicio());
                            fragmentTransaction.commit();
                        }
                    }
                }
            });


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
    protected void onResume() {
        super.onResume();
        Log.d("pepe","onresume");
        updateCartCount();

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

        switch (item.getItemId()) {
            case R.id.orderItem:
                Intent listaPedidos = new Intent(HomeActivity.this, ListaPedidos.class);
                startActivity(listaPedidos);

                break;
            case R.id.buyAgainItem:
                Intent buyagain = new Intent(HomeActivity.this, VolverAcomprarActivity.class);
                startActivity(buyagain);
                break;
            case R.id.accountItem:
                Intent micuenta = new Intent(HomeActivity.this, MiCuentaActivity.class);
                startActivity(micuenta);
                break;
            case R.id.logOut:
                AuthUI.getInstance().signOut(this);
                FirebaseAuth.getInstance().signOut();
                Intent cerrarSession = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(cerrarSession);
                break;
            case R.id.options:
                Intent options = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(options);
                break;
            case R.id.aboutUs:
                Intent aboutUs = new Intent(HomeActivity.this, AboutUsActivity.class);
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
        Log.d("pepe","oncreate");
        getMenuInflater().inflate(R.menu.menu_toolbar_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);

        View view=menu.findItem(R.id.shoppingCart).getActionView();
        cartCount=(TextView) view.findViewById(R.id.badge);
        cartIcon=view.findViewById(R.id.cart_icon);

        updateCartCount();


/*
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });*/

        return true;
    }


    private void searchData(String query) {
        Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.shoppingCart) {
            Intent shipping = new Intent(HomeActivity.this, ShippingCart.class);
            startActivity(shipping);
        }
        return true;
    }

    public void updateCartCount(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Task<QuerySnapshot> q =db.collection("Orders").whereEqualTo("idUser",fb.getCurrentUser().getUid()).whereEqualTo("estado", Estados.CARRITO).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot ord : task.getResult()){
                                Pedido p =ord.toObject(Pedido.class);
                                db.collection("OrderDetails").whereEqualTo("idOrder",p.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            int cont=0;
                                            for(QueryDocumentSnapshot ordDet : task.getResult()){
                                                cont++;
                                            }
                                            cartCount.setText(String.valueOf(cont));
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
                Tasks.whenAllComplete(q).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> task) {
                        cartIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent shipping = new Intent(HomeActivity.this, ShippingCart.class);
                                startActivity(shipping);
                            }
                        });
                    }
                });
            }
        });



    }
}