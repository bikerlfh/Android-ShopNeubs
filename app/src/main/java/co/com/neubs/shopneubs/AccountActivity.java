package co.com.neubs.shopneubs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AccountActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnMisDatos;
    private Button btnPedidos;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        setSupportActionBar(toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar_account);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btnPedidos = (Button) findViewById(R.id.btn_pedidos);
        btnMisDatos = (Button) findViewById(R.id.btn_mis_datos);
        btnLogout = (Button) findViewById(R.id.btn_logout);


        btnPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarInicioSesion()){
                    // Abrir actividad mis pedidos
                }
            }
        });

        btnMisDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarInicioSesion()){
                    // Abrir actividad mis datos
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSession();
            }
        });
    }

    private void cerrarSession() {

    }

    /**
     * Valida que el usuario tenga la sesión iniciada
     * @return
     */
    private boolean validarInicioSesion(){

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
