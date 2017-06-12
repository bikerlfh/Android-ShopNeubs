package co.com.neubs.shopneubs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import co.com.neubs.shopneubs.classes.SessionManager;

public class AccountActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnLogout;
    private TextView lblPedidos,lblPerfil,lblTerminosCondiciones,lblInformacionEnvio,lblGarantias;
    private TextView lblWelcome;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        setSupportActionBar(toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar_account);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = SessionManager.getInstance();


        lblWelcome = (TextView) findViewById(R.id.lbl_welcome_account);
        lblPedidos = (TextView) findViewById(R.id.lbl_action_pedidos);
        lblPerfil = (TextView) findViewById(R.id.lbl_action_perfil);
        lblTerminosCondiciones = (TextView) findViewById(R.id.lbl_action_terminos);
        lblInformacionEnvio = (TextView) findViewById(R.id.lbl_action_informacion_envio);
        lblGarantias = (TextView) findViewById(R.id.lbl_action_garantias);

        btnLogout = (Button) findViewById(R.id.btn_logout);

        lblPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarSession()){
                    // Abrir actividad mis pedidos
                    Intent intent   = new Intent(AccountActivity.this,OrdersActivity.class);
                    startActivity(intent);
                }
                else
                    abrirLoginRegister();
            }
        });

       lblPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarSession()){
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
                if(sessionManager.closeUserSession(AccountActivity.this)){
                    visualizarControlesSession(false);
                }
            }
        });

        visualizarControlesSession(validarSession());
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

    /**
     * Muestra los componentes segun el estado de la sesion
     * @param isActiva
     */
    private void visualizarControlesSession(boolean isActiva){
        if (isActiva){
            lblWelcome.setText(sessionManager.getUsername().isEmpty()?sessionManager.getEmail() : sessionManager.getUsername());
            btnLogout.setVisibility(View.VISIBLE);
        }
        else{
            btnLogout.setVisibility(View.GONE);
            lblWelcome.setText(getString(R.string.welcome_title));
        }
    }

    private void abrirLoginRegister(){
        Intent intent = new Intent(AccountActivity.this,LoginRegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Valida que el usuario tenga la sesión iniciada
     * @return
     */
    private boolean validarSession(){
        if (sessionManager.isAuthenticated(this)){
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Se valida la sesión y se visualizan los controles segun su estado
        visualizarControlesSession(validarSession());
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
