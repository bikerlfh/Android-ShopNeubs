package co.com.neubs.shopneubs;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import co.com.neubs.shopneubs.adapters.MenuCategoriaAdapter;
import co.com.neubs.shopneubs.classes.GridSpacingItemDecoration;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.classes.SessionManager;
import co.com.neubs.shopneubs.classes.models.Categoria;
import co.com.neubs.shopneubs.classes.models.ItemCar;
import co.com.neubs.shopneubs.classes.models.SaldoInventario;
import co.com.neubs.shopneubs.controls.IconNotificationBadge;
import co.com.neubs.shopneubs.fragments.IndexFragment;
import co.com.neubs.shopneubs.fragments.ProductosCategoriaFragment;

public class PrincipalActivity extends AppCompatActivity {

    public static final String TAG_FRAGMENT = "FRAGMENT";

    private String codigoCategoria = null;
    private TextView lblHeaderUsuario;
    private DrawerLayout drawer;
    private NavigationView navigationViewPrincipal;
    private RecyclerView recyclerViewCategoria;
    private TextView txtmessage;

    private SessionManager sessionManager;
    private MaterialSearchView searchView;

    private IconNotificationBadge iconShopCart;

    /**
     * callback cuando se backPressed
     * se usa para cuando se hace backPressed en un fragment
     */
    protected OnBackPressedListener onBackPressedListener;

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

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Se bloquea el navigationViewFiltro si existe.
        NavigationView navigationViewFiltro = (NavigationView) findViewById(R.id.drawer_filtro);
        if (navigationViewFiltro != null){
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,navigationViewFiltro);
        }
        // se inicializa la funcionalidad del NavigationViewPrincipal
        initNavigationViewPrincipal();

        // se inicializa la funcionalidad del SearchView
        initSearchView();

        // Se inicializa con el fragment Index
        openFragment(new IndexFragment());
    }

    /**
     * inicializa la funcionalidad del NavigationViewPrincipal
     * Menu Categorias y
     */
    private void initNavigationViewPrincipal(){
        navigationViewPrincipal = (NavigationView) findViewById(R.id.nav_view);
        // Se consulta el contenedor del item Account y se asigna el onclick listener para que abra
        // la actividad AccountActivity
        View containerItemMenuAccount = findViewById(R.id.container_item_menu_account);
        containerItemMenuAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this,AccountActivity.class);
                startActivity(intent);
                drawer.closeDrawers();
            }
        });

        //navigationViewPrincipal.setNavigationItemSelectedListener(this);
        recyclerViewCategoria = (RecyclerView) findViewById(R.id.recycle_view_categoria);

        recyclerViewCategoria.addItemDecoration(new GridSpacingItemDecoration(1, Helper.dpToPx(1,this),true));
        recyclerViewCategoria.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        MenuCategoriaAdapter menuCategoriaAdapter = new MenuCategoriaAdapter(this);
        menuCategoriaAdapter.setOnCategoriaClickListener(new MenuCategoriaAdapter.OnCategoriaClickListener() {
            @Override
            public void onClick(View v, Categoria categoria) {
                // Se valida que la categoria seleccionada no sea la que se esta visualizando
                if (categoria != null && !categoria.getCodigo().equals(codigoCategoria)) {
                    codigoCategoria = categoria.getCodigo();
                    // Se crea el argumento CODIGO_CATEGORIA para pasarle al fragment
                    Bundle args = new Bundle();
                    args.putString(ProductosCategoriaFragment.PARAM_CODIGO_CATEGORIA, codigoCategoria);

                    Fragment fragment = new ProductosCategoriaFragment();
                    fragment.setArguments(args);
                    openFragment(fragment);
                    // se cierran todos los drawers
                    drawer.closeDrawers();
                }
            }
        });
        recyclerViewCategoria.setAdapter(menuCategoriaAdapter);

        // Se consulta la vista del navigationView
        //View header = navigationViewPrincipal.getHeaderView(0);
        // Se asigna el lbl header welcome
        lblHeaderUsuario = (TextView)findViewById(R.id.lbl_header_usuario);
        setTextHeaderNavigationView(sessionManager.isAuthenticated());
    }

    /**
     * Crea los listeners del SearchView
     */
    private void initSearchView(){

        // Si la versión es menor a 21 se debe esconder el frameContent cuando se abre el searchView
        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)) {
            searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
                FrameLayout frameContent = (FrameLayout) findViewById(R.id.fragment_content);

                @Override
                public void onSearchViewShown() {
                    // se esconde el frameContent
                    frameContent.setVisibility(View.GONE);
                }

                @Override
                public void onSearchViewClosed() {
                    // se visualiza el frameContent
                    frameContent.setVisibility(View.VISIBLE);
                }
            });
        }

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && query.length() > 0) {
                    Intent intent = new Intent(PrincipalActivity.this, BusquedaActivity.class);
                    intent.putExtra(BusquedaActivity.PARAM_QUERY, query);
                    startActivity(intent);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        initSugerencia();
    }

    /**
     * Cambia los textos del header que esta en el navigationView
     * @param isActiva
     */
    private void setTextHeaderNavigationView(boolean isActiva) {
        lblHeaderUsuario.setText(isActiva? sessionManager.getEmail() : getString(R.string.title_welcome));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTextHeaderNavigationView(sessionManager.isAuthenticated());
        if (iconShopCart != null) {
            iconShopCart.show(sessionManager.getCountItemsShopCar());
        }
        initSugerencia();
    }

    @Override
    public void onBackPressed() {
        // si ningun fragment asignó un callback onBackPressed
        if (onBackPressedListener == null) {
            if (drawer.isDrawerOpen(GravityCompat.START) || drawer.isDrawerOpen(GravityCompat.END) || searchView.isSearchOpen()) {
                if (searchView.isSearchOpen())
                    searchView.closeSearch();
                else
                    drawer.closeDrawers();
            }
            // Si no se está visualizando el IndexFragment, se carga
            else if (getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT).getClass() != IndexFragment.class) {
                openFragment(new IndexFragment());
                // Se descheckean todos los items del menu ya que se va a visualizar el IndexFragment
                unCheckMenuItems();
                codigoCategoria = null;
            } else
                super.onBackPressed();
        }
        else{
            // se invoca el método doBack del fragment
            onBackPressedListener.doBack();
        }
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
            iconShopCart.setIcon(R.drawable.ic_action_menu_shop_cart);
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

    /*
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
                startActivity(intent);
                break;

        }
        // Se valida que la categoria seleccionada no sea la que se esta visualizando
        if (codigoCategoria != null && !codigoCategoria.equals(codigoCategoriaAnterior)) {
            // Se crea el argumento CODIGO_CATEGORIA para pasarle al fragment
            Bundle args = new Bundle();
            args.putString(ProductosCategoriaFragment.PARAM_CODIGO_CATEGORIA, codigoCategoria);

            fragment = new ProductosCategoriaFragment();
            fragment.setArguments(args);
            openFragment(fragment);
            item.setChecked(true);
        }
        // se cierran los drawers
        drawer.closeDrawers();
        return true;
    }*/

    /**
     * Reemplaza un fragment en el contenedor
     * @param fragment fragment a cargar
     */
    private void openFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, fragment,TAG_FRAGMENT).commit();
    }

    /**
     * inicializa las sugerencias del SearchView
     */
    private void initSugerencia(){
        searchView.setSuggestions(sessionManager.getSugerencias());
        // cuando se selecciona una sugerencia se envía el query
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchView.setQuery(parent.getItemAtPosition(position).toString(),true);
            }
        });
    }

    /**
     * Asigna el onBackPressedListener a la actividad
     * @param onBackPressedListener evento
     */
    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    /**
     * deschekea todos los items del menú Principal y los submenus
     */
    private void unCheckMenuItems(){
        Menu menuPrincipal = navigationViewPrincipal.getMenu();
        for(int i=0;i < menuPrincipal.size();i++){
            MenuItem item = menuPrincipal.getItem(i);
            item.setChecked(false);
            // Si el item tiene un submenu
            // como es el caso del menu CATEGORIAS
            if (item.getSubMenu() != null){
                SubMenu subMenu = item.getSubMenu();
                for (int j = 0;j<subMenu.size();j++)
                    subMenu.getItem(j).setChecked(false);
            }
        }
    }

    /**
     * Agrega un itemCart
     * @param saldoInventario saldo inventario que se va a agregar al carro
     */
    public void agregarItemCart(SaldoInventario saldoInventario){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,(ViewGroup) findViewById(R.id.lytLayout));
        txtmessage = (TextView)layout.findViewById(R.id.toastMessage);
        // Se agrega el item al carrito
        Toast toast = new Toast(getApplicationContext());
        if (sessionManager.getItemCarByIdSaldoInventario(saldoInventario.getIdSaldoInventario()) == null) {
            ItemCar itemCar = new ItemCar();
            itemCar.setNombreProducto(saldoInventario.getProducto().getNombre());
            itemCar.setIdSaldoInventario(saldoInventario.getIdSaldoInventario());
            itemCar.setImage(saldoInventario.getProducto().getImagen());
            itemCar.setIdMarca(saldoInventario.getProducto().getIdMarca());
            itemCar.setCantidad(1);
            if (saldoInventario.getPrecioOferta() > 0)
                itemCar.setPrecioVentaUnitario(saldoInventario.getPrecioOferta());
            else
                itemCar.setPrecioVentaUnitario(saldoInventario.getPrecioVentaUnitario());
            if (sessionManager.addItemCar(itemCar)) {
                txtmessage.setText(R.string.title_item_added);
                iconShopCart.show(sessionManager.getCountItemsShopCar());
            }
            else{
                txtmessage.setText(R.string.error_default);
            }
        }
        else {
            txtmessage.setText(R.string.error_item_in_car);

        }
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    /**
     * interfaz para realizar el callback en onBackPressed desde un fragment
     */
    public interface OnBackPressedListener {
        void doBack();
    }
}
