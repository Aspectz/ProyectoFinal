package jogasa.simarro.projectefinal.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import jogasa.simarro.projectefinal.pojo.Usuario;
import jogasa.simarro.projectefinal.pojo.Vendedor;
import jogasa.simarro.projectefinal.R;

public class    SignUpActivity extends AppCompatActivity {
    private EditText name, email, firstPassword, secondPassword;
    private Button signUpbutton;
    private String option;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        option = getIntent().getStringExtra("Option");


        name = (EditText) findViewById(R.id.nameEditText);
        email = (EditText) findViewById(R.id.emailEditText);
        firstPassword = (EditText) findViewById(R.id.firstPasswordEditText);
        secondPassword = (EditText) findViewById(R.id.secondPasswordEditText);
        signUpbutton = (Button) findViewById(R.id.signUpEditText);


        if (option.equalsIgnoreCase("seller")) name.setHint("Company's name");

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };
        signUpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    private void signUp() {
        String username = name.getText().toString();
        String correo = email.getText().toString();
        String passwd = firstPassword.getText().toString();
        String passwd2 = secondPassword.getText().toString();


        if (passwd.compareTo(passwd2) != 0) {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.samePassword), Toast.LENGTH_SHORT).show();
            return;
        }
        if (passwd.length() < 6) {
            Toast.makeText(this, getResources().getString(R.string.minimPassword), Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(correo, passwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser user = mAuth.getCurrentUser();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    user.sendEmailVerification();
                    Toast.makeText(SignUpActivity.this, getString(R.string.verEmailSent), Toast.LENGTH_SHORT).show();
                    if (option.equalsIgnoreCase("seller")) {
                        Vendedor vendedor = new Vendedor(user.getUid(), username, correo);
                        db.collection("Suppliers").document(user.getUid()).set(vendedor);
                    } else {
                        Usuario usuario = new Usuario(user.getUid(), username, correo);
                        db.collection("Users").document(user.getUid()).set(usuario);
                    }
                    Intent main = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(main);
                }
            }
        });
    }
}