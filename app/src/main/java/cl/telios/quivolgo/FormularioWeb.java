package cl.telios.quivolgo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cl.telios.quivolgo.Helpers.AdminSQLiteOpenHelper;
import cl.telios.quivolgo.Helpers.Instalador;

public class FormularioWeb extends AppCompatActivity {
    private WebView browser;
    SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Ingreso de Medicion");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "QUIVOLGO", null, Integer.parseInt(getString(R.string.database_version)));
        bd = admin.getWritableDatabase();
        String codigo = getIntent().getStringExtra("codigo").toString();
        final Instalador instalador = Instalador.getChofer(bd);

        String URL = "http://telios.cl/quivolgo/inventario/medicion_celular.php?cod_parcela="+codigo+"&id_instalador="+instalador.getId();

        browser = (WebView)findViewById(R.id.webkit);

        //habilitamos javascript y el zoom
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setBuiltInZoomControls(false);
        browser.getSettings().setDisplayZoomControls(false);
        //habilitamos los plugins (flash)


        browser.loadUrl(URL);

        browser.setWebViewClient(new WebViewClient()
        {
            // evita que los enlaces se abran fuera nuestra app en el navegador de android
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                Log.d("Develop", url);
                return false;
            }
            public void onPageFinished(WebView view, String url) {

                Log.d("Develop", url);
                String url_cierre = "http://telios.cl/quivolgo/inventario/forms/insert_medicion_celular.php";
                if(url.equals(url_cierre)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(FormularioWeb.this);
                    builder.setTitle("Medicion Registrada");
                    builder.setIcon(R.drawable.appicon);
                    builder.setCancelable(false);
                    builder.setMessage("Se ha guardado la medición.");
                    String positiveText = getString(android.R.string.ok);
                    builder.setPositiveButton(positiveText,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // positive button logic
                                    startActivity(new Intent(FormularioWeb.this, Dash.class));
                                    FormularioWeb.this.finish();
                                }
                            });

                    AlertDialog dialog = builder.create();
                    // display dialog
                    dialog.show();

                }


            }

        });



    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(FormularioWeb.this, Dash.class));
        FormularioWeb.this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
