package com.example.user.aplicacio1;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

/**
 * Created by oscar on 03/11/2017.
 */

public class PrefereinciesFragment extends PreferenceFragment {
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencies);

        //Valor por defecto
        final EditTextPreference fragmentos = (EditTextPreference)findPreference(getResources().getString(R.string.pa3_key));
        fragmentos.setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference preference, Object newValue){
                        int valor;
                        try {
                            valor=Integer.parseInt((String)newValue);
                        }catch (Exception e) {
                            Toast.makeText(getActivity(),"Ha de ser un numero",Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        if (valor>=0 && valor<=9) {
                            fragmentos.setSummary("En cuantos trozos se divide un asteroide (" + valor + ")");
                            return true;
                        }else {
                            Toast.makeText(getActivity(),"Maximo de fragmentos 9",Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                });
    }
}
