package jogasa.simarro.projectefinal.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

import jogasa.simarro.projectefinal.pojo.Usuario;
import jogasa.simarro.projectefinal.R;

public class LoginActivity extends AppCompatActivity {

    private TextView forgotPassword;
    private EditText email, password;
    private Button logInButton, signUp;
    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore fb = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences pref = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        String language = pref.getString("idioma", "esp");

        if (language.compareTo("esp") == 0) setAppLocale("es");
        if (language.compareTo("eng") == 0) setAppLocale("en");

        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    if (!user.isEmailVerified()) {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.emailNotVerified), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        };
        //GOOGLE

        //UsuariosBD.getUsuarios().add(u);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        signUp = (Button) findViewById(R.id.registerButton);
        logInButton = (Button) findViewById(R.id.loginButton);
        email = (EditText) findViewById(R.id.emailEditText);
        password = (EditText) findViewById(R.id.passwordEditText);


        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(LoginActivity.this, OnBoardActivity.class);
                startActivity(signUp);
                finish();
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent forgotPassword = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(forgotPassword);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setAppLocale(String locale) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration config = res.getConfiguration();
        config.setLocale(new Locale(locale.toLowerCase()));
        res.updateConfiguration(config, dm);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    public void login() {

        String username = email.getText().toString();
        String passwd = password.getText().toString();
        if (!username.isEmpty() && !passwd.isEmpty()) {
            mAuth.signInWithEmailAndPassword(username, passwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        FirebaseUser currentuser = mAuth.getCurrentUser();
                        if (currentuser.isEmailVerified()) {
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.emailNotVerified), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        }
    }
}