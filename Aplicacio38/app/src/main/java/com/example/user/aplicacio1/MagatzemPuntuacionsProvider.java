package com.example.user.aplicacio1;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.Vector;

/**
 * Created by oscar on 09/02/2018.
 */

public class MagatzemPuntuacionsProvider implements MagatzemPuntuacions {
    private Activity activity;

    public MagatzemPuntuacionsProvider(Activity activity){
        this.activity=activity;
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        Uri uri = Uri.parse("content://com.exemple.user.eje11_3/puntuacions");
        ContentValues valors = new ContentValues();
        valors.put("nom",nom);
        valors.put("punts", punts);
        valors.put("data",data);
        try {
            activity.getContentResolver().insert(uri, valors);
        }catch (Exception e){
            Toast.makeText(activity,"Verificar que esta instalada"+"com.exemple.user.eje11_3", Toast.LENGTH_LONG).show();
            Log.e("Aplicacio","Error: "+e.toString(),e);
        }
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        Vector<String> result = new Vector<String>();
        Uri uri = Uri.parse("content://com.exemple.user.eje11_3/puntuacions");
        Cursor cursor = activity.getContentResolver().query(uri,null,null,null,"data DESC");
        if(cursor != null){
            while (cursor.moveToNext()){
                String nom = cursor.getString(cursor.getColumnIndex("nom"));
                int punts = cursor.getInt(cursor.getColumnIndex("punts"));
                result.add(punts+" "+nom);
            }
            cursor.close();
        }

        return result;
    }
}
