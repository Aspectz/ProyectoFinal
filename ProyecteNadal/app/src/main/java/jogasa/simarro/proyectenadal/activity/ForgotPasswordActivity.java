package jogasa.simarro.proyectenadal.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class ForgotPasswordActivity extends AppCompatActivity {
    private TextView logIn;
    private TextInputLayout emailText;
    private Button recoverButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        logIn=(TextView)findViewById(R.id.logInRecover);
        emailText=(TextInputLayout)findViewById(R.id.emailText);


        mAuth = FirebaseAuth.getInstance();
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                    if(!user.isEmailVerified()){
                        Toast.makeText(ForgotPasswordActivity.this, "Correo no verificado", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


            }
        };







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

                recoverPasswd();

                /*if(searchEmail()){
                    Toast.makeText(ForgotPasswordActivity.this, "EMAIL SENDED", Toast.LENGTH_SHORT).show();
                    emailText.getEditText().setText("");
                }else{
                    Toast.makeText(ForgotPasswordActivity.this, "YOUR EMAIL IS NOT CORRECT", Toast.LENGTH_SHORT).show();
                }*/


            }
        });

    }
    private boolean searchEmail(){
        String correo=emailText.getEditText().getText().toString();
        /*for(Usuario u : UsuariosBD.getUsuarios()){
            if(u.getEmail().compareTo(correo)==0){
                return true;
            }
        }*/
        return false;
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null){
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(authStateListener);
    }
    private void recoverPasswd(){
        final String correo=emailText.getEditText().getText().toString();

        if(!correo.isEmpty()){
            mAuth.sendPasswordResetEmail(correo).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(this, "Correo enviado a "+correo, Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(this, "Correo no valido", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "Introduce un correo", Toast.LENGTH_SHORT).show();
        }
    }
}