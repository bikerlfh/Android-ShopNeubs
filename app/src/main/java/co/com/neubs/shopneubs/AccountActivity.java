package co.com.neubs.shopneubs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import co.com.neubs.shopneubs.classes.SessionManager;
import co.com.neubs.shopneubs.classes.models.SugerenciaBusqueda;
import co.com.neubs.shopneubs.controls.IconNotificationBadge;

public class AccountActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnLogout;
    private TextView lblPedidos,lblPerfil,lblTerminosCondiciones,lblInformacionEnvio,lblGarantias;
    private TextView lblWelcome;
    private MaterialSearchView searchView;

    private SessionManager sessionManager;
    private IconNotificationBadge iconShopCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        toolbar = (Toolbar) findViewById(R.id.toolbar_account);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = SessionManager.getInstance(this);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        // Se consultan las sugerencias
        SugerenciaBusqueda sugerenciaBusqueda = new SugerenciaBusqueda();
        searchView.setSuggestions(sugerenciaBusqueda.getAllSugerencias());

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
                    Intent intent   = new Intent(AccountActivity.this,PedidosActivity.class);
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
                    Intent intent   = new Intent(AccountActivity.this,PerfilActivity.class);
                    startActivity(intent);
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
                if(sessionManager.closeUserSession()){
                    visualizarControlesSession(false);
                }
            }
        });

        visualizarControlesSession(validarSession());

        setListenersSearchView();
    }

    /**
     * Crea los listeners del SearchView
     */
    private void setListenersSearchView(){
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(AccountActivity.this,BusquedaActivity.class);
                intent.putExtra(BusquedaActivity.PARAM_QUERY,query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchView.setQuery(parent.getItemAtPosition(position).toString(),true);
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
        return sessionManager.isAuthenticated();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Se valida la sesión y se visualizan los controles segun su estado
        visualizarControlesSession(validarSession());
        if (iconShopCart != null) {
            iconShopCart.show(sessionManager.getCountItemsShopCar());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal_toolbar, menu);

        // Se asigna la accion search al searchView
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        // Se esconde el itemMenu filtro, ya que no es necesario
        menu.findItem(R.id.action_filtro).setVisible(false);

        final MenuItem itemMenuCart = menu.findItem(R.id.action_cart);
        iconShopCart = (IconNotificationBadge)itemMenuCart.getActionView();

        if (iconShopCart != null) {
            iconShopCart.setIcon(R.drawable.ic_menu_shop_cart);
            iconShopCart.show(sessionManager.getCountItemsShopCar());
            iconShopCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionsItemSelected(itemMenuCart);
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.action_cart:
                intent = new Intent(AccountActivity.this,ShopCarActivity.class);
                break;
        }
        if (intent != null)
            startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
    private void action(int resid) {
        Toast.makeText(this, getText(resid), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        return super.onSupportNavigateUp();
    }

}
