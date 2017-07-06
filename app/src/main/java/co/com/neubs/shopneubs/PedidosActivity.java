package co.com.neubs.shopneubs;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import co.com.neubs.shopneubs.adapters.PedidoAdapter;
import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.APIValidations;
import co.com.neubs.shopneubs.classes.GridSpacingItemDecoration;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.classes.SessionManager;
import co.com.neubs.shopneubs.classes.models.PedidoVenta;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

public class PedidosActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    private RecyclerView recyclerView;
    private PedidoAdapter pedidoAdapter;

    private ConstraintLayout rootView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = SessionManager.getInstance(this);
        if(!sessionManager.isAuthenticated())
            this.finish();

        rootView = (ConstraintLayout) findViewById(R.id.root_view_pedidos);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view_orders);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, Helper.dpToPx(3,this), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);

        recyclerView.setLayoutManager(mLayoutManager);


        // El header Token se agrega en el APIRest
        APIRest.Async.get(APIRest.URL_MIS_PEDIDOS, new IServerCallback() {
            @Override
            public void onSuccess(String json) {
                PedidoVenta[] listadoPedidoVenta = APIRest.serializeObjectFromJson(json,PedidoVenta[].class);
                if (listadoPedidoVenta.length>0){
                pedidoAdapter = new PedidoAdapter(PedidosActivity.this, listadoPedidoVenta);
                recyclerView.setAdapter(pedidoAdapter);
                }
                else {
                    visualizarSinPedido();
                }
            }

            @Override
            public void onError(String message_error, APIValidations apiValidations) {
                if (apiValidations!=null){
                    if (apiValidations.isTokenInvalid()){
                        sessionManager.closeSessionExpired(PedidosActivity.this);
                    }

                    Toast.makeText(PedidosActivity.this,apiValidations.getResponse(),Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(PedidosActivity.this,message_error,Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        return true;
    }

    public void visualizarSinPedido()
    {
        // Ser remueven todas las vistas del rootView
        rootView.removeAllViews();
        // Se infla el layout (layout_sin_pedido)
        View view = getLayoutInflater().inflate(R.layout.layout_sin_pedido, null, false);
        view.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.MATCH_PARENT));

        //Se agrega la vista al rootView
        rootView.addView(view);
    }
}