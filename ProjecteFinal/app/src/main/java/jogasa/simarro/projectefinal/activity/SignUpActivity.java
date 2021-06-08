package jogasa.simarro.projectefinal.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import jogasa.simarro.projectefinal.pojo.Usuario;
import jogasa.simarro.projectefinal.pojo.Vendedor;
import jogasa.simarro.projectefinal.R;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText name, email, firstPassword, secondPassword;
    private Button signUpbutton;
    private String option;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private TextInputLayout emailInput, nameInput, firstPasswordInput, secondPasswordInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        option = getIntent().getStringExtra("Option");


        emailInput = (TextInputLayout) findViewById(R.id.emailInput);
        nameInput = (TextInputLayout) findViewById(R.id.nameInput);
        firstPasswordInput = (TextInputLayout) findViewById(R.id.firstPasswordInput);
        secondPasswordInput = (TextInputLayout) findViewById(R.id.secondPasswordInput);

        name = (TextInputEditText) findViewById(R.id.nameEditText);
        email = (TextInputEditText) findViewById(R.id.emailEditText);
        firstPassword = (TextInputEditText) findViewById(R.id.firstPasswordEditText);
        secondPassword = (TextInputEditText) findViewById(R.id.secondPasswordEditText);
        signUpbutton = (Button) findViewById(R.id.signUpEditText);


        if (option.equalsIgnoreCase("seller")) {
            nameInput.setHint(getResources().getString(R.string.companyName));
        }

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

        if (!validName() | !validFirstPassword() | !validSecondPassword()) {
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

    private boolean validName() {
        if (name.getText().toString().isEmpty()) {
            nameInput.setError(getResources().getString(R.string.nameField));
            return false;
        } else {
            nameInput.setError(null);
            return true;
        }
    }

    private boolean validSecondPassword() {
        if (firstPassword.getText().toString().compareTo(secondPassword.getText().toString()) != 0) {
            secondPasswordInput.setError(getResources().getString(R.string.samePassword));
            return false;
        } else {
            secondPasswordInput.setError(null);
            return true;
        }
    }

    private boolean validFirstPassword() {
        if (firstPassword.getText().toString().length() < 6) {
            firstPasswordInput.setError(getResources().getString(R.string.minimPassword));
            return false;
        } else {
            firstPasswordInput.setError(null);
            return true;
        }
    }

}