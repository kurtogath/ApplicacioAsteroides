package com.example.user.aplicacio1;

import android.content.ComponentName;
import android.os.StrictMode;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by cicles on 14/03/2018.
 */

public class MagatzemPuntuacionsSerWeb implements MagatzemPuntuacions {
    //static String ip="http://depinframis.no-ip.biz:18080/";
    //static String ip="http://192.168.2.2:18080/";
    static String ip="http://192.168.2.249:8080/"; //IP Clase OK
    static String servei="PuntuacionsSW/services/PuntuacionsSW/";
    static String metode_llista="llista";
    static String metode_nova_puntuacio="nova_puntuacio";


    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        try{
            //Defineix la url DEL SERVEI wEB
            URL url = new URL(ip+servei+metode_llista);
            //Establim Connexio
            HttpURLConnection connexio = (HttpURLConnection)url.openConnection();
            //Establim com envima la peticio
            connexio.setRequestMethod("POST");
            connexio.setDoOutput(true);
            //Definim un fluxe de sortida ds de la xarxa
            OutputStreamWriter sal = new OutputStreamWriter(connexio.getOutputStream());
            sal.write("maxim=");
            sal.write(URLEncoder.encode(String.valueOf(quantitat),"UTF-8"));
            //Força l'enviament de la soliitut POST
            sal.flush();
            //Avalua la resposta
            if(connexio.getResponseCode() == HttpURLConnection.HTTP_OK){
                //Tractar la resposta que esta en XML
                SAXParserFactory fabrica = SAXParserFactory.newInstance();
                SAXParser parser = fabrica.newSAXParser();
                XMLReader lector = parser.getXMLReader();
                ManejadorXML manejadorXML = new ManejadorXML();
                lector.setContentHandler(manejadorXML);
                lector.parse(new InputSource(connexio.getInputStream()));
                return manejadorXML.getLlista();
            }else {
                Log.e("LLista0", connexio.getResponseMessage());
            }
        }catch (Exception e){
            Log.e("LLista1", e.getMessage(),e);
            return null;
        }
        return null;
    }


    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        try {

            URL url = new URL(ip+servei+metode_nova_puntuacio);
            HttpURLConnection connexio = (HttpURLConnection)url.openConnection();
            connexio.setRequestMethod("POST");
            connexio.setDoOutput(true);
            OutputStreamWriter sal = new OutputStreamWriter(connexio.getOutputStream());
            sal.write("punts=");
            sal.write(URLEncoder.encode(String.valueOf(punts),"UTF-8"));
            sal.write("&nom=");
            sal.write(URLEncoder.encode(nom,"UTF-8"));
            sal.write("&data=");
            sal.write(URLEncoder.encode(String.valueOf(data),"UTF-8"));
            sal.flush();
            if(connexio.getResponseCode() == HttpURLConnection.HTTP_OK){
                SAXParserFactory fabrica = SAXParserFactory.newInstance();
                SAXParser parser = fabrica.newSAXParser();
                XMLReader lector = parser.getXMLReader();
                ManejadorXML manejadorXML = new ManejadorXML();
                lector.setContentHandler(manejadorXML);
                lector.parse(new InputSource(connexio.getInputStream()));
                if (manejadorXML.getLlista().size() != 1 || !manejadorXML.getLlista().get(0).equals("OK")){
                    Log.e("Puntuacio 0", "Error  en resposta en guardar "+" puntuacio");
                }else{
                    Log.e("Puntuacio1",connexio.getResponseMessage());
                }
            }
            connexio.disconnect();

        }catch (Exception e){
            Log.e("Puntuacion2",e.getMessage(),e);
        }
    }

    class ManejadorXML extends DefaultHandler{
        private Vector<String> llista;
        private StringBuilder cadena;

        public Vector<String> getLlista(){
            return llista;
        }

        @Override //Inicializar variables
        public void startDocument() throws SAXException{
            cadena= new StringBuilder();
            llista = new Vector<String>();
        }

        @Override//Es crida que apareix un text dins d'una etiqueta
        public void characters(char[] ch, int inici, int lon) throws SAXException{
            //guardem el text dins n string i despres el tractem
            cadena.append(ch,inici,lon);
            //SAX no garanteix que ens pasara tot el text e un sol event, si el text es molt extens es realitzaran diferens cridades a aquesta mètode.
            //Per aixo el text es va acumulant amb append()
        }

        @Override
        public void endElement(String uri, String nomLocal, String nomQualif) throws SAXException{
            if (nomLocal.equals("return")){
                try {
                    llista.add(URLDecoder.decode(cadena.toString(), "UTF-8"));
                }catch (UnsupportedEncodingException e){
                    Log.e("Asteroides", e.getMessage(),e);
                }
            }
            cadena.setLength(0);
        }
    }

}
