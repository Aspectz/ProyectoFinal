package jogasa.simarro.proyectenadal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.bd.UsuariosBD;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class SignUpActivity extends AppCompatActivity {
    private EditText name,email,firstPassword,secondPassword;
    private Button signUpbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        name=(EditText)findViewById(R.id.nameEditText);
        email=(EditText)findViewById(R.id.emailEditText);
        firstPassword=(EditText)findViewById(R.id.firstPasswordEditText);
        secondPassword=(EditText)findViewById(R.id.secondPasswordEditText);
        signUpbutton=(Button)findViewById(R.id.signUpEditText);


        signUpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String primeraContrasena=firstPassword.getText().toString();
                String segundaContrasena=secondPassword.getText().toString();
                String nombre=name.getText().toString();
                String correo=email.getText().toString();

                if(!nombre.isEmpty() || !correo.isEmpty() ){
                    if(!buscarCorreoRegistrado(correo)){
                        if(Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                            if(!primeraContrasena.isEmpty() || !segundaContrasena.isEmpty()){
                                if(primeraContrasena.compareTo(segundaContrasena)!=0){
                                    Toast.makeText(SignUpActivity.this, "YOUR PASSWORD MUST BE THE SAME", Toast.LENGTH_SHORT).show();
                                }else{
                                    if(!Pattern.compile("[a-zA-Z0-9]{8,24}").matcher(primeraContrasena).matches()){
                                        Toast.makeText(SignUpActivity.this, "MIN 1 CAP, 1 NUM, 1 MIN", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Usuario usuario=new Usuario(nombre,correo,primeraContrasena);
                                        UsuariosBD.getUsuarios().add(usuario);
                                        Intent main=new Intent(SignUpActivity.this,MainActivity.class);
                                        main.putExtra("Usuario",usuario);
                                        startActivity(main);
                                    }
                                }
                            }else{
                                Toast.makeText(SignUpActivity.this, "PASSWORD ARE OBLIGATED", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(SignUpActivity.this, "INVALID EMAIL", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(SignUpActivity.this, "EMAIL ALREADY SIGNED UP", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignUpActivity.this, "NAME OR EMAIL ARE OBLIGATED", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    public boolean buscarCorreoRegistrado(String correo){
        for(Usuario u : UsuariosBD.getUsuarios()){
            if(u.getEmail().compareTo(correo)==0){
                return true;
            }
        }
        return false;
    }
}