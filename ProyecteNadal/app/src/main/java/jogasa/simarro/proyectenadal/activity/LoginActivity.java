package jogasa.simarro.proyectenadal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.bd.UsuariosBD;
import jogasa.simarro.proyectenadal.pojo.Producto;
import jogasa.simarro.proyectenadal.pojo.Tienda;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class LoginActivity extends AppCompatActivity {

    private TextView forgotPassword,signUp;
    private EditText email,password;
    private Button logInButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Usuario u=new Usuario("juan","juan","1234");
        UsuariosBD.getUsuarios().add(u);
        forgotPassword=(TextView)findViewById(R.id.forgotPassword);
        signUp=(TextView)findViewById(R.id.registerButton);
        logInButton=(Button)findViewById(R.id.loginButton);
        email=(EditText)findViewById(R.id.emailEditText);
        password=(EditText)findViewById(R.id.passwordEditText);


        forgotPassword.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent forgotPassword=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(forgotPassword);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(signUp);

            }
        });

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo=email.getText().toString();
                String contrasena=password.getText().toString();
                if(UsuariosBD.getUsuarios()!=null){
                    for(Usuario u : UsuariosBD.getUsuarios()){
                        if(u.getEmail().compareTo(correo)==0 && u.getContraseña().compareTo(contrasena)==0){
                            Intent main=new Intent(LoginActivity.this,MainActivity.class);
                            main.putExtra("Usuario",u);
                            startActivity(main);
                            return;
                        }
                    }
                    Toast.makeText(LoginActivity.this, "Incorrecto", Toast.LENGTH_SHORT).show();
                }


            }
        });





    }
}