package jogasa.simarro.proyectenadal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.util.Locale;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.pojo.Producto;
import jogasa.simarro.proyectenadal.pojo.Tienda;


public class LoadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
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