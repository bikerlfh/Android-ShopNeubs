package co.com.neubs.shopneubs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
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

    private LinearLayout mMainView;
    private View mProgressView;

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

        mMainView = (LinearLayout) findViewById(R.id.root_view_pedidos);
        mProgressView = findViewById(R.id.loading_progress_bar);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view_orders);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, Helper.dpToPx(3,this), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);

        recyclerView.setLayoutManager(mLayoutManager);

        showLoadingView(true);
        // El header Token se agrega en el APIRest
        APIRest.Async.get(APIRest.URL_MIS_PEDIDOS, new IServerCallback() {
            @Override
            public void onSuccess(String json) {
                showLoadingView(false);
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
                showLoadingView(false);
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
        mMainView.removeAllViews();
        // Se infla el layout (layout_sin_pedido)
        View view = getLayoutInflater().inflate(R.layout.layout_sin_pedido, null, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));

        //Se agrega la vista al rootView
        mMainView.addView(view);
    }

    /**
     * Shows the progress UI and hides the main view
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showLoadingView(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = this.getResources().getInteger(android.R.integer.config_shortAnimTime);

            mMainView.setVisibility(show ? View.GONE : View.VISIBLE);
            mMainView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mMainView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mMainView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}