package co.com.neubs.shopneubs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import co.com.neubs.shopneubs.classes.SessionManager;
import co.com.neubs.shopneubs.classes.models.SugerenciaBusqueda;
import co.com.neubs.shopneubs.controls.IconNotificationBadge;
import co.com.neubs.shopneubs.fragments.IndexFragment;
import co.com.neubs.shopneubs.fragments.ProductosCategoriaFragment;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ProductosCategoriaFragment.OnFragmentInteractionListener {

    public static final String TAG_FRAGMENT = "FRAGMENT";

    private String codigoCategoria = null;
    private TextView lblHeaderWelcome;
    private DrawerLayout drawer;

    private SessionManager sessionManager;
    private MaterialSearchView searchView;

    private SugerenciaBusqueda sugerenciaBusqueda;

    private IconNotificationBadge iconShopCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        // Se instancia el sessionManager
        sessionManager = SessionManager.getInstance(this);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        // Se consultan las sugerencias
        sugerenciaBusqueda = new SugerenciaBusqueda();
        searchView.setSuggestions(sugerenciaBusqueda.getAllSugerencias());


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Se inicializa con el fragment Index
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, new IndexFragment(),TAG_FRAGMENT).commit();

        // Se consulta la vista del navigationView
        View header = navigationView.getHeaderView(0);
        // Se asigna el lbl header welcome
        lblHeaderWelcome = (TextView) header.findViewById(R.id.lbl_header_welcome);
        setTextoWelcome(sessionManager.isAuthenticated());

        // se inicializa la funcionalidad del SearchView
        setListenersSearchView();
    }

    /**
     * Crea los listeners del SearchView
     */
    private void setListenersSearchView(){
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(PrincipalActivity.this,BusquedaActivity.class);
                intent.putExtra(BusquedaActivity.PARAM_QUERY,query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchView.setQuery(parent.getItemAtPosition(position).toString(),true);
            }
        });
    }

    /**
     * Cambia el texto del lblHeaderWelcome que esta en el navigationView
     * @param isActiva
     */
    private void setTextoWelcome(boolean isActiva) {
        if (isActiva)
            lblHeaderWelcome.setText(sessionManager.getEmail());
        else
            lblHeaderWelcome.setText(getString(R.string.welcome_title));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTextoWelcome(sessionManager.isAuthenticated());
        if (iconShopCart != null) {
            iconShopCart.show(sessionManager.getCountItemsShopCar());
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START) || drawer.isDrawerOpen(GravityCompat.END) || searchView.isSearchOpen()) {
            if (searchView.isSearchOpen())
                searchView.closeSearch();
            else
                drawer.closeDrawers();
        }
        // Si no se está visualizando el IndexFragment, se carga
        else if (getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT).getClass() != IndexFragment.class){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, new IndexFragment(),TAG_FRAGMENT).commit();
        }
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal_toolbar, menu);

        // Se asigna la accion search al searchView
        final MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        final MenuItem itemMenuCart = menu.findItem(R.id.action_cart);
        iconShopCart = (IconNotificationBadge)itemMenuCart.getActionView();

        if (iconShopCart != null) {
            iconShopCart.setIcon(R.drawable.ic_menu_shop_cart);
            iconShopCart.show(sessionManager.getCountItemsShopCar());
            iconShopCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionsItemSelected(itemMenuCart);
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_cart){
            Intent intent = new Intent(PrincipalActivity.this,ShopCarActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment;
        String codigoCategoriaAnterior = codigoCategoria;
        switch (item.getItemId()){
            case R.id.nav_board:
                codigoCategoria = "01";
                break;
            case R.id.nav_caja_atx:
                codigoCategoria = "02";
                break;
            case R.id.nav_consolas:
                codigoCategoria = "16";
                break;
            case R.id.nav_dd:
                codigoCategoria = "04";
                break;
            case R.id.nav_electronica:
                codigoCategoria = "17";
                break;
            case R.id.nav_fuentes:
                codigoCategoria = "05";
                break;
            case R.id.nav_impresoras:
                codigoCategoria = "06";
                break;
            case R.id.nav_memorias_ram:
                codigoCategoria = "09";
                break;
            case R.id.nav_monitores:
                codigoCategoria = "10";
                break;
            case R.id.nav_pcs:
                codigoCategoria = "03";
                break;
            case R.id.nav_perifericos:
                codigoCategoria = "15";
                break;
            case R.id.nav_portatiles:
                codigoCategoria = "11";
                break;
            case R.id.nav_procesadores:
                codigoCategoria = "12";
                break;
            case R.id.nav_refrigeracion:
                codigoCategoria = "13";
                break;
            case R.id.nav_tabletas:
                codigoCategoria = "18";
                break;
            case R.id.nav_tarjetas_video:
                codigoCategoria = "14";
                break;
            case R.id.nav_my_account:
                Intent intent = new Intent(this,AccountActivity.class);
                this.startActivity(intent);
                break;

        }
        // Se valida que la categoria seleccionada no sea la que se esta visualizando
        if (codigoCategoria != null && !codigoCategoria.equals(codigoCategoriaAnterior)) {
            // Se crea el argumento CODIGO_CATEGORIA para pasarle al fragment
            Bundle args = new Bundle();
            args.putString(ProductosCategoriaFragment.PARAM_CODIGO_CATEGORIA, codigoCategoria);

            fragment = new ProductosCategoriaFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, fragment,TAG_FRAGMENT).commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
