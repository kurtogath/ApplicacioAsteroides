package com.example.user.aplicacio1;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Vector;

/**
 * Created by cicles on 25/01/2018.
 */

public class MagatzemPuntuacionsPregerencies implements MagatzemPuntuacions {

    //Aquest  valor servira per donom al fixter xml
    private static String PREFERENCIES = "puntuacions";
    private Context context;

    public MagatzemPuntuacionsPregerencies(Context context) {
        this.context = context;
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        SharedPreferences preferencies = context.getSharedPreferences(PREFERENCIES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencies.edit();
        for (int i = 9; i >= 1; i--) {
            editor.putString("puntuacio" + i,
                    preferencies.getString("puntuacio" + (i - 1), ""));
            editor.putString("puntuacio0", punts + " " + nom);
        }

        editor.commit();
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        Vector<String> result = new Vector<String>();
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCIES, Context.MODE_PRIVATE);

        for (int i = 0; i <= 9; i++) {
            String s = preferences.getString("puntuacio" + i, "");
            if (!s.isEmpty()) {
                result.add(s);
            }
        }
        return result;
    }
}
