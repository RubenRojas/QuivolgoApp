package cl.telios.quivolgo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cl.telios.quivolgo.Helpers.AdminSQLiteOpenHelper;
import cl.telios.quivolgo.Helpers.Instalador;
import cl.telios.quivolgo.Helpers.WebRequest;

public class Login extends AppCompatActivity {

    EditText login;
    EditText password;
    SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "QUIVOLGO", null, Integer.parseInt(getString(R.string.database_version)));
        bd = admin.getWritableDatabase();

        Instalador us = Instalador.getChofer(bd);    }



    public void ValidateLogin(View v) {
        String email;
        String pass;
        login = (EditText) findViewById(R.id.nombre_usuario);
        email = login.getText().toString();
        password = (EditText) findViewById(R.id.password);
        pass = password.getText().toString();
        ArrayList<String> passing = new ArrayList<String>();
        passing.add(email);
        passing.add(pass);
        new getLogin().execute(passing);
    }

    private class getLogin extends AsyncTask<ArrayList<String>, Void, Void> {
        Instalador resultado;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Iniciando Sesión ... ");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(ArrayList<String>... passing) {
            //get data from view
            ArrayList<String> passed = passing[0]; //get passed arraylist
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();
            // Making a request to url and getting response
            String URL = "http://telios.cl/quivolgo/mobile/login.php?rut=" + passed.get(0) + "&pass=" + passed.get(1);
            Log.d("Develop", URL);
            //retorna un json con los datos de usuario(result: success) , sino, un json con un "result: no data";     <-- OJO A ESTO!!!
            String jsonStr = webreq.makeWebServiceCall(URL, WebRequest.GET);
            try {
                resultado = ParseJSON(jsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            String gLogin;
            try {
                gLogin = resultado.getRut();
            } catch (Exception e) {
                Log.d("Develop", e.getMessage());
                gLogin = "NoMail";
            }
            if (!gLogin.equals("NoMail")) {
                pDialog.dismiss();
                resultado.insertChofer(bd);

                startActivity(new Intent(Login.this, Dash.class));
                Login.this.finish();
            } else {
                //Toast.makeText(getApplicationContext(), "Chofer o contraseña incorrectos. Intenta nuevamente.",Toast.LENGTH_LONG).show();
                pDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setTitle("Error al Inicio de Sesion");
                builder.setIcon(R.drawable.appicon);
                builder.setMessage("Has ingresado mal tus datos. Por favor intenta nuevamente");
                String positiveText = getString(android.R.string.ok);
                builder.setPositiveButton(positiveText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // positive button logic
                            }
                        });

                AlertDialog dialog = builder.create();
                // display dialog
                dialog.show();
            }
        }
    }

    private Instalador ParseJSON(String json) throws JSONException {
        if (json != null) {
            JSONObject jsonObj = new JSONObject(json);
            String result = jsonObj.getString("result"); //success o no-data
            if (result.equals("success")) {
                JSONObject user = jsonObj.getJSONObject("user");
                String rut = user.getString("rut");
                String pass = user.getString("pass");
                String id = user.getString("id");

                Instalador instalador = new Instalador(rut, pass, id);
                return instalador;
            } else {
                Instalador instalador = new Instalador("NoMail", "", "");
                return instalador;
            }
        } else {
            Instalador instalador = new Instalador("NoMail", "", "");
            return instalador;
        }
    }
}
