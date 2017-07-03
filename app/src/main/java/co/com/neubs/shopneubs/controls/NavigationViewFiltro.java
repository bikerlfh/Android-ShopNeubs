package co.com.neubs.shopneubs.controls;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.classes.GridSpacingItemDecoration;
import co.com.neubs.shopneubs.classes.Helper;
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

    public void setListMarca(List<Marca> listMarca){
        this.listMarca = listMarca;
    }

    public String getFiltro(){
        String filtro  = "";
        Marca marcaSeleccionada = marcaAdapter.getMarcaSeleccionada();
        if (marcaSeleccionada != null){
            filtro += "marca="+marcaSeleccionada.getIdMarca();
        }
        return filtro;
    }

    private int convertDpToPixels(float dpi){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpi,getResources().getDisplayMetrics());
    }


    private class MarcaFiltroAdapter extends RecyclerView.Adapter<MarcaFiltroAdapter.MarcaViewHolder>{

        private Context context;
        private List<Marca> listMarca;
        private Marca marcaSeleccionada;

        public MarcaFiltroAdapter(Context context, List<Marca> listMarca) {
            this.context = context;
            this.listMarca = listMarca;
        }

        @Override
        public MarcaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_marca_filtro ,null);
            return new MarcaViewHolder(view);
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

        public Marca getMarcaSeleccionada(){
            return marcaSeleccionada;
        }

        public class MarcaViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

            private CardView mCardView;
            private RadioButton mNombreMarca;

            private Marca marca;

            public MarcaViewHolder(View itemView) {
                super(itemView);
                mNombreMarca = (RadioButton) itemView.findViewById(R.id.rbtn_marca);

            }
            public void bindMarca(Marca marca){
                this.marca = marca;
                mNombreMarca.setText(marca.getDescripcion());
            }

            @Override
            public void onClick(View v) {
                mCardView.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent));
            }
        }


    }
    private interface OnSelectedMarca{
        Marca onSelectedMarca();
    }
}
