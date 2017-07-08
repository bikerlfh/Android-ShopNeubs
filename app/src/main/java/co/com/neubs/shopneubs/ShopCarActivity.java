package co.com.neubs.shopneubs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import co.com.neubs.shopneubs.adapters.ShopCarAdapter;
import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.APIValidations;
import co.com.neubs.shopneubs.classes.GridSpacingItemDecoration;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.classes.SessionManager;
import co.com.neubs.shopneubs.classes.models.PedidoVenta;
import co.com.neubs.shopneubs.controls.IconNotificationBadge;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

public class ShopCarActivity extends AppCompatActivity {


    private SessionManager sessionManager;

    private RecyclerView recyclerView;
    private TextView lblValorTotal;
    private Button btnRealizarPedido;
    private  ProgressDialog progressDialog;
    private LinearLayout rootLayout;

    private IconNotificationBadge iconShopCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_car);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rootLayout = (LinearLayout)findViewById(R.id.root_layout_shop_car);

        sessionManager = SessionManager.getInstance(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view_shop_car);
        lblValorTotal = (TextView) findViewById(R.id.lbl_valor_total);
        btnRealizarPedido = (Button) findViewById(R.id.btn_realizar_pedido);


        recyclerView = (RecyclerView) findViewById(R.id.recycle_view_shop_car);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, Helper.dpToPx(3,this), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);

        ShopCarAdapter shopCarAdapter = new ShopCarAdapter(this);
        recyclerView.setAdapter(shopCarAdapter);

        if (shopCarAdapter.getItemCount() <= 0){
            visualizarCarroVacio();
            return;
        }
        else {
            btnRealizarPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!sessionManager.isAuthenticated()) {
                        Intent intent = new Intent(ShopCarActivity.this, LoginRegisterActivity.class);
                        startActivity(intent);
                        return;
                    }

                    progressDialog = new ProgressDialog(ShopCarActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage(getString(R.string.msg_generando_pedido));
                    progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                    progressDialog.setProgress(0);
                    progressDialog.setMax(100);
                    progressDialog.show();

                    Gson gson = new GsonBuilder().create();
                    String json = gson.toJson(sessionManager.getShopCar());
                    Map<String, String> params = new HashMap<>();
                    params.put("data", json);

                    APIRest.Async.post(APIRest.URL_SOLICITUD_PEDIDO, params, new IServerCallback() {
                        @Override
                        public void onSuccess(String json) {
                            progressDialog.dismiss();
                            final PedidoVenta pedidoVenta = APIRest.serializeObjectFromJson(json, PedidoVenta.class);
                            Toast.makeText(ShopCarActivity.this, getString(R.string.msg_pedido_generado) + " " + String.valueOf(pedidoVenta.getNumeroPedido()), Toast.LENGTH_SHORT).show();
                            sessionManager.deleteShopCar();
                            // Se abre el pedido venta detalle
                            Intent intent = new Intent(ShopCarActivity.this, PedidoDetalleActivity.class);
                            intent.putExtra(PedidoDetalleActivity.PARAM_ID_PEDIDO_VENTA, pedidoVenta.getIdPedidoVenta());
                            startActivity(intent);
                            // se finaliza la actividad
                            finish();
                        }

                        @Override
                        public void onError(String message_error, APIValidations apiValidations) {
                            progressDialog.dismiss();
                            if (apiValidations != null)
                                Toast.makeText(ShopCarActivity.this, apiValidations.getDetail(), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(ShopCarActivity.this, message_error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            calcularValorTotal();
        }
    }

    /**
     * Remueve todas las vistas del rootLayout y visualiza la vista shop_cart_vacio
     */
    public void visualizarCarroVacio()
    {
        // Ser remueven todas las vistas del rootLayout
        rootLayout.removeAllViews();
        // Se infla el layout (shop_cart_vacio)
        View view = getLayoutInflater().inflate(R.layout.layout_shop_cart_vacio, null, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));

        Button btnComenzarAhora = (Button)view.findViewById(R.id.btn_comenzar_ahora);
        btnComenzarAhora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAfterTransition();
            }
        });
        //Se agrega la vista al rootLayout
        rootLayout.addView(view);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        return super.onSupportNavigateUp();
    }

    public void calcularValorTotal(){
        lblValorTotal.setText(Helper.MoneyFormat(sessionManager.getValorTotalShopCar()));
        // Se actualiza el icono ShopCart
        if (iconShopCart != null) {
            iconShopCart.show(sessionManager.getCountItemsShopCar());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal_toolbar, menu);

        menu.findItem(R.id.action_search).setVisible(false);

        final MenuItem itemMenuCart = menu.findItem(R.id.action_cart);
        iconShopCart = (IconNotificationBadge)itemMenuCart.getActionView();

        if (iconShopCart != null) {
            iconShopCart.setIcon(R.drawable.ic_menu_shop_cart);
            iconShopCart.show(sessionManager.getCountItemsShopCar());
        }
        return true;
    }

}
