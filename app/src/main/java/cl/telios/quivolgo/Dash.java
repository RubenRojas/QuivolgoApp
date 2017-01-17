package cl.telios.quivolgo;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cl.telios.quivolgo.Helpers.AdminSQLiteOpenHelper;
import cl.telios.quivolgo.Helpers.Instalador;

public class Dash extends AppCompatActivity {
    SQLiteDatabase bd;
    TextView rut_usuario;
    Button cerrarSesion, nuevaMedicion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        getSupportActionBar().hide();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "QUIVOLGO", null, Integer.parseInt(getString(R.string.database_version)));
        bd = admin.getWritableDatabase();

        final Instalador instalador = Instalador.getChofer(bd);
        rut_usuario = (TextView)findViewById(R.id.rut_usuario);
        rut_usuario.setText(instalador.getRut());


        cerrarSesion = (Button)findViewById(R.id.cerrarSesion);
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instalador.deleteChofer(bd);
                Intent intent = new Intent(Dash.this, Splash.class);
                startActivity(intent);
                Dash.this.finish();
            }
        });

        nuevaMedicion = (Button)findViewById(R.id.nuevaMedicion);
        nuevaMedicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dash.this, Scanner.class);
                startActivity(intent);
                Dash.this.finish();
            }
        });
    }
}
