package jogasa.simarro.proyectenadal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.bd.UsuariosBD;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class ForgotPasswordActivity extends AppCompatActivity {
    private TextView logIn;
    private TextInputLayout emailText;
    private Button recoverButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        logIn=(TextView)findViewById(R.id.logInRecover);
        emailText=(TextInputLayout)findViewById(R.id.emailText);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logInActivity=new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                startActivity(logInActivity);
            }
        });

        recoverButton=(Button)findViewById(R.id.recoverButton);

        recoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchEmail()){
                    Toast.makeText(ForgotPasswordActivity.this, "EMAIL SENDED", Toast.LENGTH_SHORT).show();
                    emailText.getEditText().setText("");
                }else{
                    Toast.makeText(ForgotPasswordActivity.this, "YOUR EMAIL IS NOT CORRECT", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private boolean searchEmail(){
        String correo=emailText.getEditText().getText().toString();
        for(Usuario u : UsuariosBD.getUsuarios()){
            if(u.getEmail().compareTo(correo)==0){
                return true;
            }
        }
        return false;
    }
}