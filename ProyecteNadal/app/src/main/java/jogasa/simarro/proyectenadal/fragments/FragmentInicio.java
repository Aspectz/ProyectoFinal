package jogasa.simarro.proyectenadal.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.text.ParseException;
import java.util.ArrayList;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.activity.VisualizarProductoActivity;
import jogasa.simarro.proyectenadal.adapters.AdapterProductos;
import jogasa.simarro.proyectenadal.pojo.Favorites;
import jogasa.simarro.proyectenadal.pojo.Producto;


public class FragmentInicio extends Fragment implements AdapterView.OnItemClickListener {
    public GridView grid;
    private Spinner orderSpinner;
    private FirebaseAuth fAuth= FirebaseAuth.getInstance();

    private String fav=null;


    public FragmentInicio(String fav){
        this.fav=fav;
    }
    public FragmentInicio(){
        this.fav=null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=  inflater.inflate(R.layout.fragment_inicio,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        grid=(GridView)getView().findViewById(R.id.gridProductos);
        orderSpinner=(Spinner)getView().findViewById(R.id.orderBySpinner);
        grid.setOnItemClickListener(this);

        
    }
    public void mostrarProductos() throws ParseException {

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        ArrayList<Producto> productos=new ArrayList<Producto>();
        ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.orderByArray));
        orderSpinner.setAdapter(spinnerAdapter);
        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).toString().equals(getText(R.string.ascending))){
                    productos.clear();
                    db.collection("Products").orderBy("precio", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(QueryDocumentSnapshot q: task.getResult()){
                                productos.add(q.toObject(Producto.class));
                            }
                            grid.setAdapter(new AdapterProductos(FragmentInicio.this,productos));
                        }
                    });
                }
                else{
                    productos.clear();
                    db.collection("Products").orderBy("precio", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(QueryDocumentSnapshot q: task.getResult()){
                                productos.add(q.toObject(Producto.class));
                            }
                            grid.setAdapter(new AdapterProductos(FragmentInicio.this,productos));
                        }
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                db.collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot q: task.getResult()){
                            productos.add(q.toObject(Producto.class));
                        }
                        grid.setAdapter(new AdapterProductos(FragmentInicio.this,productos));
                    }
                });
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            if(fav!=null)mostrarFavoritos();
            else mostrarProductos();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private void mostrarFavoritos() {

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        ArrayList<Producto> productos=new ArrayList<Producto>();
        db.collection("Favorites").whereEqualTo("idUser",fAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot fv : task.getResult()){
                        if(fv.exists()){
                            db.collection("Products").document(fv.toObject(Favorites.class).getIdProduct()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        productos.add(task.getResult().toObject(Producto.class));
                                    }
                                    grid.setAdapter(new AdapterProductos(FragmentInicio.this,productos));
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Producto seleccionado=(Producto)grid.getAdapter().getItem(position);
        //Recoge el producto incluso con firestore
        Intent visualizar=new Intent(getActivity(), VisualizarProductoActivity.class);
        visualizar.putExtra("Producto",seleccionado);

        startActivity(visualizar);
    }
    private class AdapterProducts extends RecyclerView.ViewHolder  {
        public AdapterProducts(@NonNull View itemView) {
            super(itemView);
        }
    }
}