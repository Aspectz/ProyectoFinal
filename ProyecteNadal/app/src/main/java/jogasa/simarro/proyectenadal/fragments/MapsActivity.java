package jogasa.simarro.proyectenadal.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;

import java.util.Locale;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.adapters.AdapterProductos;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;


    Address address;


    private LatLng madrid=new LatLng(40.416775, -3.703790);
    private LatLng barcelona=new LatLng(41.38879,2.15899);
    private LatLng valencia=new LatLng(39.46975, -0.37739);
    private LatLng main=new LatLng(39.05004098613268, -0.4591414886868851);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=  inflater.inflate(R.layout.activity_maps,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // Create a LatLngBounds that includes the city of Adelaide in Australia.
        LatLngBounds spainBounds = new LatLngBounds(
                new LatLng(36.42333674005244, -7.8673196535419905), // SW bounds
                new LatLng(42.39298011424012, 2.297264728741342)  // NE bounds
        );
        // Constrain the camera target to the Adelaide bounds.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(spainBounds.getCenter(),5));
        // Add a marker in Sydney and move the camera
        setMarker(madrid,"FruitApp","Madrid");
        setMarker(barcelona,"FruitApp","Barcelona");
        setMarker(valencia,"FruitApp","Valencia");
        setMarker(main,"FruitApp","Rafelguaraf");
    }


    private void setMarker(LatLng position,String titulo, String info){
        Marker myMarker=mMap.addMarker(new MarkerOptions()
                .position(position).title(titulo).snippet(info)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }
}