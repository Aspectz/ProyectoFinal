package jogasa.simarro.proyectenadal.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.bd.UsuariosBD;
import jogasa.simarro.proyectenadal.pojo.Producto;
import jogasa.simarro.proyectenadal.pojo.Tienda;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class LoginActivity extends AppCompatActivity {

    private TextView forgotPassword;
    private EditText email,password;
    private Button logInButton,signUp;
    private GoogleSignInClient googleSignInClient;
    private SignInButton signInButton;
    public static final int RC_SIGN_IN = 123;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        mAuth = FirebaseAuth.getInstance();


        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();

                if(user!=null){
                    if(!user.isEmailVerified()){
                        Toast.makeText(LoginActivity.this, "Correo no verificado", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


            }
        };

        //GOOGLE
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton = (SignInButton) findViewById(R.id.google_signIn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });


        Usuario u=new Usuario("juan","juan","1234");
        UsuariosBD.getUsuarios().add(u);
        forgotPassword=(TextView)findViewById(R.id.forgotPassword);
        signUp=(Button)findViewById(R.id.registerButton);
        logInButton=(Button)findViewById(R.id.loginButton);
        email=(EditText)findViewById(R.id.emailEditText);
        password=(EditText)findViewById(R.id.passwordEditText);


        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(signUp);

            }
        });



        /*

        email.setText("juan");
        password.setText("1234");

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
        });*/
    }
    private void signInGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            }catch(ApiException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }
    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(authStateListener);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            FirebaseUser user = mAuth.getCurrentUser();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            Usuario u = new Usuario();
            u.setNombre(user.getDisplayName());
            u.setEmail(user.getEmail());
            intent.putExtra("Usuario", u);
            startActivity(intent);
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            Usuario u = new Usuario();
                            u.setNombre(user.getDisplayName());
                            u.setEmail(user.getEmail());
                            intent.putExtra("Usuario", u);
                            Log.d("usuario", u.toString());
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null){
            mAuth.removeAuthStateListener(authStateListener);
        }
    }
    public void login(){
        String username=email.getText().toString();
        String passwd=password.getText().toString();
        mAuth.signInWithEmailAndPassword(username,passwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "ALLAHU", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    FirebaseUser currentuser=mAuth.getCurrentUser();
                    Usuario u = new Usuario();
                    u.setNombre(currentuser.getDisplayName());
                    u.setEmail(currentuser.getEmail());
                    intent.putExtra("Usuario", u);
                    startActivity(intent);
                }
            }
        });
    }
}