package com.example.user.aplicacio1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, View.OnLongClickListener, GestureOverlayView.OnGesturePerformedListener{
    public static MagatzemPuntuacions magatzem;

    private Button bAcercaDe;
    private Button bSortir;
    private Button bConf;
    private Button bJugar;
    private GestureLibrary llibreria;
    // private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //llancar Buton Acerca DE
        bAcercaDe = (Button)findViewById(R.id.acercade);
        bAcercaDe.setOnClickListener(this);
        //llancar Buton finish
        bSortir = (Button)findViewById(R.id.sortir);
        //bSortir.setBackgroundResource(R.drawable.degradat);
        bSortir.setOnClickListener(this);

        //lancar el boto sortir quan feim long click mostrant puntuacions
        bSortir.setOnLongClickListener(this);

        //llancar Button conf
        bConf = (Button)findViewById(R.id.configuracio);
        bConf.setOnClickListener(this);

        //lancar el boto conf quan feim long click
        bConf.setOnLongClickListener(this);

        //Lancar el boto Jugar
        bJugar = (Button)findViewById(R.id.joc);
        bJugar.setOnClickListener(this);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String s = pref.getString("Modo de guardado",null);
        Toast.makeText(this, s,Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "onCreate",Toast.LENGTH_LONG).show();

        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Audio
        /*if(prefs.getBoolean("musica",true)) {
            mp = MediaPlayer.create(this, R.raw.audio);
            mp.start();
        }*/
        //startService(new Intent(MainActivity.this, ServeiMusica.class));
    }

    @Override
    public void onGesturePerformed(GestureOverlayView ov, Gesture gesture){
        ArrayList<Prediction> predictions = llibreria.recognize(gesture);
        Prediction prediction = predictions.get(0);
        switch (prediction.name){
            case "jugar":
                llancarJoc();
                break;
            case "configurar":
                llancarConf();
                break;
            case "acercade":
                llancarAcercaDe();
                break;
            case "cancelar":
                finish();
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);

        MenuInflater infl = getMenuInflater();

        infl.inflate(R.menu.menu_main,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.configuracio){
            llancarConf();
            return true;
        }
        if (id == R.id.acercaDe){
            llancarAcercaDe();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //metode per llançar acerca de
    public void llancarAcercaDe(){
        Intent i = new Intent(this, AcercaDe.class);
        startActivity(i);
    }
    //metode per llancar preferencies
    public void llancarConf(){
        Intent i = new Intent(this,Preferencies.class);
        startActivity(i);
    }

    //metode per llancar preferencies
    public void llancarPuntuacions(){
        Intent i = new Intent(this,Puntuacions.class);
        startActivity(i);
    }

    //Metode per llncar La classe joc
    public  void  llancarJoc(){
        Intent i = new Intent(this, Joc.class);
        startActivityForResult(i, 1234);
    }

    //Mètode que es crida de forma automatica cquan es finalitza la actividad secundaria. Permet llegir les dades retornades
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //super.onActivityResult(requestCode, resultCode, data);

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.pa1_3_key), Context.MODE_PRIVATE);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        if(requestCode == 1234 && resultCode == RESULT_OK && data != null){

            int puntuacio = data.getExtras().getInt("puntuacio");
            String nom = "Oscar";

            if (pref.getString("Modo de guardado",null).equals("0")){

                Toast.makeText(this, "1",Toast.LENGTH_LONG).show();
                magatzem = new MagatzemPuntuacionsArray();

            }else if(pref.getString("Modo de guardado",null).equals("1")) {

                Toast.makeText(this, "2",Toast.LENGTH_LONG).show();
                magatzem = new MagatzemPuntuacionsPregerencies(this);

            }else if(pref.getString("Modo de guardado",null).equals("2")) {

                Toast.makeText(this, "3",Toast.LENGTH_LONG).show();
                magatzem = new MagatzemPuntuacionsFitxerIntern(this);

            }else if(pref.getString("Modo de guardado",null).equals("3")) {

                Toast.makeText(this, "4",Toast.LENGTH_LONG).show();
                magatzem = new MagatzemPuntuacionsFitxerExtern(this);

            }else if(pref.getString("Modo de guardado",null).equals("4")) {

                Toast.makeText(this, "5",Toast.LENGTH_LONG).show();
                magatzem = new MagatzemPuntuacionsXML_SAX(this);

            }else if(pref.getString("Modo de guardado",null).equals("5")) {

                Toast.makeText(this, "6",Toast.LENGTH_LONG).show();
                magatzem  = new MagatzemPuntuacionsGson();

            }else if(pref.getString("Modo de guardado",null).equals("6")) {

                Toast.makeText(this, "7",Toast.LENGTH_LONG).show();
                magatzem  = new MagatzemPuntuacionsJson();

            }else if(pref.getString("Modo de guardado",null).equals("7")) {

                Toast.makeText(this, "8",Toast.LENGTH_LONG).show();
                magatzem  = new MagatzemPuntuacionsSQLite(this);

            }else if(pref.getString("Modo de guardado",null).equals("8")) {

                Toast.makeText(this, "9",Toast.LENGTH_LONG).show();
                magatzem  = new MagatzemPuntuacionsSQLiteRel(this);
            }else if(pref.getString("Modo de guardado",null).equals("9")) {

                Toast.makeText(this, "10", Toast.LENGTH_LONG).show();
                magatzem = new MagatzemPuntuacionsProvider(this);
            }
            magatzem.guardarPuntuacio(puntuacio, nom, System.currentTimeMillis());
            llancarPuntuacions();
        }
    }


    //metode que mostrar els valors dels preferencies constriunt una cadena
    public void mostrarPreferencies(View view){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        String cad = "Musica: " +pref.getBoolean("musica", false) + "\nTipus Grafic: " + pref.getString("graficos", "0") + "\nNumero de fragmentos: " + pref.getString("fragmentos", "0")+
                "\nActivar multijuadores: " + pref.getBoolean("modo", false) + "\nmÃ xim jugadores: " + pref.getString("maxim jugadores", "0") +
                "\ntipus Conexion: " + pref.getString("tipo conexion", "0");
        //mostrar en un Toast els valors de preferencia
        Toast.makeText(this, cad, Toast.LENGTH_SHORT).show();

    }
    //control d'esdeveniments de click
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.joc:
                llancarJoc();
                break;
            case R.id.configuracio:
                llancarConf();
                break;
            case R.id.acercade:
                llancarAcercaDe();
                break;
            case R.id.sortir:
                finish();
                break;
        }
    }

    //control d'esdeveniments de longclick
    public boolean onLongClick(View view) {
        switch (view.getId()){
            case R.id.sortir:
                llancarPuntuacions();
                return  true;

            case R.id.configuracio:
                mostrarPreferencies(null);
                return true;

            default:
                return false;
        }
    }

    //Implementar MEtodes de cicle de vida
    protected void onStart(){
        super.onStart();
        //Toast.makeText(this, "onStart",Toast.LENGTH_LONG).show();
    }

    protected void onResume(){
        super.onResume();
        //Toast.makeText(this, "onResume",Toast.LENGTH_LONG).show();
    }
    protected void onPause(){
        super.onPause();
        //Toast.makeText(this, "onPause",Toast.LENGTH_LONG).show();

    }
    protected void onStop(){
        super.onStop();
        //Toast.makeText(this, "onStop",Toast.LENGTH_LONG).show();
        //mp.pause();
    }
    protected void onRestart(){
        super.onRestart();
        //Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
    }

    protected void onDestroy(){
        //Toast.makeText(this, "onDestroy",Toast.LENGTH_LONG).show();
        // stopService(new Intent(MainActivity.this, ServeiMusica.class));
        super.onDestroy();
    }
    //Reproduir musica des de un punt quan giram el telefon
    /*@Override
    protected void onSaveInstanceState(Bundle guardarEstat) {
        super.onSaveInstanceState(guardarEstat);
        guardarEstat.putInt("posicio", mp.getCurrentPosition());
    }
    @Override
    protected void onRestoreInstanceState(Bundle recEstat) {
        super.onRestoreInstanceState(recEstat);
        mp.seekTo(recEstat.getInt("posicio"));
    }*/
}



/*
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private Button bAcercaDe;
    private Button bConfig;
    private Button popupConfig;
    private Button bPuntuacions;
    private Button bJoc;
    private Button bSortir;
    private MediaPlayer mp;
    private GestureLibrary llibreria;

    public static MagatzemPuntuacions magatzem;

    //public static MagatzemPuntuacions magatzem = new MagatzemPuntuacionsFitxerIntern();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bAcercaDe=(Button) findViewById(R.id.acercade);
        bAcercaDe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                llencarAcercaDe(null);
            }
        });

        // LLencar apreferenceis
        bConfig=(Button) findViewById(R.id.configuracio);
        bConfig.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                llencarPreferencies(null);
            }
        });

        //PopUp de configuracio
        popupConfig = (Button)findViewById(R.id.configuracio);
        popupConfig.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                mostrarPreferencies(null);
                return true;
            }
        });

        //bPuntuacions.setBackgroundResource(R.drawable.degradat);
        bPuntuacions = (Button)findViewById(R.id.sortir);
        bPuntuacions.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                llencarPuntuacions(null);
                return true;
            }
        });

        //LLençar el Joc
        bJoc=(Button)findViewById(R.id.joc);
        bJoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llencarJoc(null);
            }
        });

        Toast.makeText(this,"onCreate",Toast.LENGTH_SHORT).show();

        //Exercici 8.2

        mp=MediaPlayer.create(this, R.raw.audio);
       // mp.start();

    }

    //Mètode per llençar la vista Acerca_De

    public void llencarAcercaDe(View view) {
        Intent i = new Intent(this, AcercaDe.class);
        startActivity(i);
    }

    //Mètode per llençar la vista de les preferencies
    public void llencarPreferencies(View view){
        Intent i = new Intent(this, Preferencies.class);
        startActivity(i);
    }

    //Mètode per llençar el PopUp de les preferencies. TOAST
    public void mostrarPreferencies(View view) {
        Resources res= getResources();
        String musica =  res.getString(R.string.pa1_key);
        String graficos =  res.getString(R.string.pa2_key);
        String fragmentos =  res.getString(R.string.pa3_key);

        String online =  res.getString(R.string.pa1_2_key);
        String players =  res.getString(R.string.pa2_2_key);
        String connex =  res.getString(R.string.pa3_2_key);


        SharedPreferences pref=  PreferenceManager.getDefaultSharedPreferences(this);
        String s= "Musica: "+ pref.getBoolean(musica, false)+
                "\nTipus grafics: "+ pref.getString(graficos, "?")+
                "\nNumero fragments: "+ pref.getString(fragmentos, "?")+
                "\nAcivar multijugador: "+ pref.getBoolean(online, true)+
                "\nMaximJugadors: "+ pref.getString(players, "?")+
                "\nTipusConnexio: "+ pref.getString(connex, "?");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

    }

    //Mètode Per llençar la vista de les puntuacions

    public void llencarPuntuacions(View view) {
        Intent i = new Intent(this,Puntuacions.class);
        startActivity(i);
    }

    //Mètode per llencar el Joc
    public void llencarJoc(View view){
        Intent i = new Intent(this, Joc.class);
        startActivityForResult(i,1234);
    }

    //Mètode que es crida de forma automatica cquan es finalitza la actividad secundaria. Permet llegir les dades retornades
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        if(requestCode == 1234 && resultCode == RESULT_OK && data != null){
            int puntuacio = data.getExtras().getInt("puntuacio");
            String nom = "Jo";

            if (pref.getString("conexion","1").equals("0")){
                magatzem = new MagatzemPuntuacionsArray();
            }else if(pref.getString("conexion","2").equals("1")) {
                magatzem = new MagatzemPuntuacionsPregerencies(this);
            }else if(pref.getString("conexion","3").equals("2")) {
                magatzem = new MagatzemPuntuacionsFitxerIntern(this);
            }else if(pref.getString("conexion","4").equals("3")) {
                magatzem = new MagatzemPuntuacionsFitxerExtern(this);
            }else if(pref.getString("conexion","5").equals("4")) {
                magatzem = new MagatzemPuntuacionsXML_SAX(this);
            }else if(pref.getString("conexion","6").equals("5")) {
                magatzem  = new MagatzemPuntuacionsGson();
            }else if(pref.getString("conexion","7").equals("6")) {
                magatzem  = new MagatzemPuntuacionsJson();
            }else if(pref.getString("conexion","8").equals("7")) {
                magatzem  = new MagatzemPuntuacionsSQLite(this);
            }else if(pref.getString("conexion","9").equals("8")) {
                magatzem  = new MagatzemPuntuacionsSQLiteRel(this);
            }
            magatzem.guardarPuntuacio(puntuacio, nom, System.currentTimeMillis());
            llencarPuntuacions(null);
        }
    }

    //-------------- MENU
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater infl= getMenuInflater();
        infl.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==R.id.configuracio){
            return true;
        }

        if(id==R.id.acercaDe){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // ------------ FI MENU

    //------------------------------- Ciclede vida--------------------------------
    @Override
    protected void onStart(){
        super.onStart();
        Toast.makeText(this,"onStart",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onResume(){
        super.onResume();
        mp.start();
        Toast.makeText(this,"onResume",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onPause(){
        Toast.makeText(this,"onPause",Toast.LENGTH_SHORT).show();
        //mp.pause();
        super.onPause();
    }
    @Override
    protected void onStop(){
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
        mp.pause();
        super.onStop();

    }
    @Override
    protected void onRestart(){
        super.onRestart();
        Toast.makeText(this,"onRestart",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Toast.makeText(this,"onDestroy",Toast.LENGTH_SHORT).show();
    }


}
*/