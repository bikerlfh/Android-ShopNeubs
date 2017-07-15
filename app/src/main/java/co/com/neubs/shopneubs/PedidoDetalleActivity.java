package co.com.neubs.shopneubs;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import co.com.neubs.shopneubs.adapters.PedidoDetalleAdapter;
import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.APIValidations;
import co.com.neubs.shopneubs.classes.GridSpacingItemDecoration;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.classes.models.PedidoVenta;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

public class PedidoDetalleActivity extends AppCompatActivity {

    public static final String PARAM_PEDIDO_VENTA = "pedidoVenta";
    public static final String PARAM_ID_PEDIDO_VENTA = "idPedidoVenta";


    private RecyclerView recyclerView;
    private TextView lblFecha,lblEstado,lblCostoTotal,lblNumeroProductos;

    private PedidoVenta pedidoVenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_detalle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pedido_detalle);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lblEstado= (TextView) findViewById(R.id.lbl_estado_pedido_detalle);
        lblFecha = (TextView) findViewById(R.id.lbl_fecha_pedido_detalle);
        lblCostoTotal= (TextView) findViewById(R.id.lbl_costo_total_pedido_detalle);
        lblNumeroProductos = (TextView) findViewById(R.id.lbl_numero_productos_pedido_detalle);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view_pedido_detalle);
        // ajustes en el recycleview
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, Helper.dpToPx(3,this), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));

        Intent intentExtra = getIntent();
        if (intentExtra.getExtras().isEmpty())
            finish();

        // Si se envía el pedido venta
        if (intentExtra.getExtras().containsKey(PARAM_PEDIDO_VENTA)) {
            // Se obtiene el pedido venta
            pedidoVenta = (PedidoVenta) intentExtra.getExtras().get(PARAM_PEDIDO_VENTA);
            getSupportActionBar().setTitle(getString(R.string.title_pedido).concat(String.valueOf(pedidoVenta.getNumeroPedido())));
            lblEstado.setText(pedidoVenta.getEstado());
            lblFecha.setText(pedidoVenta.getFecha());
            lblCostoTotal.setText(Helper.MoneyFormat(pedidoVenta.getValorTotal()));
            lblNumeroProductos.setText(getString(R.string.title_num_products) + " (" + (String.valueOf(pedidoVenta.getNumeroProductos())) + ")");

            APIRest.Async.get(pedidoVenta.getUrlDetalle(), new IServerCallback() {
                @Override
                public void onSuccess(String json) {
                    PedidoVenta pedidoVenta = APIRest.serializeObjectFromJson(json, PedidoVenta.class);
                    final PedidoDetalleAdapter pedidoDetalleAdapter = new PedidoDetalleAdapter(PedidoDetalleActivity.this, pedidoVenta.getPedidoVentaPosicion());
                    recyclerView.setAdapter(pedidoDetalleAdapter);
                }

                @Override
                public void onError(String message_error, APIValidations apiValidations) {
                    if (apiValidations != null) {
                        Toast.makeText(PedidoDetalleActivity.this, apiValidations.getDetail(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(PedidoDetalleActivity.this, getString(R.string.error_connection_server), Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            });
        }
        else if(intentExtra.getExtras().containsKey(PARAM_ID_PEDIDO_VENTA)){
            final String url = APIRest.URL_PEDIDO_DETALLE + String.valueOf((int)intentExtra.getExtras().get(PARAM_ID_PEDIDO_VENTA)) + "/";
            APIRest.Async.get(url, new IServerCallback() {
                @Override
                public void onSuccess(String json) {
                    pedidoVenta = APIRest.serializeObjectFromJson(json, PedidoVenta.class);
                    getSupportActionBar().setTitle(getString(R.string.title_pedido).concat(String.valueOf(pedidoVenta.getNumeroPedido())));
                    lblEstado.setText(pedidoVenta.getEstado());
                    lblFecha.setText(pedidoVenta.getFecha());
                    lblCostoTotal.setText(Helper.MoneyFormat(pedidoVenta.getValorTotal()));
                    lblNumeroProductos.setText(getString(R.string.title_num_products) + " (" + (String.valueOf(pedidoVenta.getNumeroProductos())) + ")");

                    final PedidoDetalleAdapter pedidoDetalleAdapter = new PedidoDetalleAdapter(PedidoDetalleActivity.this, pedidoVenta.getPedidoVentaPosicion());
                    recyclerView.setAdapter(pedidoDetalleAdapter);
                }

                @Override
                public void onError(String message_error, APIValidations apiValidations) {
                    if (apiValidations != null) {
                        Toast.makeText(PedidoDetalleActivity.this, apiValidations.getDetail(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(PedidoDetalleActivity.this, getString(R.string.error_connection_server), Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            finishAfterTransition();
        else
            finish();
        return true;
    }
}
