package cl.telios.quivolgo;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import cl.telios.quivolgo.Helpers.AdminSQLiteOpenHelper;
import cl.telios.quivolgo.Helpers.CheckNetwork;
import cl.telios.quivolgo.Helpers.Instalador;

public class Splash extends AppCompatActivity {
    SQLiteDatabase bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "QUIVOLGO", null, Integer.parseInt(getString(R.string.database_version)));
        bd = admin.getWritableDatabase();


        if(CheckNetwork.isInternetAvailable(Splash.this)){

            continuar();
        }
        else{
            new AlertDialog.Builder(this)
                    .setTitle("Sin Conexión a internet.")
                    .setMessage("Esta aplicación necesita una conexión a internet para funcionar.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Splash.this.finish();
                        }
                    }).create().show();
        }

    }
    private void continuar(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //reviso si hay un usuario logueado
                //si existe, redireccion a dash, sino, a login
                final Intent mainIntent;
                Log.d("Develop", String.valueOf(Instalador.someoneLogued(bd)));
                if(Instalador.someoneLogued(bd)){
                    mainIntent = new Intent(Splash.this, Dash.class);
                    mainIntent.putExtra("refer", "SPLASH");
                }
                else{
                    //LogViewer.d("Develop", "Usuario NO Logueado");
                    mainIntent = new Intent(Splash.this, Login.class);
                }
                Splash.this.startActivity(mainIntent);
                //overridePendingTransition(R.transition.left_in, R.transition.left_out);
                Splash.this.finish();

            }
        }, 1500);
    }
}
