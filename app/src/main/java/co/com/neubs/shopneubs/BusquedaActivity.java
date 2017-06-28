package co.com.neubs.shopneubs;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.HashMap;
import java.util.Map;

import co.com.neubs.shopneubs.adapters.ProductoAdapter;
import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.APIValidations;
import co.com.neubs.shopneubs.classes.ConsultaPaginada;
import co.com.neubs.shopneubs.classes.GridSpacingItemDecoration;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.classes.OnVerticalScrollListener;
import co.com.neubs.shopneubs.classes.models.SugerenciaBusqueda;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

public class BusquedaActivity extends AppCompatActivity {

    public final static String PARAM_QUERY = "query";
    public final static String PARAM_SUGERENCIAS = "sugerencias";

    private ProductoAdapter productoAdapter;

    private ProgressBar mProgressBar;
    private RecyclerView recycleView;

    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private SugerenciaBusqueda sugerenciaBusqueda;

    String query;

    private View rootView;
    FrameLayout rootViewResults;
    View viewProductoNoEncontrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        rootViewResults = (FrameLayout)findViewById(R.id.root_layout_resultados_busqueda);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        recycleView = (RecyclerView) findViewById(R.id.recycle_view_busqueda);
        rootView = findViewById(R.id.root_layout_busqueda);

        Intent intentExtra = getIntent();
        query = intentExtra.getExtras().getString(PARAM_QUERY,"");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Se consultan las sugerencias y se pasan al git
        sugerenciaBusqueda = new SugerenciaBusqueda();
        searchView.setSuggestions(sugerenciaBusqueda.getAllSugerencias());

        recycleView.addItemDecoration(new GridSpacingItemDecoration(2, Helper.dpToPx(3,this), true));
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);

        recycleView.setLayoutManager(mLayoutManager);

        setListenersSearchView();

        if (query.length() > 0) {
            searchView.setQuery(query, true);
        }
    }

    /**
     * Crea los listeners del SearchView
     */
    private void setListenersSearchView(){
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                getSupportActionBar().setTitle(query);
                recycleView.setAdapter(null);

                mProgressBar.setVisibility(View.VISIBLE);
                Map<String, String> parametros = new HashMap<>();
                parametros.put("filtro",query);
                APIRest.Async.get(APIRest.URL_FILTRO_PRODUCTO,parametros, new IServerCallback() {
                    @Override
                    public void onSuccess(String json) {
                        mProgressBar.setVisibility(View.GONE);
                        final ConsultaPaginada consultaPaginada = APIRest.serializeObjectFromJson(json,ConsultaPaginada.class);
                        if (consultaPaginada.getResults().size() > 0) {
                            visualizarBusquedaSinResultados(false);
                            productoAdapter = new ProductoAdapter(BusquedaActivity.this, consultaPaginada, R.layout.cardview_producto);
                            recycleView.setAdapter(productoAdapter);
                            recycleView.addOnScrollListener(new OnVerticalScrollListener() {
                                @Override
                                public void onScrolledToBottom() {
                                    super.onScrolledToBottom();
                                    productoAdapter.getNextPage(rootView);
                                }
                            });
                        }
                        else{
                            visualizarBusquedaSinResultados(true);
                        }
                    }

                    @Override
                    public void onError(String message_error, APIValidations apiValidations) {
                        mProgressBar.setVisibility(View.GONE);
                        if (apiValidations!= null){
                            if (apiValidations.timeOut())
                                Snackbar.make(rootView,getString(R.string.error_connection_server),Snackbar.LENGTH_INDEFINITE).show();
                            else
                                Snackbar.make(rootView,getString(R.string.error_default),Snackbar.LENGTH_INDEFINITE).show();
                        }
                        else
                            Snackbar.make(rootView,"Error:"+message_error,Snackbar.LENGTH_INDEFINITE).show();
                    }

                });
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

    /**
     * Visualiza la busqueda sin resultados
     */
    private void visualizarBusquedaSinResultados(boolean visualizar)
    {
        if (visualizar) {
            recycleView.setVisibility(View.GONE);
            // Se infla el layout (shop_cart_vacio)
            if (viewProductoNoEncontrado == null) {
                viewProductoNoEncontrado = getLayoutInflater().inflate(R.layout.producto_no_encontrado, null, false);
                viewProductoNoEncontrado.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));

                // Se agrega la vista al rootView
                rootViewResults.addView(viewProductoNoEncontrado);
            }
            TextView lblFiltro = (TextView) viewProductoNoEncontrado.findViewById(R.id.lbl_filtro);
            lblFiltro.setText("\"" + getSupportActionBar().getTitle() + "\"");

            viewProductoNoEncontrado.setVisibility(View.VISIBLE);
        }
        else {
            recycleView.setVisibility(View.VISIBLE);
            if (viewProductoNoEncontrado != null)
                viewProductoNoEncontrado.setVisibility(View.GONE);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal_toolbar, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()){
            case R.id.action_cart:
                intent = new Intent(BusquedaActivity.this,ShopCarActivity.class);
                break;
            case R.id.action_filtro:
                intent = new Intent(BusquedaActivity.this,FiltroActivity.class);
                break;
        }
        if (intent != null){
            startActivity(intent);
        }
        return true;
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
        } else {
            super.onBackPressed();
        }
    }
}
