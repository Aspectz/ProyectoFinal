package jogasa.simarro.projectefinal.activity;

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
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import jogasa.simarro.projectefinal.fragments.FragmentEditarProducto;
import jogasa.simarro.projectefinal.pojo.Producto;
import jogasa.simarro.projectefinal.pojo.Usuario;
import jogasa.simarro.projectefinal.R;

public class EditarProducto extends AppCompatActivity{


    private Producto productoSeleccionado;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_cuenta);
        ImageButton back=findViewById(R.id.backAddPhoto);
        productoSeleccionado=(Producto)getIntent().getSerializableExtra("Producto");

        if (savedInstanceState == null) {
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new FragmentEditarProducto(productoSeleccionado));
            fragmentTransaction.commit();
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditarProducto.this,HomeActivity.class);
                startActivity(intent);
            }
        });


    }

}