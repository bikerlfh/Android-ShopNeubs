package co.com.neubs.shopneubs.controls;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.classes.models.Marca;

/**
 * Created by bikerlfh on 7/2/17.
 */

public class NavigationViewFiltro extends NavigationView {

    private View mContainerRelevante;
    private ImageView mBtnRelevante;
    private TextView mLblRelevante;
    private View mContainerMenorPrecio;
    private ImageView mBtnMenorPrecio;
    private TextView mLblMenorPrecio;
    private View mContainerMayorPrecio;
    private ImageView mBtnMayorPrecio;
    private TextView mLblMayorPrecio;
    private View mContainerOferta;
    private ImageView mBtnOferta;
    private TextView mLblOferta;

    private RecyclerView mRecycleViewMarca;
    private Button mBtnAplicarFiltro;
    private Button mBtnLimpiarFiltro;

    private MarcaFiltroAdapter marcaAdapter;

    /**
     * Color por defecto del filtro (icono y texto)
     */
    private final int DEFAULT_COLOR_FILTRO = R.color.menuTextColorNonChecked;
    /**
     * Color del filtro seleccionado (icono y text)
     */
    private final int DEFAULT_COLOR_SELECTED_FILTRO = R.color.menuTextColorChecked;
    /**
     * Background color por defecto del filtro
     */
    private final int DEFAULT_BACKGROUND_COLOR_FILTRO = R.color.menuBackgroudColorNonChecked;
    /**
     * Background color del filtro cuando esta seleccionado
     */
    private final int DEFAULT_BACKGROUND_COLOR_SELECTED_FILTRO = R.color.menuBackgroudColorChecked;

    private String filtroOrderBy;


    public NavigationViewFiltro(Context context) {
        super(context);
        init(context,null,0);
    }

    public NavigationViewFiltro(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public NavigationViewFiltro(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.navigationview_filtro, this);

        mContainerRelevante = findViewById(R.id.container_filter_relevante);
        mContainerMenorPrecio = findViewById(R.id.container_filter_menor_precio);
        mContainerMayorPrecio = findViewById(R.id.container_filter_mayor_precio);
        mContainerOferta = findViewById(R.id.container_filter_oferta);

        // Se incializan los controles
        mBtnRelevante = (ImageView) findViewById(R.id.btn_relevante);
        mBtnMenorPrecio = (ImageView) findViewById(R.id.btn_menor_precio);
        mBtnMayorPrecio = (ImageView) findViewById(R.id.btn_mayor_precio);
        mBtnOferta = (ImageView) findViewById(R.id.btn_oferta);

        mLblRelevante = (TextView) findViewById(R.id.lbl_relevante);
        mLblMenorPrecio = (TextView) findViewById(R.id.lbl_menor_precio);
        mLblMayorPrecio = (TextView) findViewById(R.id.lbl_mayor_precio);
        mLblOferta = (TextView) findViewById(R.id.lbl_oferta);

        mRecycleViewMarca = (RecyclerView) findViewById(R.id.recycle_view_marca_filtro);
        mBtnLimpiarFiltro = (Button) findViewById(R.id.btn_limpiar_filtro);
        mBtnAplicarFiltro = (Button) findViewById(R.id.btn_aplicar_filtro);

        mContainerRelevante.setOnClickListener(onClickListenerContainerFiltro);
        mContainerMenorPrecio.setOnClickListener(onClickListenerContainerFiltro);
        mContainerMayorPrecio.setOnClickListener(onClickListenerContainerFiltro);
        mContainerOferta.setOnClickListener(onClickListenerContainerFiltro);

        cleanFormatContainerFilter();
    }

    /**
     * Evento OnClickListener de los contenedores Filtro
     * cambia de color al container seleccionado
     */
    private OnClickListener onClickListenerContainerFiltro = new OnClickListener() {
        @Override
        public void onClick(View v) {
            cleanFormatContainerFilter();
            int color = getContext().getResources().getColor(DEFAULT_COLOR_SELECTED_FILTRO);
            switch (v.getId()){
                case R.id.container_filter_relevante:
                    mContainerRelevante.setBackgroundColor(getResources().getColor(DEFAULT_BACKGROUND_COLOR_SELECTED_FILTRO));
                    mBtnRelevante.setColorFilter(color);
                    mLblRelevante.setTextColor(color);
                    filtroOrderBy = "rel";
                    break;
                case R.id.container_filter_menor_precio:
                    mContainerMenorPrecio.setBackgroundColor(getResources().getColor(DEFAULT_BACKGROUND_COLOR_SELECTED_FILTRO));
                    mBtnMenorPrecio.setColorFilter(color);
                    mLblMenorPrecio.setTextColor(color);
                    filtroOrderBy = "asc";
                    break;
                case R.id.container_filter_mayor_precio:
                    mContainerMayorPrecio.setBackgroundColor(getResources().getColor(DEFAULT_BACKGROUND_COLOR_SELECTED_FILTRO));
                    mBtnMayorPrecio.setColorFilter(color);
                    mLblMayorPrecio.setTextColor(color);
                    filtroOrderBy = "desc";
                    break;
                case R.id.container_filter_oferta:
                    mContainerOferta.setBackgroundColor(getResources().getColor(DEFAULT_BACKGROUND_COLOR_SELECTED_FILTRO));
                    mBtnOferta.setColorFilter(color);
                    mLblOferta.setTextColor(color);
                    filtroOrderBy = "promo";
                    break;
            }
        }
    };

    /**
     * Asigna el filtro por marca
     * @param listMarca listado de Marcas a visualizar
     */
    public void setFilterMarca(List<Marca> listMarca){
        marcaAdapter = new MarcaFiltroAdapter(getContext(),listMarca);
        mRecycleViewMarca.setAdapter(marcaAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecycleViewMarca.setLayoutManager(mLayoutManager);
    }

    /**
     * Obtiene el filtro
     * @return filtro
     */
    public Map<String,String> getFiltro(){
        Map<String,String> params = new HashMap<>();
        final Marca marcaSelected = marcaAdapter.getMarcaSelected();
        if (marcaSelected != null){
            params.put("marca",marcaSelected.getCodigo());
        }
        if (filtroOrderBy != null){
            params.put("order",filtroOrderBy);
        }
        return params;
    }

    /***
     * Asigna el onClickListenerAplicarFiltro
     * @param onClickListenerAplicarFiltro OnClickListenerAplicarFiltro
     */
    public void setOnClickListenerAplicarFiltro(final OnClickListenerAplicarFiltro onClickListenerAplicarFiltro){
        mBtnAplicarFiltro.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListenerAplicarFiltro.onClick(v,getFiltro());
            }
        });
    }

    /**
     * Asigna el evento click al boton limpiar filtro
     * también limpia el formato de container y el filtroOrderBy
     * @param listener
     */
    public void setOnClickListenerLimpiarFiltro(final OnClickListener listener){
        mBtnLimpiarFiltro.setOnClickListener(listener);
    }

    /**
     * Pone los colores por defecto de los countainerFilter (Ordenar por)
     */
    public void cleanFormatContainerFilter(){
        filtroOrderBy = null;
        int color = getContext().getResources().getColor(DEFAULT_COLOR_FILTRO);
        int colorBackground = getContext().getResources().getColor(DEFAULT_BACKGROUND_COLOR_FILTRO);
        mBtnRelevante.setColorFilter(color);
        mBtnMenorPrecio.setColorFilter(color);
        mBtnMayorPrecio.setColorFilter(color);
        mBtnOferta.setColorFilter(color);

        mLblRelevante.setTextColor(color);
        mLblMenorPrecio.setTextColor(color);
        mLblMayorPrecio.setTextColor(color);
        mLblOferta.setTextColor(color);

        mContainerRelevante.setBackgroundColor(colorBackground);
        mContainerMenorPrecio.setBackgroundColor(colorBackground);
        mContainerMayorPrecio.setBackgroundColor(colorBackground);
        mContainerOferta.setBackgroundColor(colorBackground);

        mBtnRelevante.performClick();
    }

    /**
     * Adaptador para el filtro de marcas
     */
    private class MarcaFiltroAdapter extends RecyclerView.Adapter<MarcaFiltroAdapter.MarcaViewHolder>{

        private Context context;
        private List<Marca> listMarca;
        private Marca marcaSelected;
        private List<MarcaViewHolder> listMarcaViewHolder;

        public MarcaFiltroAdapter(Context context, List<Marca> listMarca) {
            this.context = context;
            this.listMarca = listMarca;
            listMarcaViewHolder = new ArrayList<>();
        }

        @Override
        public MarcaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_marca_filtro ,null);
            MarcaViewHolder viewHolder = new MarcaViewHolder(view, new OnSelectedMarca() {
                @Override
                public void OnSelectedListenerMarca(Marca marca) {
                    // cuando una marca es seleccionada
                    // los demas radioButton son ser deseleccionados
                    marcaSelected = marca;
                    for (MarcaViewHolder view : listMarcaViewHolder){
                        if (view.marca != marca){
                            view.setChecked(false);
                        }
                    }
                }
            });
            listMarcaViewHolder.add(viewHolder);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MarcaViewHolder holder, int position) {
            Marca marca = listMarca.get(position);
            holder.bindMarca(marca);
        }

        @Override
        public int getItemCount() {
            return listMarca != null? listMarca.size() : 0;
        }

        public Marca getMarcaSelected(){
            return marcaSelected;
        }

        public class MarcaViewHolder extends RecyclerView.ViewHolder{

            private RadioButton mRbtnMarca;
            private Marca marca;

            public MarcaViewHolder(View itemView,final OnSelectedMarca onSelectedMarca) {
                super(itemView);
                mRbtnMarca = (RadioButton) itemView.findViewById(R.id.rbtn_marca);
                // cuando se cambia el estado del check del radioButton, se invoca el metodo onSelectedListenerMarca
                mRbtnMarca.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked)
                            onSelectedMarca.OnSelectedListenerMarca(marca);
                    }
                });

            }
            public void bindMarca(final Marca marca){
                this.marca = marca;
                mRbtnMarca.setText(marca.getDescripcion());

            }

            public void setChecked(boolean checked){
                mRbtnMarca.setChecked(checked);
            }
        }
    }

    /**
     * interfaz para controlar la marca seleccionada
     */
    private interface OnSelectedMarca{
        void OnSelectedListenerMarca(Marca marca);
    }

    /**
     * Interfaz para controlar el click del boton aplicar filtro
     */
    public interface OnClickListenerAplicarFiltro{
        void onClick(View v,Map<String,String> filtro);
    }
}
