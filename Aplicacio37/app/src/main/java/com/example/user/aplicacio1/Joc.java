package com.example.user.aplicacio1;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by oscar on 11/11/2017.
 */

public class Joc extends Activity {

    private  VistaJoc vistaJoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joc);
        vistaJoc=(VistaJoc)findViewById(R.id.VistaJoc);
        vistaJoc.setVistaVictoria(findViewById(R.id.Victoria));
        vistaJoc.setVistaDerrota(findViewById(R.id.Derrota));
        vistaJoc.setPare(this);
    }

    protected void onPause(){
        super.onPause();
        vistaJoc.getFil().pausar();
    }

    protected void onResume(){
        super.onResume();
        vistaJoc.getFil().reanudar();
    }

    protected void onDestroy(){
        vistaJoc.getFil().aturar();
        super.onDestroy();
    }
}
