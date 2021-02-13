package jogasa.simarro.proyectenadal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.pojo.Producto;
import jogasa.simarro.proyectenadal.pojo.Tienda;


public class LoadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

      /*  Producto p1=new Producto("Naranja","Clementina",3,R.drawable.narajna,10,true);
        Producto p2=new Producto("Limon","Limon",5,R.drawable.limon,4,false);
        Producto p3=new Producto("Aguacate","Aguacate",0.87f,R.drawable.aguacate,3,true);
        Producto p4=new Producto("Fresa","Fresa",1.2f,R.drawable.fresa,12,false);
        Producto p5=new Producto("Fresa2","Fresa",1.2f,R.drawable.fresa,12,false);
        Producto p6=new Producto("Fresa1","Fresa",1.2f,R.drawable.fresa,12,false);


        Tienda.anadirProducto(p1);
        Tienda.anadirProducto(p2);
        Tienda.anadirProducto(p3);
        Tienda.anadirProducto(p4);
        Tienda.anadirProducto(p5);
        Tienda.anadirProducto(p6);*/





        delay();
    }

    @Override
    protected void onStart() {
        super.onStart();
       delay();

    }

    public void delay(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent login=new Intent(LoadActivity.this,LoginActivity.class);
                startActivity(login);
            }
        },1500);


    }
}