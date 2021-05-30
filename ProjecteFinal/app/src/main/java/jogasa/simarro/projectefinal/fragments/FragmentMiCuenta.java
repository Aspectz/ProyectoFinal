package jogasa.simarro.projectefinal.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import jogasa.simarro.projectefinal.R;
import jogasa.simarro.projectefinal.pojo.Usuario;
import jogasa.simarro.projectefinal.pojo.Vendedor;

public class FragmentMiCuenta extends Fragment {

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fb = FirebaseFirestore.getInstance();
    private Task<DocumentSnapshot> user;
    String option = null;


    public FragmentMiCuenta(String opt) {
        this.option = opt;
    }

    public FragmentMiCuenta() {
        this.option = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mi_cuenta, container, false);


        Button saveButton = (Button) view.findViewById(R.id.saveSettings);
        TextView nombre = (TextView) view.findViewById(R.id.nameAccount);
        TextView email = (TextView) view.findViewById(R.id.emailAccount);
        TextView passwd = (TextView) view.findViewById(R.id.passwordAccount);
        TextView newPasswd = (TextView) view.findViewById(R.id.newPasswordAcc);
        String name = getString(R.string.names);

        String emails = getString(R.string.emails);
        user = fb.collection(option).document(fAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (option.equalsIgnoreCase("Users")) {
                        Usuario u = task.getResult().toObject(Usuario.class);
                        nombre.setText(u.getNombre());
                        email.setText(fAuth.getCurrentUser().getEmail());
                    } else if (option.equalsIgnoreCase("Suppliers")) {
                        Vendedor v = task.getResult().toObject(Vendedor.class);
                        nombre.setText(v.getCompanyName());
                        email.setText(fAuth.getCurrentUser().getEmail());
                    }

                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.getText().toString().equalsIgnoreCase(fAuth.getCurrentUser().getEmail()) || !email.getText().toString().isEmpty() && !email.getText().toString().equalsIgnoreCase(fAuth.getCurrentUser().getEmail())) {
                    fAuth.getCurrentUser().updateEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                fb.collection(option).document(fAuth.getCurrentUser().getUid()).update("email", email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Email changed successfully", Snackbar.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Email de verificacion enviado", Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                if(!newPasswd.getText().toString().isEmpty() && newPasswd.getText().toString().length()>6 && !passwd.getText().toString().isEmpty()){

                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email.getText().toString(),passwd.getText().toString());

                    fAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                fAuth.getCurrentUser().updatePassword(newPasswd.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Password changed successfully", Snackbar.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }



                /*fb.collection("Users").document(fAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            task.getResult().toObject(Usuario.class);
                        }
                    }
                });*/
            }
        });


        return view;
    }
}