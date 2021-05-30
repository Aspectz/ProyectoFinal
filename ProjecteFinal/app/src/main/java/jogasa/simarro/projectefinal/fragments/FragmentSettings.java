package jogasa.simarro.projectefinal.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

import jogasa.simarro.projectefinal.R;

public class FragmentSettings extends PreferenceFragment {


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.options);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        FirebaseAuth fb=FirebaseAuth.getInstance();
        SharedPreferences pref = getActivity().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();


        ListPreference idioma = (ListPreference) findPreference("idioma");
        /*SwitchPreference notification=(SwitchPreference)findPreference("notifications");

        notification.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Map<String,Object> noti=new HashMap<>();
                if(newValue.toString().equalsIgnoreCase("true")){
                    String token= FirebaseInstanceId.getInstance().getToken();
                    noti.put("token",token);
                    db.collection("Users").document(fb.getCurrentUser().getUid()).set(noti, SetOptions.merge());
                }
                else if(newValue.toString().equalsIgnoreCase("false")){
                    noti.put("token",null);
                    db.collection("Users").document(fb.getCurrentUser().getUid()).set(noti, SetOptions.merge());
                }



                editor.putString("notifications", newValue.toString());
                editor.commit();
                return true;
            }
        });*/


        idioma.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                switch (newValue.toString()) {
                    case "ESP":
                        //  Toast.makeText(getActivity(), "Espanol", Toast.LENGTH_SHORT).show();
                        Locale espanol = new Locale("es", "ES");
                        Locale.setDefault(espanol);

                        Configuration config_es = new Configuration();
                        config_es.locale = espanol;
                        getResources().updateConfiguration(config_es, getResources().getDisplayMetrics());

                        editor.putString("idioma", "esp");
                        editor.commit();

                        //Reiniciar activity para ver cambios
                        getActivity().recreate();
                        break;
                    case "ENG":
                        Locale ingles = new Locale("en", "EN");
                        Locale.setDefault(ingles);

                        Configuration config_en = new Configuration();
                        config_en.locale = ingles;
                        getResources().updateConfiguration(config_en, getResources().getDisplayMetrics());

                        editor.putString("idioma", "eng");
                        editor.commit();

                        //Reiniciar activity para ver cambios
                        getActivity().recreate();

                        break;
                }
                return false;
            }
        });
    }
}