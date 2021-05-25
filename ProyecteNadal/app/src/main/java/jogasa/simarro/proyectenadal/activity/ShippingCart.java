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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.fragments.FragmentShippingCart;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class ShippingCart extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {


    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_cart);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        navigationView=(NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        //Recoger headerLayout
        View headerLayout=navigationView.getHeaderView(0);
        TextView headerText = (TextView) headerLayout.findViewById(R.id.textHeader);
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

        if(savedInstanceState==null){
            getSupportActionBar().setTitle("My Cart");
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new FragmentShippingCart());
            fragmentTransaction.commit();
        }
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){

            case R.id.homeItem:
                Intent home=new Intent(ShippingCart.this, HomeActivity.class);
                startActivity(home);
                break;

            case R.id.orderItem:
                Intent listaPedidos=new Intent(ShippingCart.this, ListaPedidos.class);
                startActivity(listaPedidos);

                break;
            case R.id.buyAgainItem:
                Intent buyagain=new Intent(ShippingCart.this,VolverAcomprarActivity.class);
                startActivity(buyagain);
                break;
            case R.id.accountItem:
                Intent micuenta=new Intent(ShippingCart.this,MiCuentaActivity.class);
                startActivity(micuenta);
                break;
            case R.id.logOut:
                AuthUI.getInstance().signOut(this);
                FirebaseAuth.getInstance().signOut();
                Intent cerrarSession=new Intent(ShippingCart.this,LoginActivity.class);
                startActivity(cerrarSession);
                break;
            case R.id.aboutUs:
                Intent aboutUs=new Intent(ShippingCart.this,AboutUsActivity.class);
                startActivity(aboutUs);
                break;
            case R.id.anadirProducto:
                //Intent anadirProducto = new Intent(ShippingCart.this, AnadirProducto.class);
                //startActivity(anadirProducto);
                break;
            default:
                return false;
        }
        drawerLayout.closeDrawers();
        return false;
    }
}