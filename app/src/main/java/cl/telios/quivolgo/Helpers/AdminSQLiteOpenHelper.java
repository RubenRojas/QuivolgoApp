package cl.telios.quivolgo.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by rubro on 17-01-2017.
 */

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Develop", "On Create");
        db.execSQL("drop table if exists instalador");


        //tabla de usuario
        String query="create table instalador(" +
                "rut varchar(25) primary key," +
                "pass varchar(25), " +
                "id varchar(25) " +

                ")";
        db.execSQL(query);


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists instalador");


        //tabla de usuario
        String query="create table instalador(" +
                "rut varchar(25) primary key," +
                "pass varchar(25), " +
                "id varchar(25) " +

                ")";
        db.execSQL(query);
    }
}
