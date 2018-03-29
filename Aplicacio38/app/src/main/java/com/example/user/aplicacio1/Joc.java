package com.example.user.aplicacio1;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by oscar on 11/11/2017.
 */

public class Joc extends Activity {

    private  VistaJoc vistaJoc;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joc);
        mp=MediaPlayer.create(this, R.raw.star_trek);
        mp.start();
        vistaJoc=(VistaJoc)findViewById(R.id.VistaJoc);
        vistaJoc.setVistaVictoria(findViewById(R.id.Victoria));
        vistaJoc.setVistaDerrota(findViewById(R.id.Derrota));
        vistaJoc.setPare(this);
    }

    protected void onPause(){
        super.onPause();
        vistaJoc.getFil().pausar();
    }
    protected void onStop(){
        mp.pause();
        super.onStop();

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
