package co.com.neubs.shopneubs;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.com.neubs.shopneubs.adapters.PedidoAdapter;
import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.GridSpacingItemDecoration;
import co.com.neubs.shopneubs.classes.SessionManager;
import co.com.neubs.shopneubs.classes.models.Pedido;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

public class OrdersActivity extends AppCompatActivity {

    private final SessionManager sessionManager = SessionManager.getInstance();

    private RecyclerView recyclerView;
    private PedidoAdapter pedidoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        if(!sessionManager.isAuthenticated(this))
            this.finish();

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view_orders);

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(3), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);

        recyclerView.setLayoutManager(mLayoutManager);


        // El header Token se agrega en el APIRest
        APIRest.Async.get(APIRest.URL_MIS_PEDIDOS, new IServerCallback() {
            @Override
            public void onSuccess(String json) {
                Pedido[] listadoPedidos  = APIRest.serializeObjectFromJson(json,Pedido[].class);
                pedidoAdapter = new PedidoAdapter(OrdersActivity.this,listadoPedidos);
                recyclerView.setAdapter(pedidoAdapter);
            }

            @Override
            public void onError(String message_error, String response) {
                Toast.makeText(OrdersActivity.this,response,Toast.LENGTH_LONG).show();
            }
        });

    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
