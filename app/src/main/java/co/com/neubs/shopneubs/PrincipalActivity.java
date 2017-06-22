package co.com.neubs.shopneubs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import co.com.neubs.shopneubs.classes.SessionManager;
import co.com.neubs.shopneubs.classes.models.SugerenciaBusqueda;
import co.com.neubs.shopneubs.fragments.IndexFragment;
import co.com.neubs.shopneubs.fragments.OfertasFragment;
import co.com.neubs.shopneubs.fragments.ProductosCategoriaFragment;
import co.com.neubs.shopneubs.fragments.FiltroProductoFragment;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ProductosCategoriaFragment.OnFragmentInteractionListener,OfertasFragment.OnFragmentInteractionListener, FiltroProductoFragment.OnFragmentInteractionListener {

    private String codigoCategoria = null;
    private TextView lblHeaderWelcome;

    private SessionManager sessionManager;
    private MaterialSearchView searchView;

    private SugerenciaBusqueda sugerenciaBusqueda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        // Se consultan las sugerencias
        sugerenciaBusqueda = new SugerenciaBusqueda(this);
        searchView.setSuggestions(sugerenciaBusqueda.getAllSugerencias());

        sessionManager = SessionManager.getInstance(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, new IndexFragment()).commit();

        View header = navigationView.getHeaderView(0);
        lblHeaderWelcome = (TextView) header.findViewById(R.id.lbl_header_welcome);
        visualizarControlesSession(sessionManager.isAuthenticated());
    }

    private void visualizarControlesSession(boolean isActiva) {
        if (isActiva)
            lblHeaderWelcome.setText(sessionManager.getEmail());
        else
            lblHeaderWelcome.setText(getString(R.string.welcome_title));
    }

    @Override
    protected void onResume() {
        super.onResume();
        visualizarControlesSession(sessionManager.isAuthenticated());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal_toolbar, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()){
            case R.id.action_cart:
                intent = new Intent(PrincipalActivity.this,ShopCarActivity.class);

                break;
            case R.id.action_search:
                intent = new Intent(PrincipalActivity.this,BusquedaActivity.class);
                break;
        }
        if (intent != null)
            startActivity(intent);
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
            args.putString("CODIGO_CATEGORIA", codigoCategoria);

            fragment = new ProductosCategoriaFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, fragment).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
