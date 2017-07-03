package co.com.neubs.shopneubs.controls;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
    private RecyclerView mRecycleViewSaldoInventario;

    private ConsultaPaginada consultaPaginada;
    private ProductoAdapter adapter;


    private View layoutOptions;
    private DrawerLayout drawerLayoutParent;
    private NavigationViewFiltro navigationViewFiltro;

    /**
     * el layout para la vista de los productos en Grilla
     */
    private final int DEFAULT_LAYOUT_CARD_VIEW_GRID = R.layout.cardview_producto;
    /**
     * el layout para la vista de los productos en listas
     */
    private final int DEFAULT_LAYOUT_CARD_VIEW_LIST = R.layout.cardview_section_item;

    /**+
     * layout dinamico (DEFAULT_LAYOUT_CARD_VIEW_GRID o DEFAULT_LAYOUT_CARD_VIEW_LIST) con el que se visualiza los productos
     */
    private int layoutCardViewSelected = DEFAULT_LAYOUT_CARD_VIEW_GRID;


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
        mRecycleViewSaldoInventario = (RecyclerView) findViewById(R.id.recycle_view_filtro);

        layoutOptions = findViewById(R.id.layout_options);

        // se optiene el drawerLayoutPadre
        drawerLayoutParent = (DrawerLayout)((Activity)context).findViewById(R.id.drawer_layout);
        // se obtiene el navigationViewFiltro que puede contener el padre
        navigationViewFiltro = (NavigationViewFiltro) ((Activity)context).findViewById(R.id.drawer_filtro);

        // se configura el recycleView
        mRecycleViewSaldoInventario.addItemDecoration(new GridSpacingItemDecoration(2, Helper.dpToPx(3,getContext()), true));
        mRecycleViewSaldoInventario.setItemAnimator(new DefaultItemAnimator());
        mRecycleViewSaldoInventario.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecycleViewSaldoInventario.setLayoutManager(mLayoutManager);

        mBtnCambiarVista.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarVista();
            }
        });
        mBtnFiltro.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayoutParent != null)
                    drawerLayoutParent.openDrawer(GravityCompat.END);
            }
        });
    }

    public void setDrawerLayoutParent(DrawerLayout drawerLayoutParent) {
        this.drawerLayoutParent = drawerLayoutParent;
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
        this.layoutCardViewSelected = layoutCardViewSelected;
        adapter = new ProductoAdapter(getContext(),consultaPaginada,layoutCardViewSelected);
    }

    public void showProductos(ConsultaPaginada consultaPaginada,DrawerLayout drawerLayoutParent){
        this.drawerLayoutParent = drawerLayoutParent;
        this.navigationViewFiltro = (NavigationViewFiltro) drawerLayoutParent.findViewById(R.id.drawer_filtro);
        showProductos(consultaPaginada);
    }
    /**
     * Visualiza el los productos en el recycleview
     * @param consultaPaginada ConsultaPaginada que retorna la API con los datos
     */
    public void showProductos(ConsultaPaginada consultaPaginada){
        this.consultaPaginada = consultaPaginada;
        setCantidadProductos(this.consultaPaginada.getCount());
        initAdapter(layoutCardViewSelected);
        if (adapter != null) {
            mRecycleViewSaldoInventario.setAdapter(adapter);
            mRecycleViewSaldoInventario.addOnScrollListener(new OnVerticalScrollListener() {
                @Override
                public void onScrolledToBottom() {
                    super.onScrolledToBottom();
                    adapter.getNextPage(drawerLayoutParent);
                }
            });
            if (navigationViewFiltro != null){
                navigationViewFiltro.setFilterMarca(consultaPaginada.getMarcas());
            }
        }
    }

    /**
     * limpia el recycleview
     */
    public void cleanProductos(){
        if (mRecycleViewSaldoInventario != null){
            mRecycleViewSaldoInventario.setAdapter(null);
        }
    }
    /**
     * Cambia la vista del recycleView
     */
    public void cambiarVista(){
        layoutCardViewSelected = (layoutCardViewSelected == DEFAULT_LAYOUT_CARD_VIEW_GRID) ? DEFAULT_LAYOUT_CARD_VIEW_LIST : DEFAULT_LAYOUT_CARD_VIEW_GRID;
        showProductos(consultaPaginada);
    }

    /**
     * asigna la visibilidad al recycleView y al layout de las opciones
     * @param visibility visibilidad
     */
    public void setVisibility(int visibility){
        mRecycleViewSaldoInventario.setVisibility(visibility);
        layoutOptions.setVisibility(visibility);
    }




}
