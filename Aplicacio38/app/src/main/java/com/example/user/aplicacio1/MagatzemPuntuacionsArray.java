package com.example.user.aplicacio1;

import android.widget.Toast;

import java.util.Vector;

/**
 * Created by oscar on 08/11/2017.
 */

public class MagatzemPuntuacionsArray implements MagatzemPuntuacions {
    private Vector<String> puntuacions;

    public MagatzemPuntuacionsArray() {
        /*
        puntuacions = new Vector<String>();
        puntuacions.add("123000 Pepito Domingez");
        puntuacions.add("111000 Pedro Martinez");
        puntuacions.add("011000 Paco Perez");
        puntuacions.add("185421 Oscar Marti");
        puntuacions.add("123456 Daniel Fabian Alcon");
        puntuacions.add("142542 Eliseo Parra");
        puntuacions.add("124545 David Broncano");
        puntuacions.add("165452 Hector de Miguel");
        puntuacions.add("124525 Ignatius Farray");
        puntuacions.add("136585 Facu Diaz");
        puntuacions.add("175422 Miguel Maldonado");
        puntuacions.add("145875 Alex Pinacho");
        puntuacions.add("165854 Bea Polo");
        puntuacions.add("165452 Pablo Palacios");
        puntuacions.add("145258 Cocke Peinado");
        puntuacions.add("145258 Hector Blanco");
        puntuacions.add("132526 Helena Cueto");
        puntuacions.add("180212 Mary Jane");
        puntuacions.add("123232 Alex Fañanas");
        puntuacions.add("102452 Alvaro Fernandez");
        puntuacions.add("112212 Mario Fernandez");
        puntuacions.add("102121 Paula Gutierrez");
        puntuacions.add("121254 Pedro Lucha");
*/
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        puntuacions.add(0, punts+ " " + nom);

    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        return puntuacions;
    }
}
