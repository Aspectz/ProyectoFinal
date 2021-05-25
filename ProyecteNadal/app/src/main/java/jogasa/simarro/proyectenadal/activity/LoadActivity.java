package jogasa.simarro.proyectenadal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import jogasa.simarro.proyectenadal.R;


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