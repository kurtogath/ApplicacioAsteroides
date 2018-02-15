package com.example.user.aplicacio1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Vector;

/**
 * Created by cicles on 02/02/2018.
 */

public class MagatzemPuntuacionsSQLite extends SQLiteOpenHelper implements MagatzemPuntuacions {
    public MagatzemPuntuacionsSQLite(Context context){
        super(context,"puntuacions", null,1);
    }

    //Aquest mètode nomes es cridara una vegada qyan el sistema detecti que la base de dades encara no esta creada
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Aqui s'han de crear totes les tayulaes de la BD, i inicialitzar les dades si es necessari.
        //CREATE TABLE nom_taula(nom_columna tipus [atributs], ....)
        db = getReadableDatabase();
        db.execSQL("CREATE TABLE vpuntuacions ("+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+  " punts INTEGER, nom VARCHAR(50), data LONG)");
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //En el cas d'una nova versio hauriem d'actualitzar les taules
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        //Obte una referencia a la nostra BD en mode L/E
        SQLiteDatabase db = getReadableDatabase();
        //Sentenca SQL que afegeix una fila a la taula
        //INSERT INTO nom_taula VALUES (valor1, valor2, ....)
        //Els valors cadena van entre cometes
        db.execSQL("INSERT INTO puntuacio VALUES (null, "+punts+","+" '"+nom+"' , "+data+")");
        db.close();
}

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        Vector<String> result = new Vector<String>();
        //Obte una referencia a la nostra BD en mode lectura
        SQLiteDatabase db = getReadableDatabase();
        //Obte un cursor que conte totes les files de la consulta
        //SELECT columna1, ... FROM nom_taula ORDER BY columna][mode] LIMIT numero
        Cursor cursor = db.rawQuery("SELECT punts, nom FROM "+"puntuacio ORDER BY  punts DESC LIMIT" +quantitat,null);
        while (cursor.moveToNext()){
            result.add(cursor.getInt(0)+" " +cursor.getString(1));
        }
        cursor.close();
        db.close();
        return result;
    }
}


/**
    public MagatzemPuntuacionsSQLite(Context context){
        super(context,"puntuacions", null,1);
    }

    //Aquest mètode nomes es cridara una vegada qyan el sistema detecti que la base de dades encara no esta creada
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Aqui s'han de crear totes les tayulaes de la BD, i inicialitzar les dades si es necessari.
        //CREATE TABLE nom_taula(nom_columna tipus [atributs], ....)
        //db.execSQL("CREATE TABLE vpuntuacions ("+ "_id INTEGER PRIMARY " + " KEY AUTOINCREMENT,"+  " punts INTEGER, nom TEXT, data LONG)");
        db.execSQL("CREATE TABLE usuaris ("+"usuari_id INTEGER PRIMARY KEY, "+" nom VARCHAR(50) ,"+" coreu VARCHAR(50))");
        db.execSQL("CREATE TABLE puntuacio ("+"punts_id INTEGER PRIMARY KEY, "+"punts INTEGER, " +"data DATE, "+"usuari INTEGER,"+" FOREIGN KEY (usuari) REFERENCES usuaris(usuari_id)) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //En el cas d'una nova versio hauriem d'actualitzar les taules
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        //Obte una referenci a la nostre BD en mode I/E
        SQLiteDatabase db = getWritableDatabase();

        //Sentencia SQL afegeix una fila a la taula
        //INSERT INTO nom_taula VALUES (valor1, valor2,...)
        ///Els valors cadena van entre cometes
        db.execSQL("INSERT INTO usuaris VALUES (null, "+ "'" + nom + "', 'daw2omartigreogrio@iesjoanramis.org')");

        db.execSQL("INSERT INTO vpuntuacions VALUES (null, "+punts + ","+ data +" ,(select usu_id from usuaris where nom = '"+ nom +"'))");

        db.close();
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        Vector<String> result = new Vector<String>();
        //Obte una referencia a la nostra BD en mode Lectura
        SQLiteDatabase db = getReadableDatabase();
        //Obte un cursor que conte totes les files de la consulta
        //SELECT columna1, ... FROM nom_taula OREDER BY columna [mode]
        Cursor cursor = db.rawQuery("select punts, nom FROM " + "vpuntuacions, usuaris where usuari = usu_id ORDER BY punts DESC LIMIT " + quantitat, null);
        while (cursor.moveToNext()){
            result.add(cursor.getInt(0) + " " + cursor.getString(1));
        }
        cursor.close();
        db.close();
        return result;
    }
}
*/

