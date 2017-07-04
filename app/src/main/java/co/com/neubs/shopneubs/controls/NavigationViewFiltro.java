package co.com.neubs.shopneubs.controls;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;

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

    private ImageButton mBtnRelevante;
    private ImageButton mBtnMenorPrecio;
    private ImageButton mBtnMayorPrecio;
    private ImageButton mBtnOferta;
    private RecyclerView mRecycleViewMarca;
    private Button mBtnAplicarFiltro;

    private MarcaFiltroAdapter marcaAdapter;

    private List<Marca> listMarca;


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

        // Se incializan los controles
        mBtnRelevante = (ImageButton) findViewById(R.id.btn_relevante);
        mBtnMenorPrecio = (ImageButton) findViewById(R.id.btn_menor_precio);
        mBtnMayorPrecio = (ImageButton) findViewById(R.id.btn_mayor_precio);
        mBtnOferta = (ImageButton) findViewById(R.id.btn_oferta);
        mRecycleViewMarca = (RecyclerView) findViewById(R.id.recycle_view_marca_filtro);
        mBtnAplicarFiltro = (Button) findViewById(R.id.btn_aplicar_filtro);
    }

    private OnClickListener onClickListenerImageOrenarPor = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public void setFilterMarca(List<Marca> listMarca){
        marcaAdapter = new MarcaFiltroAdapter(getContext(),listMarca);
        mRecycleViewMarca.setAdapter(marcaAdapter);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        mRecycleViewMarca.setLayoutManager(mLayoutManager);
    }

    public Map<String,String> getFiltro(){
        Map<String,String> params = new HashMap<>();
        final Marca marcaSelected = marcaAdapter.getMarcaSelected();
        if (marcaSelected != null){
            params.put("marca",String.valueOf(marcaSelected.getCodigo()));
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

    private int convertDpToPixels(float dpi){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpi,getResources().getDisplayMetrics());
    }


    /**
     * Adaptador para las marcas
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
