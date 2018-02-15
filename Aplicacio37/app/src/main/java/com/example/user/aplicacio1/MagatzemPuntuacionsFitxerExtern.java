package com.example.user.aplicacio1;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Created by cicles on 26/01/2018.
 */


public class MagatzemPuntuacionsFitxerExtern implements MagatzemPuntuacions {
    private static String FITXER = Environment.getExternalStorageDirectory() + "/puntuacions.txt";
    private Context context;

    public MagatzemPuntuacionsFitxerExtern(Context context){
        this.context = context;
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        try{
            FileOutputStream f = new FileOutputStream(FITXER);
            String text = punts + " " + nom + "\n";
            f.write(text.getBytes());
            f.close();
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(),e);
        }
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        Vector<String> result = new Vector<String>();
        try{
            FileInputStream f = context.openFileInput(FITXER);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(f));
            int n = 0;
            String linea;
            do {
                linea = entrada.readLine();
                if(linea != null){
                    result.add(linea);
                    n++;
                }
            }while (n < quantitat && linea!= null);
            f.close();
        }catch (Exception e){
            Log.e("Asteroides", e.getMessage(), e);
        }
        return result;
    }
}
