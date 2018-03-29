package com.example.user.aplicacio1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Vector;

/**
 * Created by oscar on 07/02/2018.
 */


public class MagatzemPuntuacionsSQLiteRel extends SQLiteOpenHelper implements MagatzemPuntuacions {
    int punts = 0;
    String nom = "";
    long data = 0;

    public MagatzemPuntuacionsSQLiteRel(Context context) {
        super(context, "puntuacions", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuaris ("+"usuari_id INTEGER PRIMARY KEY, "+" nom VARCHAR(50) ,"+" coreu VARCHAR(50))");
        db.execSQL("CREATE TABLE puntuacio ("+"punts_id INTEGER PRIMARY KEY, "+"punts INTEGER, " +"data DATE, "+"usuari INTEGER,"+" FOREIGN KEY (usuari) REFERENCES usuaris(usuari_id)) ");
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("INSERT INTO usuaris VALUES (null, "+ "'" + nom + "', 'daw2omartigreogrio@iesjoanramis.org')");

        db.execSQL("INSERT INTO puntuacions VALUES (null, "+punts + ","+ data +" ,(select usu_id from usuaris where nom = '"+ nom +"'))");

        db.close();
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        Vector<String> result = new Vector<String>();
        SQLiteDatabase db = getReadableDatabase(); //ObtÃ© un cursos que contÃ© totes les files de la consulta.

        Cursor cursos = db.rawQuery("SELECT  punts,nom FROM " + "vpuntuacions, usuaris ORDER BY punts DESC LIMIT " +
                quantitat, null);

        while (cursos.moveToNext()) {
            result.add(cursos.getInt(0) + " " + cursos.getString(1));
        }

        cursos.close();
        db.close();

        return result;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Cuando cambiamos de version de la base de datos se ejecutara esta funcion, cuando se ejecute
        // lo que tenemos que hacer es copiar los datos de la antigua bd a la nueva
        if(oldVersion<newVersion) {
            onCreate(db);

            db = getWritableDatabase();
            //Cursores SQL
            Cursor c = db.rawQuery("Select _id, punts, nom, data from vpuntuacions ",null);
            while (c.moveToNext()){
                guardarPuntuacio(c.getInt(1),c.getString(2),c.getLong(3));
            }
            c.close();
            db.close();
        }

    }

}