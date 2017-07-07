package co.com.neubs.shopneubs;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import java.util.HashMap;
import java.util.Map;
import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.APIValidations;
import co.com.neubs.shopneubs.classes.ConsultaPaginada;
import co.com.neubs.shopneubs.classes.SessionManager;
import co.com.neubs.shopneubs.controls.IconNotificationBadge;
import co.com.neubs.shopneubs.controls.NavigationViewFiltro;
import co.com.neubs.shopneubs.controls.VistaFiltroPrincipal;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

public class BusquedaActivity extends AppCompatActivity {

    public final static String PARAM_QUERY = "query";
    public final static String PARAM_SUGERENCIAS = "sugerencias";

    private SessionManager sessionManager;

    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private IconNotificationBadge iconShopCart;

    /**
     * vistaFiltroPrincipal
     */
    private VistaFiltroPrincipal vistaFiltroPrincipal;

    private String query;
    private Map<String,String> parametrosRequest;

    private DrawerLayout dreawerLayout;
    View viewProductoNoEncontrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = SessionManager.getInstance(this);

        vistaFiltroPrincipal = (VistaFiltroPrincipal) findViewById(R.id.vista_filtro_principal);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        dreawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_busqueda);

        // Se obtienen los parametros y el query
        Intent intentExtra = getIntent();
        query = intentExtra.getExtras().getString(PARAM_QUERY,"");

        // Se pasan las sugerencias al searchView
        searchView.setSuggestions(sessionManager.getSugerencias());

        vistaFiltroPrincipal.setDrawerLayoutParent(dreawerLayout);

        vistaFiltroPrincipal.setOnClickListenerAplicarFiltro(new NavigationViewFiltro.OnClickListenerAplicarFiltro() {
            @Override
            public void onClick(View v, Map<String, String> filtro) {
                if (filtro.size() > 0){
                    // agregamos al filtro los parametros iniciales
                    filtro.putAll(parametrosRequest);
                    consultarPeticionAPI(filtro);
                    vistaFiltroPrincipal.setFiltroAplicado(true);
                }
                vistaFiltroPrincipal.closeDrawer();
            }
        });

        // Se asigna el evento click del boton limpiarFiltro
        vistaFiltroPrincipal.setOnClickListenerLimpiarFiltro(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarPeticionAPI(parametrosRequest);
            }
        });

        // Se asignan los listener del searchView
        initSearchView();

        if (query.length() > 0) {
            searchView.setQuery(query, true);
        }
    }

    /**
     * Crea los listeners del SearchView
     */
    private void initSearchView(){

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                getSupportActionBar().setTitle(query);
                vistaFiltroPrincipal.cleanProductos();

                parametrosRequest = new HashMap<>();
                parametrosRequest.put("filtro",query);
                consultarPeticionAPI(parametrosRequest);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });
        // cuando se selecciona una sugerencia se envía el query
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchView.setQuery(parent.getItemAtPosition(position).toString(),true);
            }
        });
    }

    private void consultarPeticionAPI(final Map<String,String> params){
        APIRest.Async.get(APIRest.URL_FILTRO_PRODUCTO,params, new IServerCallback() {
            @Override
            public void onSuccess(String json) {
                final ConsultaPaginada consultaPaginada = APIRest.serializeObjectFromJson(json,ConsultaPaginada.class);
                if (consultaPaginada.getResults().size() > 0) {
                    vistaFiltroPrincipal.showProductos(consultaPaginada);
                    visualizarBusquedaSinResultados(false);

                    // Si se encontraron resultados y el filtro no esta como sugerencia, se guarda
                    if (!sessionManager.existSugerencia(params.get("filtro"))){
                        sessionManager.addSugerencia(params.get("filtro"));
                        // Se vuelven a asignar las sugerencias
                        searchView.setSuggestions(sessionManager.getSugerencias());
                    }
                }
                else{
                    vistaFiltroPrincipal.showLoadingProgressBar(false);
                    visualizarBusquedaSinResultados(true);
                }
            }

            @Override
            public void onError(String message_error, APIValidations apiValidations) {
                vistaFiltroPrincipal.showLoadingProgressBar(false);
                if (apiValidations!= null){
                    if (apiValidations.timeOut())
                        Snackbar.make(dreawerLayout,getString(R.string.error_connection_server),Snackbar.LENGTH_INDEFINITE).show();
                    else
                        Snackbar.make(dreawerLayout,getString(R.string.error_default),Snackbar.LENGTH_INDEFINITE).show();
                }
                else
                    Snackbar.make(dreawerLayout,"Error:"+message_error,Snackbar.LENGTH_INDEFINITE).show();
            }

        });
    }

    /**
     * Visualiza la busqueda sin resultados
     */
    private void visualizarBusquedaSinResultados(boolean visualizar)
    {
        if (visualizar) {
            vistaFiltroPrincipal.setVisibility(View.GONE);
            // Se infla el layout (shop_cart_vacio)
            if (viewProductoNoEncontrado == null) {
                viewProductoNoEncontrado = getLayoutInflater().inflate(R.layout.layout_producto_no_encontrado, null, false);
                viewProductoNoEncontrado.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));

                // Se agrega la vista al rootView
                vistaFiltroPrincipal.addView(viewProductoNoEncontrado);
            }
            TextView lblFiltro = (TextView) viewProductoNoEncontrado.findViewById(R.id.lbl_filtro);
            lblFiltro.setText("\"" + getSupportActionBar().getTitle() + "\"");

            viewProductoNoEncontrado.setVisibility(View.VISIBLE);
        }
        else {
            vistaFiltroPrincipal.setVisibility(View.VISIBLE);
            if (viewProductoNoEncontrado != null)
                viewProductoNoEncontrado.setVisibility(View.GONE);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal_toolbar, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

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
        switch (item.getItemId()){
            case R.id.action_cart:
                intent = new Intent(BusquedaActivity.this,ShopCarActivity.class);
                break;
        }
        if (intent != null){
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        }else if(dreawerLayout.isDrawerOpen(GravityCompat.END)){
            dreawerLayout.closeDrawers();
        }else if(vistaFiltroPrincipal.isFiltroAplicado()){
            // Se realiza la peticion con los parametros inciales
            consultarPeticionAPI(parametrosRequest);
            vistaFiltroPrincipal.setFiltroAplicado(false);
        }else {
            super.onBackPressed();
        }
    }
}
