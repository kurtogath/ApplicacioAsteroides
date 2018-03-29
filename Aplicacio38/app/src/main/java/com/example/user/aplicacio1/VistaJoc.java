package com.example.user.aplicacio1;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
import java.util.Vector;



public class VistaJoc extends View implements SensorEventListener{

    //Objecte que permet accedir a l'activita que crida al Layout
    private Activity pare;

    //Variable que conté la puntuació del joc
    private int puntuacio = 0;

    //variables per ASTEROIDES
    private Vector<Grafic> asteroides; //Vector amb els asteroides
    private int numAsteroides = 3;
    private int numFragments = 3; //Fragments en que es divideix;
    private Drawable drawableAsteroide[]= new Drawable[numFragments];
    //variables per a la nau
    private Grafic nau; //Grafic de la nau
    private int girNau; //increment de direció
    private double acceleracioNau; //Augment de velocitat
    private static final int MAX_VELOCITAT_NAU = 20;
    //increment estandard de gir i acceleració
    private static final int PAS_GIR_NAU = 5;
    private static final float PAS_ACCELERACIO_NAU = 0.5f;
    //FILS I TEMPS
    //Fil encarregats de processar el joc
    private ThreadJoc fil = new ThreadJoc();
    //Cada quan volem processar canvis (ms)
    private static int PERIODE_PROCES = 50;
    //Quan es va relaitzar el darrer proces
    private long darrerProces = 0;

    //Manejador d'events de la pantalla tàctil per la nau
    private float mX=0,mY=0;
    private boolean dispar = false;
    private  SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());

    //Misil
    private Grafic missil;
    private static int PASS_VELOCITAT_MISSIL = 12;
    private Vector<Grafic> missils = new Vector<Grafic>();
    private Vector<Double> tempsMissils = new Vector<Double>();

    private SensorManager mSensorManager;
    private Context contexte;

    //private MediaPlayer mpDispar, mpExplosio;

    //variables pel so
    SoundPool soundPool;
    int idDispar, idExplosio;
    //


    //Variables que indiquen l'estat del joc
    public static final int ESTAT_JUGANT = 0;
    public static final int ESTAT_VICTORIA = 1;
    public static final int ESTAT_DERROTA = 2;


    private boolean hihaValorInicial;
    private float valorInicial;

    private int estat = ESTAT_JUGANT;
    private View vistaVictoria;
    private View vistaDerrota;

    public VistaJoc(Context context, AttributeSet attrs) {
        super(context, attrs);
        contexte = context;
        //Registre el sensor d'orientacio i indica gestio de events
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> llistaSensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (!llistaSensors.isEmpty()) {
            Sensor orientacioSensor = llistaSensors.get(0);
            mSensorManager.registerListener(this, orientacioSensor, SensorManager.SENSOR_DELAY_GAME);
        }

        //Inicialitza so
        //mpDispar = MediaPlayer.create(context, R.raw.dispar);
        //mpExplosio = MediaPlayer.create(context, R.raw.explosio);

        //inicialitza so
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        idDispar = soundPool.load(context, R.raw.dispar, 0);
        idExplosio = soundPool.load(context, R.raw.explosio, 0);


        //Declara i obte les imatges
        Drawable drawableNau, drawableAteroide, drawableMissil;
        //drawableNau = null;
        //drawableAteroide = context.getResources().getDrawable(R.drawable.asteroide1);

        if(pref.getString("graficos", "1").equals("0")) {
            Path pathAsteroide = new Path();
            Path pathNau = new Path();
            pathAsteroide.moveTo((float)0.3, (float)0.0);
            pathAsteroide.lineTo((float)0.6, (float)0.0);
            pathAsteroide.lineTo((float)0.6, (float)0.3);
            pathAsteroide.lineTo((float)0.8, (float)0.2);
            pathAsteroide.lineTo((float)1.0, (float)0.4);
            pathAsteroide.lineTo((float)0.8, (float)0.6);
            pathAsteroide.lineTo((float)0.9, (float)0.9);
            pathAsteroide.lineTo((float)0.8, (float)1.0);
            pathAsteroide.lineTo((float)0.4, (float)1.0);
            pathAsteroide.lineTo((float)0.4, (float)1.0);
            pathAsteroide.lineTo((float)0.0, (float)0.6);
            pathAsteroide.lineTo((float)0.0, (float)0.2);
            pathAsteroide.lineTo((float)0.3, (float)0.0);

            pathNau.moveTo((float)0.0, (float)0.0);
            pathNau.lineTo((float)1.0, (float)1.0);
            pathNau.lineTo((float)0.0, (float)2.0);
            pathNau.lineTo((float)0.0, (float)0.0);


            ShapeDrawable dNau = new ShapeDrawable(new PathShape(pathNau,1,1));
            dNau.getPaint().setColor(Color.WHITE);
            dNau.getPaint().setStyle(Paint.Style.STROKE);
            dNau.setIntrinsicWidth(30);
            dNau.setIntrinsicHeight(20);
            drawableNau = dNau;

            ShapeDrawable dAsteroide = new ShapeDrawable(new PathShape(pathAsteroide,1,1));
            dAsteroide.getPaint().setColor(Color.WHITE);
            dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
            dAsteroide.setIntrinsicWidth(100);
            dAsteroide.setIntrinsicHeight(100);
            drawableAteroide = dAsteroide;
            setBackgroundColor(Color.BLACK);

            ShapeDrawable dMissil = new ShapeDrawable(new RectShape());
            dMissil.getPaint().setColor(Color.WHITE);
            dMissil.getPaint().setStyle(Paint.Style.STROKE);
            dMissil.setIntrinsicWidth(15);
            dMissil.setIntrinsicHeight(3);
            drawableMissil = dMissil;

        } else {
            drawableAsteroide[0] = context.getResources().getDrawable(R.drawable.asteroide1);
            drawableAsteroide[1] = context.getResources().getDrawable(R.drawable.asteroide2);
            drawableAsteroide[2] = context.getResources().getDrawable(R.drawable.asteroide3);
            drawableNau = context.getResources().getDrawable(R.drawable.enterprise);
            drawableMissil = context.getResources().getDrawable((R.drawable.missil1));
        }

        nau = new Grafic(this, drawableNau);
        missil = new Grafic(this, drawableMissil);
        //inicialitza els asteroides
        asteroides = new Vector<Grafic>();
        for(int i = 0; i < numAsteroides; i++) {
            Grafic asteroide = new Grafic(this, drawableAsteroide[0]);
            asteroide.setIncY(Math.random()*4-2);
            asteroide.setIncX(Math.random()*4-2);
            asteroide.setAngle((int) (Math.random()*360));
            asteroide.setRotacio((int) (Math.random()*8-4));
            asteroides.add(asteroide);
        }
    }

    //ACTUALITZA ELS VALORS DELS ELEMENTS
    //ES A DIR, GESTIONA ELS MOVIMENTS
    protected synchronized void actualitzaFisica() {
        //Hora actual en milisegons
        long ara = System.currentTimeMillis();
        //No fer res si el periode de proces NO s'ha complert
        if(darrerProces+PERIODE_PROCES > ara) {
            return;
        }
        //Per una execució en temps real calculem retard
        double retard =(ara-darrerProces)/PERIODE_PROCES;
        darrerProces = ara; //Per la propera vegada actualitzem velocitat i direccio de la nau a partir de girNau i
        //acceleracioNau segons l'entrada del jugador
        nau.setAngle((int) (nau.getAngle() + girNau*retard));
        double nIncX = nau.getIncX() + acceleracioNau*Math.cos(Math.toRadians(nau.getAngle()))*retard;
        double nIncY = nau.getIncY() + acceleracioNau*Math.sin(Math.toRadians(nau.getAngle()))*retard;
        //Actualitzem si el modul de la velocitat no passa el maxim
        if(Math.hypot(nIncX, nIncY) <= MAX_VELOCITAT_NAU) {
            nau.setIncX(nIncX);
            nau.setIncY(nIncY);
        }

        //Actualitzem les posicions X i Y
        nau.incrementarPos(retard);
        for(int i = 0; i < asteroides.size(); i++) {
            asteroides.get(i).incrementarPos(retard);
        }

        //Actualitzem posicio del missil
        if(!missils.isEmpty()) {
            for (int i = 0; i < missils.size(); i++) {
                missils.get(i).incrementarPos(retard);
                double tempsmissil = tempsMissils.get(i) - retard;
                tempsMissils.set(i, tempsmissil);
                if (tempsmissil < 1) {
                    missils.remove(i);
                    tempsMissils.remove(i);
                }
                if(!missils.isEmpty()) {
                    for (int j = 0; j < asteroides.size(); j++) {
                        if (missils.get(i).verificarColisio(asteroides.get(j))) {
                            destrueixAsteroide(j);
                            break;
                        }
                    }
                }
            }
        }

        //Si un asteroide choca amb la nau finalitza el joc
        for(Grafic asteroide: asteroides){
            if(asteroide.verificarColisio(nau)){
                estat = ESTAT_DERROTA;
                sortir();
            }
        }
    }

    //METODES AUXILIARS
    private void destrueixAsteroide(int i) {
        puntuacio += 1000;
        int tam;
        if (asteroides.get(i).getDrawable()!=drawableAsteroide[2]){
            if (asteroides.get(i).getDrawable()==drawableAsteroide[1]){
                tam=2; //[1]
            }else{
                tam=1;
            }
            for (int n=0; n<numFragments; n++){
                Grafic asteroide= new Grafic(this, drawableAsteroide[tam]);
                asteroide.setCenX(asteroides.get(i).getCenX());
                asteroide.setCenY(asteroides.get(i).getCenY());
                asteroide.setIncX(Math.random()*7-2-tam);
                asteroide.setIncY(Math.random()*7-2-tam);
                asteroide.setAngle((int)(Math.random()*360));
                asteroide.setRotacio((int)(Math.random()*8-4));
                asteroides.add(asteroide);
            }
        }
        asteroides.remove(i);
        //mpExplosio.start();
        soundPool.play(idExplosio,1,1,0,0,1);

        if(asteroides.isEmpty()){
            //Actualitza l'estat de la partida a Victoria
            estat = ESTAT_VICTORIA;

            //Si no qued cap asteroide en pantalla finalitza la partida;
            sortir();
        }


    }

    private void ActivarMissil() {
        Drawable drawableMissil = contexte.getResources().getDrawable((R.drawable.missil1));
        Grafic misile = new Grafic(this ,drawableMissil);
        misile.setCenX(nau.getCenX());
        misile.setCenY(nau.getCenY());
        misile.setAngle(nau.getAngle());
        misile.setIncX(Math.cos(Math.toRadians(misile.getAngle())) * PASS_VELOCITAT_MISSIL);
        misile.setIncY(Math.sin(Math.toRadians(misile.getAngle())) * PASS_VELOCITAT_MISSIL);
        double tempsMissil = (int)Math.min(this.getWidth() / Math.abs(misile.getIncX()), this.getHeight() / Math.abs(misile.getIncY())) - 2;
        //mpDispar.start();
        soundPool.play(idDispar, 1, 1,1,0,1);
        missils.addElement(misile);
        tempsMissils.addElement(tempsMissil);

    }
    protected void onSizeChanged(int ample, int alt, int ample_ant, int alt_ant) {
        super.onSizeChanged(ample, alt, ample_ant, alt_ant);
        //una vegada que coneixem la nostra amplada i altura posiciona els asteroides

        nau.setCenX (ample/2);
        nau.setCenY (alt/2);

        for(Grafic asteroide: asteroides) {
            do {
                asteroide.setCenX((int) (Math.random() * ample));
                asteroide.setCenY((int) (Math.random() * alt));
            }while(asteroide.distancia(nau) <(ample+alt)/5);
        }
        //Llança un nou fil
        darrerProces = System.currentTimeMillis();
        fil.start();
    }

    //GESTIO D'EVENTS DE LA NAU AMB LA PANTALLA TÀCTIL


    @Override
    public boolean onTouchEvent(MotionEvent mevent) {
        super.onTouchEvent(mevent);
        float x = mevent.getX();
        float y = mevent.getY();
        switch (mevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dispar = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dy < 6 && dx > 6) {
                    girNau = Math.round((x - mX) / 2);
                    dispar = false;
                } else if (dx < 6 && dy > 6) {
                    acceleracioNau = Math.round((mY - y) / 25);
                    dispar = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                girNau = 0;
                acceleracioNau = 0;
                if (dispar) {
                    ActivarMissil();
                }
                break;
        }
        mX = x;
        mY = y;
        return true;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Dibuixa els asteroides
        nau.dibuixarGrafic(canvas);
        for(Grafic asteroide: asteroides) {
            asteroide.dibuixarGrafic(canvas);
        }
        for(Grafic misils: missils) {
            misils.dibuixarGrafic(canvas);
        }

        //Mostra la vista de Victòria
        if(estat == ESTAT_VICTORIA){
            vistaVictoria.setVisibility(VISIBLE);
        }else if (estat == ESTAT_DERROTA){
            vistaDerrota.setVisibility(VISIBLE);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float valor=event.values[1]; //eix Y
        if(!hihaValorInicial) {
            valorInicial = valor;
            hihaValorInicial = true;
        }
        girNau = (int) (valor - valorInicial) / 3;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //CLASSE QUE CREA UN NOU FIL
    class ThreadJoc extends Thread {
        private boolean pausa,corrent;

        public synchronized void pausar() {
            pausa=true;
        }

        public synchronized void reanudar() {
            pausa=false;
            notify();
        }

        public synchronized void aturar() {
            corrent = false;
            if(pausa)reanudar();
        }
        public void run() {
            corrent = true;
            while(corrent) {
                actualitzaFisica();
                synchronized (this) {
                    while(pausa) {
                        try {
                            wait();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }

    public ThreadJoc getFil() {
        return fil;
    }
    public SensorManager getmSensorManager() {
        return mSensorManager;
    }

    public void setPare(Activity pare){
        this.pare = pare;
    }

    //Métode que permet finalitzar el joc retornant la puntuacio
    private void sortir(){
        Bundle bundle = new Bundle();
        bundle.putInt("puntuacio", puntuacio);

        Intent intent = new Intent();
        intent.putExtras(bundle);
        pare.setResult(Activity.RESULT_OK, intent);
        //pare.finish();
    }

    //Métodes que indiquen la vista a mostrar quan acaba la partida
    public void setVistaDerrota(View vista){
        vistaDerrota = vista;
    }

    public void setVistaVictoria(View vista){
        vistaVictoria = vista;
    }
}

