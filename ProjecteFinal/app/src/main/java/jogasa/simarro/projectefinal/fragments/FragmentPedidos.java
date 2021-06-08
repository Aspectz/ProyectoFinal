package jogasa.simarro.projectefinal.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import jogasa.simarro.projectefinal.R;
import jogasa.simarro.projectefinal.adapters.AdaptadorPedidos;
import jogasa.simarro.projectefinal.pojo.Estados;
import jogasa.simarro.projectefinal.pojo.OrderDetails;
import jogasa.simarro.projectefinal.pojo.Pedido;

public class FragmentPedidos extends Fragment {
    public ListView listaPedidos;
    private FirebaseFirestore fb = FirebaseFirestore.getInstance();
    private FirebaseAuth fauth = FirebaseAuth.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listaPedidos = (ListView) getView().findViewById(R.id.listaPedidos);

    }

    public void mostrarProductos() {

        ArrayList<OrderDetails> pedidos = new ArrayList<OrderDetails>();

        fb.collection("Orders").whereEqualTo("idUser", fauth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot pe : task.getResult()) {
                        if (pe.toObject(Pedido.class).getEstado() != Estados.CARRITO) {
                            fb.collection("OrderDetails").whereEqualTo("idOrder", pe.toObject(Pedido.class).getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot pd : task.getResult()) {
                                            if(pd.exists()){
                                                pedidos.add(pd.toObject(OrderDetails.class));
                                            }
                                        }
                                        if(getActivity()!=null){
                                            listaPedidos.setAdapter(new AdaptadorPedidos(getActivity(), pedidos));
                                        }


                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mostrarProductos();
    }


}