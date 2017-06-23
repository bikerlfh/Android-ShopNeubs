package co.com.neubs.shopneubs;

import android.content.Intent;
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
import android.widget.ProgressBar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        recycleView = (RecyclerView) findViewById(R.id.recycle_view_busqueda);
        rootView = findViewById(R.id.root_layout_busqueda);

        Intent intentExtra = getIntent();
        query = intentExtra.getExtras().getString(PARAM_QUERY,"");

        toolbar.setTitle(query);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Se consultan las sugerencias y se pasan al git
        sugerenciaBusqueda = new SugerenciaBusqueda(this);
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
            public boolean onQueryTextSubmit(String query) {
                mProgressBar.setVisibility(View.VISIBLE);
                Map<String, String> parametros = new HashMap<>();
                parametros.put("filtro",query);
                APIRest.Async.get(APIRest.URL_FILTRO_PRODUCTO,parametros, new IServerCallback() {
                    @Override
                    public void onSuccess(String json) {
                        mProgressBar.setVisibility(View.GONE);
                        final ConsultaPaginada consultaPaginada = APIRest.serializeObjectFromJson(json,ConsultaPaginada.class);
                        productoAdapter = new ProductoAdapter(BusquedaActivity.this,consultaPaginada,R.layout.cardview_producto);
                        recycleView.setAdapter(productoAdapter);
                        recycleView.addOnScrollListener(new OnVerticalScrollListener(){
                            @Override
                            public void onScrolledToBottom() {
                                super.onScrolledToBottom();
                                productoAdapter.getNextPage(rootView);
                            }
                        });
                    }

                    @Override
                    public void onError(String message_error, APIValidations apiValidations) {
                        mProgressBar.setVisibility(View.GONE);
                        if (apiValidations!= null)
                            Snackbar.make(rootView,"Bad Request:"+apiValidations.getResponse(),Snackbar.LENGTH_INDEFINITE).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal_toolbar, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_cart){
            Intent intent = new Intent(BusquedaActivity.this,ShopCarActivity.class);
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
        } else {
            super.onBackPressed();
        }
    }
}
