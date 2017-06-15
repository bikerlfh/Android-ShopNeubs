package co.com.neubs.shopneubs;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import co.com.neubs.shopneubs.adapters.PedidoDetalleAdapter;
import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.APIValidations;
import co.com.neubs.shopneubs.classes.GridSpacingItemDecoration;
import co.com.neubs.shopneubs.classes.models.PedidoVenta;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

public class PedidoDetalleActivity extends AppCompatActivity {

    public static final String PARAM_URL = "param_url";
    private String url;

    private RecyclerView recyclerView;
    private PedidoVenta pedidoVenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pedido_detalle);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view_pedido_detalle);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(3), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        Intent intentExtra = getIntent();
        if (intentExtra.getExtras().isEmpty())
            finish();

        APIRest.Async.get(intentExtra.getExtras().getString(PARAM_URL,""), new IServerCallback() {
            @Override
            public void onSuccess(String json) {
                pedidoVenta = APIRest.serializeObjectFromJson(json,PedidoVenta.class);
                final PedidoDetalleAdapter pedidoDetalleAdapter = new PedidoDetalleAdapter(PedidoDetalleActivity.this,pedidoVenta.getPedidoVentaPosicion());
                recyclerView.setAdapter(pedidoDetalleAdapter);
            }

            @Override
            public void onError(String message_error, APIValidations apiValidations) {
                if (apiValidations != null){
                    Toast.makeText(PedidoDetalleActivity.this,apiValidations.getDetail(),Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(PedidoDetalleActivity.this,getString(R.string.error_connection_server),Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {

        return super.onSupportNavigateUp();
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
