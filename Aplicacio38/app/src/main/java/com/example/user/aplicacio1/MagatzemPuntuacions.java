package com.example.user.aplicacio1;

import java.util.Vector;

/**
 * Created by oscar on 08/11/2017.
 */

public interface MagatzemPuntuacions {
    public void guardarPuntuacio(int punts, String nom, long data);
    public Vector<String> llistaPuntuacions(int quantitat);
}