package co.com.neubs.shopneubs;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.HashMap;
import java.util.Map;

import co.com.neubs.shopneubs.adapters.ProductoAdapter;
import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.APIValidations;
import co.com.neubs.shopneubs.classes.ConsultaPaginada;
import co.com.neubs.shopneubs.classes.GridSpacingItemDecoration;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.classes.OnVerticalScrollListener;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

public class BusquedaActivity extends AppCompatActivity {

    private FloatingSearchView mfloatingSearchView;
    private RecyclerView recyclerView;
    ProductoAdapter productoAdapter;

    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        // Se consulta el rootView
        rootView = findViewById(R.id.root_layout_busqueda);

        recyclerView = (RecyclerView)findViewById(R.id.recycle_view_busqueda);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, Helper.dpToPx(3,this), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(mLayoutManager);

        mfloatingSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);

        mfloatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("filtro", currentQuery);
                APIRest.Async.get(APIRest.URL_FILTRO_PRODUCTO, parametros, new IServerCallback() {
                    @Override
                    public void onSuccess(String json) {
                        final ConsultaPaginada consultaPaginada = APIRest.serializeObjectFromJson(json, ConsultaPaginada.class);
                        productoAdapter = new ProductoAdapter(BusquedaActivity.this, consultaPaginada);
                        recyclerView.setAdapter(productoAdapter);
                        recyclerView.addOnScrollListener(new OnVerticalScrollListener() {
                            @Override
                            public void onScrolledToBottom() {
                                super.onScrolledToBottom();
                                productoAdapter.getNextPage(rootView);

                            }
                        });
                        //snackbar.dismiss();
                    }

                    @Override
                    public void onError(String message_error, APIValidations apiValidations) {
                        if (apiValidations != null)
                            Toast.makeText(BusquedaActivity.this, "Bad Request:" + apiValidations.getResponse(), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(BusquedaActivity.this, message_error, Toast.LENGTH_SHORT).show();
                        //snackbar.dismiss();
                    }
                });
            }
        });
        mfloatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener(){

            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                //mfloatingSearchView.swapSuggestions();

                if (newQuery.length() >= 3) {

                }
            }
        });
    }
}
