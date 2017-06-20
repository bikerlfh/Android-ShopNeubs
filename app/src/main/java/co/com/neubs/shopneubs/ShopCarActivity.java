package co.com.neubs.shopneubs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.com.neubs.shopneubs.adapters.ShopCarAdapter;
import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.APIValidations;
import co.com.neubs.shopneubs.classes.GridSpacingItemDecoration;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.classes.SessionManager;
import co.com.neubs.shopneubs.classes.models.ItemCar;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

public class ShopCarActivity extends AppCompatActivity {


    private SessionManager sessionManager;

    private RecyclerView recyclerView;
    private TextView lblValorTotal;
    private Button btnRealizarPedido;
    private  ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_car);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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




        btnRealizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(ShopCarActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage(getString(R.string.msg_generando_pedido));
                progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                progressDialog.setProgress(0);
                progressDialog.setMax(100);
                progressDialog.show();

                Gson gson = new GsonBuilder().create();
                String json =  gson.toJson(sessionManager.getShopCar());
                Map<String,String> params = new HashMap<String, String>();
                params.put("data",json);

                APIRest.Async.post(APIRest.URL_SOLICITUD_PEDIDO, params, new IServerCallback() {
                    @Override
                    public void onSuccess(String json) {
                        progressDialog.dismiss();
                        int numeroPedido = (int)APIRest.getObjectFromJson(json,"numeroPedido");
                        Toast.makeText(ShopCarActivity.this,getString(R.string.msg_pedido_generado) + " " + String.valueOf(numeroPedido),Toast.LENGTH_SHORT).show();
                        sessionManager.deleteShopCar();
                        finishAfterTransition();

                    }

                    @Override
                    public void onError(String message_error, APIValidations apiValidations) {
                        progressDialog.dismiss();
                        if (apiValidations != null)
                            Toast.makeText(ShopCarActivity.this,apiValidations.getDetail(),Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(ShopCarActivity.this,message_error,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        calcularValorTotal();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        return super.onSupportNavigateUp();
    }

    public void calcularValorTotal(){
        lblValorTotal.setText(Helper.MoneyFormat(sessionManager.getValorTotalShopCar()));
    }
}
