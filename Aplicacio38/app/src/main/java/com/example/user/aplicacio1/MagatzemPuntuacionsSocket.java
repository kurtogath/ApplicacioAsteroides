package com.example.user.aplicacio1;

import android.content.IntentFilter;
import android.media.ImageWriter;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

/**
 * Created by cicles on 22/02/2018.
 */

public class MagatzemPuntuacionsSocket implements MagatzemPuntuacions {
    //String ip = "depinframis.no-ip.biz";
    String ip = "192.168.2.2";
    int port = 4321;

    public MagatzemPuntuacionsSocket(){
        //Desactiva la comprovacio d'acces a xarxa des del fill principal
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }



    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        //La connexio se ha de realitzar dins un try catx ja que pot donar error
        try {
            Socket sk = new Socket(ip,port);
            //Flux de dades entrant
            BufferedReader entrada = new BufferedReader(new InputStreamReader(sk.getInputStream()));
            //Flux de dades sortint
            PrintWriter sortida = new PrintWriter(new OutputStreamWriter(sk.getOutputStream()),true);
            //Envia per xarxa una cadena
            sortida.println(punts+" "+nom);
            //LLegeix una cadena des de la xarxa
            String resposta = entrada.readLine();
            if (!resposta.equals("OK")) {
                Log.e("Asteroides", "Error: resposta del servidor "+" incorrecte");
            }
            //Tanca la connexio
            sk.close();
        }catch (IOException e){
            Log.e("Asteroides",e.toString(),e);
        }
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        Vector<String> result = new Vector<String>();
        try{
            Socket sk = new Socket(ip,port);
            //Entrada de dades
            BufferedReader entrada = new BufferedReader(new InputStreamReader(sk.getInputStream()));
            //Sortida de dades
            PrintWriter sortida = new PrintWriter(new OutputStreamWriter(sk.getOutputStream()),true);
            //Envia per xarxa una cadena
            sortida.println("PUNTS");
            int n = 0;
            String resposta;
            do{
                resposta=entrada.readLine();
                if (resposta!=null){
                    result.add(resposta);
                    n++;
                }
            }while (n<quantitat && resposta != null);
            //Tanca connexio
            sk.close();
        }catch (IOException e){
            Log.e("Asteroides",e.toString(),e);
        }
        return result;
    }
}
