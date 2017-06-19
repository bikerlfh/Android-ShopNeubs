package co.com.neubs.shopneubs;

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

import java.lang.reflect.Array;
import java.util.ArrayList;

import co.com.neubs.shopneubs.adapters.ShopCarAdapter;
import co.com.neubs.shopneubs.classes.GridSpacingItemDecoration;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.classes.SessionManager;
import co.com.neubs.shopneubs.classes.models.ItemCar;

public class ShopCarActivity extends AppCompatActivity {


    private SessionManager sessionManager;

    private RecyclerView recyclerView;
    private TextView lblValorTotal;
    private Button btnRealizarPedido;

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
