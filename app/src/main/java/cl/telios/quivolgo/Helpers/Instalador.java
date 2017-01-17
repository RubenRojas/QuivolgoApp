package cl.telios.quivolgo.Helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by rubro on 17-01-2017.
 */

public class Instalador {
    String rut;
    String pass;
    String id;

    public Instalador(String rut, String pass, String id) {
        this.rut = rut;
        this.pass = pass;
        this.id = id;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    /*************************************
     FUNCIONES DE BASE DE DATSOS
     *************************************/
    public static boolean someoneLogued(SQLiteDatabase bd){
        String query = "select rut from instalador limit 1";
        Cursor fila = bd.rawQuery(query, null);
        ////LogViewer.d("Develop", fila.getString(0));
        if (fila.moveToFirst()) {
            //LogViewer.d("Develop", fila.getString(0));
            return true;
        }
        else{
            return false;
        }
    }
    public boolean insertChofer(SQLiteDatabase bd){
        Boolean pass;
        ContentValues registro = new ContentValues();
        registro.put("rut", this.rut);
        registro.put("pass", this.pass);
        registro.put("id", this.id);
        try{
            bd.insert("instalador", null, registro);
            pass= true;
        }
        catch (Exception e){
            //LogViewer.d("Develop", e.toString());
            pass = false;
        }
        return pass;
    }
    public boolean updateChofer(SQLiteDatabase bd){
        Boolean pass;
        ContentValues registro = new ContentValues();
        registro.put("rut", this.rut);
        registro.put("pass", this.pass);
        registro.put("id", this.id);

        try{
            bd.update("instalador", registro,null, null);
            pass = true;
        }
        catch (Exception e){
            pass = false;
        }
        return true;
    }
    public boolean deleteChofer(SQLiteDatabase bd){
        Boolean pass;
        try{
            bd.delete("instalador", "id='" + this.id+"'", null);
            pass = true;
        }
        catch (Exception e){
            pass = false;
        }
        return pass;
    }
    public static Instalador getChofer(SQLiteDatabase bd) {
        String query = "select rut, pass, id from instalador limit 1";
        Cursor fila = bd.rawQuery(query, null);
        Instalador us;
        ////LogViewer.d("Develop", fila.getString(0));
        if (fila.moveToFirst()) {
            us = new Instalador(fila.getString(0), fila.getString(1), fila.getString(2));
            return us;
        }
        else{
            return null;
        }
    }
}

