package jogasa.simarro.projectefinal.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import jogasa.simarro.projectefinal.R;


public class ForgotPasswordActivity extends AppCompatActivity {
    private ImageButton logIn;
    private TextInputLayout emailText;
    private Button recoverButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        logIn = (ImageButton) findViewById(R.id.backAddPhoto);
        emailText = (TextInputLayout) findViewById(R.id.emailText);


        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    if (!user.isEmailVerified()) {
                        Toast.makeText(ForgotPasswordActivity.this, getResources().getString(R.string.verEmailSent), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        };
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logInActivity = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(logInActivity);
            }
        });
        recoverButton = (Button) findViewById(R.id.recoverButton);

        recoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoverPasswd();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    private void recoverPasswd() {
        final String correo = emailText.getEditText().getText().toString();

        if (!correo.isEmpty()) {
            mAuth.sendPasswordResetEmail(correo).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, getResources().getString(R.string.emailSentTo) + correo, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.emailNotValid), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, getResources().getString(R.string.insertEmail), Toast.LENGTH_SHORT).show();
        }
    }
}