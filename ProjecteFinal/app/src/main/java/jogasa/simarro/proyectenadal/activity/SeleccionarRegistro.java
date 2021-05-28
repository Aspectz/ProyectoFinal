package jogasa.simarro.proyectenadal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import jogasa.simarro.proyectenadal.R;

public class SeleccionarRegistro extends AppCompatActivity {

    private Button seller,client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_registro);

        seller=(Button)findViewById(R.id.registrarVendedor);
        client=(Button)findViewById(R.id.registrarCliente);


        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SeleccionarRegistro.this,SignUpActivity.class);
                intent.putExtra("Option","seller");
                startActivity(intent);
            }
        });
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SeleccionarRegistro.this,SignUpActivity.class);
                intent.putExtra("Option","client");
                startActivity(intent);
            }
        });






    }
}