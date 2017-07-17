package co.com.neubs.shopneubs.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.classes.models.Categoria;

/**
 * Created by bikerlfh on 7/10/17.
 */

public class MenuCategoriaAdapter extends RecyclerView.Adapter<MenuCategoriaAdapter.MenuCategoriaViewHolder>{
    private Context context;
    private List<Categoria> listCategoriaPadre;
    private OnCategoriaClickListener onCategoriaClickListener;
    private List<MenuCategoriaViewHolder> viewHolders = new ArrayList<>();

    private View.OnClickListener onSubCategoriaClickListener;

    public MenuCategoriaAdapter(Context context) {
        this.context = context;
        this.listCategoriaPadre = new Categoria().getCategoriasPadre();
    }

    public MenuCategoriaAdapter(Context context, List<Categoria> listCategoriaPadre) {
        this.context = context;
        this.listCategoriaPadre = listCategoriaPadre;
    }

    @Override
    public MenuCategoriaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_categoria,null);
        MenuCategoriaViewHolder viewHolder = new MenuCategoriaViewHolder(view);
        viewHolders.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MenuCategoriaViewHolder holder, int position) {
        final Categoria categoria = listCategoriaPadre.get(position);
        holder.bindCategoria(categoria, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSubCategoriaClickListener != null)
                    onSubCategoriaClickListener.onClick(v);
                unCheckCategorias();
                holder.setChecked(true);
                onCategoriaClickListener.onClick(v,categoria);

            }
        });
    }

    /**
     * uncheckea todas las categorias
     */
    public void unCheckCategorias(){
        for (MenuCategoriaViewHolder viewHolder:viewHolders) {
            viewHolder.setChecked(false);
            if (viewHolder.adapterSubCategorias != null)
                viewHolder.adapterSubCategorias.unCheckCategorias();
        }
    }

    /**
     * Evento click de una subcategoria
     * @param listener onClickListener
     */
    public void setOnSubCategoriaClickListener(View.OnClickListener listener){
        this.onSubCategoriaClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return listCategoriaPadre.size();
    }

    /**
     * Asigna el onCategoriaClickListener
     * @param onCategoriaClickListener onCategoriaClickListener
     */
    public void setOnCategoriaClickListener(final OnCategoriaClickListener onCategoriaClickListener) {
        this.onCategoriaClickListener = onCategoriaClickListener;
    }

    /**
     * ViewHolder
     */
    protected class MenuCategoriaViewHolder extends RecyclerView.ViewHolder{

        private Context context;
        private TextView lblTitulo;
        private ImageView imgIcon,imgBtnExpandable;
        private RecyclerView recyclerViewSubCategorias;
        private View containerPadre;
        private int idIcon;
        private boolean inShowSubCategoria = false;
        public Categoria categoria;
        public MenuCategoriaAdapter adapterSubCategorias;

        public MenuCategoriaViewHolder(View itemView) {
            super(itemView);
            containerPadre = itemView.findViewById(R.id.container_padre);
            lblTitulo = (TextView) itemView.findViewById(R.id.lbl_titulo_categoria);
            imgIcon = (ImageView) itemView.findViewById(R.id.img_icon);
            imgBtnExpandable = (ImageView) itemView.findViewById(R.id.img_btn_expandible);
            recyclerViewSubCategorias = (RecyclerView) itemView.findViewById(R.id.recycle_view_subcategorias);
            recyclerViewSubCategorias.setVisibility(View.GONE);
            context = itemView.getContext();
            setChecked(false);
        }

        protected void bindCategoria(final Categoria categoria, View.OnClickListener onClickListenerCategoria){
            this.categoria = categoria;
            lblTitulo.setText(categoria.getDescripcion());
            setIcon(categoria.getCodigo());

            // Se consultan las categorias hijas
            List<Categoria> listCategoriaHijo = categoria.getListCategoriaHijo();
            // si existen categorias hijas
            if (listCategoriaHijo.size() > 0){
                /// Se visualiza el boton expandable
                imgBtnExpandable.setVisibility(View.VISIBLE);
                // Se crea al adaptador menuCategoriaAdapter para las subCategorias
                adapterSubCategorias = new MenuCategoriaAdapter(context,listCategoriaHijo);
                // Se asigna el evento onClick de la subcategoria para que unChecked todas las categorias
                adapterSubCategorias.setOnSubCategoriaClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        unCheckCategorias();
                    }
                });
                // Se asigna el mismo onCategoriaClickListener
                adapterSubCategorias.setOnCategoriaClickListener(onCategoriaClickListener);

                recyclerViewSubCategorias.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
                recyclerViewSubCategorias.setAdapter(adapterSubCategorias);
                // se asigna el evento click al boton expandible
                imgBtnExpandable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSubCategoria(!inShowSubCategoria);
                    }
                });
            }
            else
                imgBtnExpandable.setVisibility(View.GONE);

            // Se asigna el onClickListener al container si se ha proporcionado el onCategoriaClickListener
            if (onCategoriaClickListener != null) {
                containerPadre.setOnClickListener(onClickListenerCategoria);
            }
        }

        public void setChecked(boolean checked){
            if(checked){
                lblTitulo.setTextColor(context.getResources().getColor(R.color.menuTextColorChecked));
                imgIcon.setColorFilter(context.getResources().getColor(R.color.menuIconColorChecked));
                imgBtnExpandable.setColorFilter(context.getResources().getColor(R.color.menuIconColorChecked));
                containerPadre.setBackgroundColor(context.getResources().getColor(R.color.menuBackgroudColorChecked));
            }
            else{
                lblTitulo.setTextColor(context.getResources().getColor(R.color.menuTextColorNonChecked));
                imgIcon.setColorFilter(context.getResources().getColor(R.color.menuIconColorNonChecked));
                imgBtnExpandable.setColorFilter(context.getResources().getColor(R.color.menuIconColorNonChecked));
                containerPadre.setBackgroundColor(context.getResources().getColor(R.color.menuBackgroudColorNonChecked));
            }
        }
        /**
         * Asigna el icono a las categorias
         * @param codigoCategoria codigo de la categoria
         */
        private void setIcon(String codigoCategoria){
            switch (codigoCategoria){
                case "01":
                    idIcon = R.drawable.ic_categoria_01;
                    break;
                case "02":
                    idIcon = R.drawable.ic_categoria_02;
                    break;
                case "03":
                    idIcon = R.drawable.ic_categoria_03;
                    break;
                case "04":
                    idIcon = R.drawable.ic_categoria_04;
                    break;
                case "05":
                    idIcon = R.drawable.ic_categoria_05;
                    break;
                case "06":
                    idIcon = R.drawable.ic_categoria_06;
                    break;
                /*case "07":
                    idIcon = R.drawable.ic_categoria_07;
                    break;*/
                case "08":
                    idIcon = R.drawable.ic_categoria_08;
                    break;
                case "09":
                    idIcon = R.drawable.ic_categoria_09;
                    break;
                case "10":
                    idIcon = R.drawable.ic_categoria_10;
                    break;
                case "11":
                    idIcon = R.drawable.ic_categoria_11;
                    break;
                case "12":
                    idIcon = R.drawable.ic_categoria_12;
                    break;
                case "13":
                    idIcon = R.drawable.ic_categoria_13;
                    break;
                case "14":
                    idIcon = R.drawable.ic_categoria_14;
                    break;
                case "15":
                    idIcon = R.drawable.ic_categoria_15;
                    break;
                case "16":
                    idIcon = R.drawable.ic_categoria_16;
                    break;
                case "17":
                    idIcon = R.drawable.ic_categoria_17;
                    break;
                case "18":
                    idIcon = R.drawable.ic_categoria_18;
                    break;
                default:
                    idIcon = 0;
                    break;
            }
            if (idIcon > 0){
                imgIcon.setImageDrawable(context.getResources().getDrawable(idIcon));
                imgIcon.setVisibility(View.VISIBLE);
            }
            else
                imgIcon.setVisibility(View.INVISIBLE);
        }

        /**
         * Visualiza o esconde las subcategorias
         * @param show
         */
        private void showSubCategoria(boolean show){
            imgBtnExpandable.setImageDrawable(context.getResources().getDrawable(show? R.drawable.ic_action_expand_less_black : R.drawable.ic_action_expand_more_black));
            recyclerViewSubCategorias.setVisibility(show? View.VISIBLE:View.GONE);
            inShowSubCategoria = show;
        }
     }

    /**
     * Interfaz para realizar el callback al click de una categoria
     */
    public interface OnCategoriaClickListener{
        void onClick(View v,Categoria categoria);
    }
}
