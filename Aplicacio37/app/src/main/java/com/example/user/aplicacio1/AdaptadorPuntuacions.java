package com.example.user.aplicacio1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

/**
 * Created by cicles on 08/11/2017.
 */

public class AdaptadorPuntuacions extends RecyclerView.Adapter<AdaptadorPuntuacions.ViewHolder> {

    private LayoutInflater inflador; //Crea layouts a partir de XML
    private Vector<String> llista;
    private int cont=0;
    private Context context;
    protected View.OnClickListener onClickListener;

    //----------------------------------CONSTRUCTOR-----------------------------------------------------
    /**
        Un inflador peretra posteriorment crear una vista a partir del XML.
        Aquesta classe conte les vistes que volem modificar d'un element. Aqyesta classe s'utilitza per evitar haver de crear les vistes de cada element des de zero.
        Utilitzara un objecte d'aquesta classe amb les tres vistes ja creades però sense personalitzar que tal manera qque emprarà el mateix objecte per a tots els elements
        i simplement ho personalitzara segons la posicio. Aquesta forma de treballar millora el rendiment de l'aplicacio.
    */

    public AdaptadorPuntuacions(Context context, Vector<String> llista) {


        inflador = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.llista=llista;
        this.context=context;
    }

    //------------------------------- GET I SETS ---------------------------------------------

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener=onClickListener;
    }

    //-------------------------------Clase ViewHolder ---------------------------------------

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titol, subtitol;
        public ImageView icono;


        public ViewHolder(View itemView) {
            super(itemView);
            titol = (TextView) itemView.findViewById(R.id.titol);
            subtitol = (TextView) itemView.findViewById(R.id.subtitol);
            icono = (ImageView) itemView.findViewById(R.id.icono);
        }
    }

    //------------------------------ Mètodos Sobrescritos ------------------------------------------------------

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = inflador.inflate(R.layout.element_puntuacio,parent, false);
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
        holder.titol.setText(llista.get(position));
        switch (Math.round((float)Math.random()*3)) {
            case 0:
                holder.icono.setImageResource(R.drawable.asteroide1);
                break;
            case 1:
                holder.icono.setImageResource(R.drawable.asteroide2);
                break;
            default:
                holder.icono.setImageResource(R.drawable.asteroide3);
                break;
        }
    }


    @Override
    public int getItemCount() {
            return llista.size();
    }

}


