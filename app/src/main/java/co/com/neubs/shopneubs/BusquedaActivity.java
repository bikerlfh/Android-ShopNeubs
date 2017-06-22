package co.com.neubs.shopneubs;

import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import co.com.neubs.shopneubs.adapters.ProductoAdapter;

public class BusquedaActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    ProductoAdapter productoAdapter;
    private AppBarLayout mAppBarLayout;
    private ProgressBar mProgressBar;

    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);
    }

}
