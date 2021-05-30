package jogasa.simarro.projectefinal.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import jogasa.simarro.projectefinal.fragments.FragmentSettings;

public class SettingsActivity extends PreferenceActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){

            SharedPreferences pref;
            pref=getSharedPreferences("preferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor edt=pref.edit();

            fragmentManager=getFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(android.R.id.content,new FragmentSettings());
            fragmentTransaction.commit();
        }

    }
}