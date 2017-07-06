package co.com.neubs.shopneubs.controls;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.adapters.ProductoAdapter;
import co.com.neubs.shopneubs.classes.ConsultaPaginada;
import co.com.neubs.shopneubs.classes.GridSpacingItemDecoration;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.classes.OnVerticalScrollListener;

/**
 * Created by bikerlfh on 7/3/17.
 */

public class VistaFiltroPrincipal extends RelativeLayout {

    private TextView mLblCantidadProducto;
    private ImageButton mBtnCambiarVista;
    private ImageButton mBtnFiltro;
    private RecyclerView mRecycleViewProductos;
    private ContentLoadingProgressBar mLoadingProgressBar;

    /**
     * itemDecoration para el recycleView cuando se visualiza como grid
     */
    private RecyclerView.ItemDecoration itemDecorationGrid;

    /**
     * itemDecoration para el recycleView cuando se visualiza como List
     */
    private RecyclerView.ItemDecoration itemDecorationList;

    /**
     * Es el layout que contiene el lblCantidadProducto y los botones btnFiltro y btnCambiarVista
     */
    private View mLayoutOptions;

    /**
     * Objeto que contiene el resultado de un request a la API
     * en el se encuentran el listado de marcas y el listado de SaldoInventario (para llenar el  adapter del recycleViewProductos)
     */
    private ConsultaPaginada consultaPaginada;
    /**
     * Adapter para el recycleViewProductos
     */
    private ProductoAdapter adapter;

    /**
     * DrawerLayaut contenedor padre y contenedero del navigationViewFiltro
     * Si este objeto es null, la funcionalidad el filtro no estará disponible
     */
    private DrawerLayout drawerLayoutParent;
    /**
     * NavigationView del filtro.
     */
    private NavigationViewFiltro navigationViewFiltro;

    /**
     * el layout para la vista de los productos en Grilla
     */
    @LayoutRes
    private final int DEFAULT_LAYOUT_GRID = R.layout.cardview_producto_grid;
    /**
     * el layout para la vista de los productos en listas
     */
    @LayoutRes
    private final int DEFAULT_LAYOUT_LIST = R.layout.cardview_producto_list;

    /**+
     * contiene el id del layout con el cual se inflará el ProductoAdapter
     * se inicializa con el DEFAULT_LAYOUT_GRID
     */
    @LayoutRes
    private int idLayoutSelected = DEFAULT_LAYOUT_GRID;

    private final int ICON_VIEW_GRID = R.drawable.ic_action_view_as_grid;
    private final int ICON_VIEW_LIST = R.drawable.ic_action_view_as_list;

    /**
     * Especifica si hay un filtro aplicado
     */
    private boolean filtroAplicado = false;


    public VistaFiltroPrincipal(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public VistaFiltroPrincipal(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public VistaFiltroPrincipal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public VistaFiltroPrincipal(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.vista_filtro_principal, this);
        // inicializamos los controles
        mLblCantidadProducto = (TextView) findViewById(R.id.lbl_cantidad_productos);
        mBtnCambiarVista = (ImageButton) findViewById(R.id.btn_cambiar_vista);
        mBtnFiltro = (ImageButton) findViewById(R.id.btn_filtro);
        mRecycleViewProductos = (RecyclerView) findViewById(R.id.recycle_view_filtro);
        mLoadingProgressBar = (ContentLoadingProgressBar) findViewById(R.id.loading_progress_bar);

        mLayoutOptions = findViewById(R.id.layout_options);

        // se configura el recycleView
        initRecycleViewGrid();

        // se optiene el drawerLayoutPadre
        setDrawerLayoutParent((DrawerLayout)((Activity)context).findViewById(R.id.drawer_layout));
        /*
        drawerLayoutParent = (DrawerLayout)((Activity)context).findViewById(R.id.drawer_layout);
        if (drawerLayoutParent != null) {
            // se obtiene el navigationViewFiltro que puede contener el padre
            navigationViewFiltro = (NavigationViewFiltro) ((Activity) context).findViewById(R.id.drawer_filtro);
        }*/

        showLoadingProgressBar(true);

        // Se asigna el evento click al boton del Cambiar Vista
        mBtnCambiarVista.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarVista();
            }
        });
        // Se asigna el evento click al boton del Filtro
        mBtnFiltro.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayoutParent != null)
                    drawerLayoutParent.openDrawer(GravityCompat.END);
            }
        });
    }

    /**
     * Asigna el drawerLayout
     * @param drawerLayoutParent drawerLayout
     */
    public void setDrawerLayoutParent(DrawerLayout drawerLayoutParent) {
        this.drawerLayoutParent = drawerLayoutParent;
        if (this.drawerLayoutParent != null) {
            navigationViewFiltro = (NavigationViewFiltro) drawerLayoutParent.findViewById(R.id.drawer_filtro);
            if (navigationViewFiltro != null){
                // Se bloquea el navigationViewFiltro, siempre cerrado
                this.drawerLayoutParent.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,navigationViewFiltro);
            }
        }
    }

    public void setNavigationViewFiltro(NavigationViewFiltro navigationViewFiltro) {
        this.navigationViewFiltro = navigationViewFiltro;
    }

    /**
     * Se visualiza la cantidad de productos
     * @param cantidadProductos
     */
    private void setCantidadProductos(int cantidadProductos){
        String str = String.valueOf(cantidadProductos) + " " + getContext().getString(R.string.msg_productos_encontrados);
        mLblCantidadProducto.setText(str);
    }

    private void initAdapter(int layoutCardViewSelected) {
        idLayoutSelected = layoutCardViewSelected;
        adapter = new ProductoAdapter(getContext(),consultaPaginada,idLayoutSelected);
    }

    /**
     * Visualiza los productos en el recycleview
     * @param consultaPaginada ConsultaPaginada que retorna la API con los datos
     * @param drawerLayoutParent drawerLayaout el que contiene el navigationViewFiltro
     */
    public void showProductos(ConsultaPaginada consultaPaginada,DrawerLayout drawerLayoutParent){
        setDrawerLayoutParent(drawerLayoutParent);
        showProductos(consultaPaginada);
    }
    /**
     * Visualiza los productos en el recycleview
     * @param consultaPaginada ConsultaPaginada que retorna la API con los datos
     */
    public void showProductos(ConsultaPaginada consultaPaginada){
        // se escpmde el loadingProgressBar
        showLoadingProgressBar(false);
        this.consultaPaginada = consultaPaginada;
        // Se visualizan los productos encontrados en ConsultaPaginada
        setCantidadProductos(this.consultaPaginada.getCount());
        // Se inicializa el adapter
        initAdapter(idLayoutSelected);

        // si existe el navigationViewFiltro, se le pasan las marcas
        if (navigationViewFiltro != null){
            navigationViewFiltro.setFilterMarca(consultaPaginada.getMarcas());
        }
        else{
            // Se esconde el boton filtro.
            mBtnFiltro.setVisibility(View.GONE);
        }

        if (adapter != null) {
            mRecycleViewProductos.setAdapter(adapter);
            mRecycleViewProductos.addOnScrollListener(new OnVerticalScrollListener() {
                @Override
                public void onScrolledToBottom() {
                    super.onScrolledToBottom();
                    adapter.getNextPage(drawerLayoutParent);
                }
            });
        }
    }

    /**
     * Asigna el evento click al boton btnApicarFiltro
     * @param onClickListenerAplicarFiltro onClickListenerAplicarFiltro
     */
    public void setOnClickListenerAplicarFiltro(NavigationViewFiltro.OnClickListenerAplicarFiltro onClickListenerAplicarFiltro){
        if (navigationViewFiltro != null) {
            navigationViewFiltro.setOnClickListenerAplicarFiltro(onClickListenerAplicarFiltro);
        }
    }

    /**
     * Asigna el evento click al boton btnApicarFiltro
     * @param listener OnClickListener Event
     */
    public void setOnClickListenerLimpiarFiltro(final OnClickListener listener){
        if (navigationViewFiltro != null){
            navigationViewFiltro.setOnClickListenerLimpiarFiltro(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigationViewFiltro.cleanFormatContainerFilter();
                    listener.onClick(v);
                    closeDrawer();
                }
            });
        }
    }

    /**
     * Cierra el drawerFiltro
     */
    public void closeDrawer(){
        if (drawerLayoutParent != null)
            drawerLayoutParent.closeDrawers();
    }

    /**
     * limpia el recycleview
     */
    public void cleanProductos(){
        if (mRecycleViewProductos != null){
            mRecycleViewProductos.setAdapter(null);
        }
    }

    /**
     * configura el recycleView para el tipo de vista GRID
     */
    private void initRecycleViewGrid(){
        if (itemDecorationGrid == null)
            itemDecorationGrid = new GridSpacingItemDecoration(2, Helper.dpToPx(3,getContext()), true);

        mRecycleViewProductos.addItemDecoration(itemDecorationGrid);
        mRecycleViewProductos.setItemAnimator(new DefaultItemAnimator());
        mRecycleViewProductos.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecycleViewProductos.setLayoutManager(mLayoutManager);

    }
    /**
     * configura el recycleView para el tipo de vista LIST
     */
    private void initRecycleViewList(){
        if (itemDecorationList == null)
            itemDecorationList = new GridSpacingItemDecoration(1, Helper.dpToPx(3,getContext()), true);

        mRecycleViewProductos.addItemDecoration(itemDecorationList);
        mRecycleViewProductos.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecycleViewProductos.setLayoutManager(mLayoutManager);
    }

    /**
     * Cambia la vista del recycleView
     */
    public void cambiarVista(){
        // Se elimina el item animator
        mRecycleViewProductos.setItemAnimator(null);
        // se remueven los item decoration Grid y List
        mRecycleViewProductos.removeItemDecoration(itemDecorationGrid);
        mRecycleViewProductos.removeItemDecoration(itemDecorationList);

        if (idLayoutSelected == DEFAULT_LAYOUT_GRID){
            // Se cambia la vista a LIST
            idLayoutSelected = DEFAULT_LAYOUT_LIST;
            mBtnCambiarVista.setImageDrawable(getContext().getApplicationContext().getResources().getDrawable(ICON_VIEW_LIST));
            // se configura el recycleView
            initRecycleViewList();
        }
        else{
            // Se cambia la vista a GRID
            idLayoutSelected = DEFAULT_LAYOUT_GRID;
            mBtnCambiarVista.setImageDrawable(getContext().getApplicationContext().getResources().getDrawable(ICON_VIEW_GRID));
            initRecycleViewGrid();
        }
        showProductos(consultaPaginada);
    }




    /**
     * Asigna la visibilidad al recycleView y al layout de las opciones
     * @param visibility visibilidad
     */
    public void setVisibility(int visibility){
        mRecycleViewProductos.setVisibility(visibility);
        mLayoutOptions.setVisibility(visibility);
    }

    /**
     * Visualiza o esconde el loadingProgressBar
     * @param show true para visualizar, false para esconderlo
     */
    public void showLoadingProgressBar(boolean show){
        if (show){
            mRecycleViewProductos.setVisibility(GONE);
            mLoadingProgressBar.setVisibility(VISIBLE);
        }else{
            mRecycleViewProductos.setVisibility(VISIBLE);
            mLoadingProgressBar.setVisibility(GONE);
        }

    }

    /**
     *  Asigna si hay o no filtro aplicado
     * @param filtroAplicado filtro aplicado
     */
    public void setFiltroAplicado(boolean filtroAplicado) {
        this.filtroAplicado = filtroAplicado;
    }

    /**
     * Verifica si un filtro esta aplicado o no
     * @return true si hay un filtro aplicado, false si no lo hay
     */
    public boolean isFiltroAplicado(){
        return filtroAplicado;
    }

}
