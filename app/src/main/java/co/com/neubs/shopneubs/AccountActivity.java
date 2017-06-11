package co.com.neubs.shopneubs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import co.com.neubs.shopneubs.classes.models.Usuario;

public class AccountActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnLogout;
    private TextView lblLoginSignup;
    private TextView lblPedidos,lblPerfil,lblTerminosCondiciones,lblInformacionEnvio,lblGarantias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        setSupportActionBar(toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar_account);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        lblPedidos = (TextView) findViewById(R.id.lbl_action_pedidos);
        lblPerfil = (TextView) findViewById(R.id.lbl_action_perfil);
        lblTerminosCondiciones = (TextView) findViewById(R.id.lbl_action_terminos);
        lblInformacionEnvio = (TextView) findViewById(R.id.lbl_action_informacion_envio);
        lblGarantias = (TextView) findViewById(R.id.lbl_action_garantias);

        btnLogout = (Button) findViewById(R.id.btn_logout);
        lblLoginSignup = (TextView) findViewById(R.id.lbl_action_login_register);


        lblLoginSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirLoginRegister();
            }
        });

        lblPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarInicioSesion()){
                    // Abrir actividad mis pedidos
                }
                else
                    abrirLoginRegister();
            }
        });

       lblPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarInicioSesion()){
                    // Abrir actividad mis datos
                }
                else
                    abrirLoginRegister();
            }
        });
        lblTerminosCondiciones.setOnClickListener(clickListenerLinks);
        lblInformacionEnvio.setOnClickListener(clickListenerLinks);
        lblGarantias.setOnClickListener(clickListenerLinks);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSession();
            }
        });
    }
    private View.OnClickListener clickListenerLinks = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri uri = null;
            switch (v.getId()){
                case R.id.lbl_action_terminos:
                    uri = Uri.parse("https://www.shopneubs.com/terminos-y-condiciones/");
                    break;
                case R.id.lbl_action_informacion_envio:
                    uri = Uri.parse("https://www.shopneubs.com/informacion-envio/");
                    break;
                case R.id.lbl_action_garantias:
                    uri = Uri.parse("https://www.shopneubs.com/garantia/");
                    break;
            }
            if (uri != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void cerrarSession() {

    }

    private void abrirLoginRegister(){
        Intent intent = new Intent(AccountActivity.this,LoginRegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Valida que el usuario tenga la sesión iniciada
     * @return
     */
    private boolean validarInicioSesion(){
        Usuario usuario = new Usuario(this);
        if (usuario.getLoginUser()){
            return true;
        }
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
