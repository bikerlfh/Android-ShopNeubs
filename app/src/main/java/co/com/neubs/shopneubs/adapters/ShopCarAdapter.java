package co.com.neubs.shopneubs.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.ShopCarActivity;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.classes.SessionManager;
import co.com.neubs.shopneubs.classes.models.ItemCar;
import co.com.neubs.shopneubs.classes.models.Marca;
import co.com.neubs.shopneubs.controls.ImageLoaderView;

/**
 * Created by bikerlfh on 6/16/17.
 */

public class ShopCarAdapter extends RecyclerView.Adapter<ShopCarAdapter.ShopCarViewHolder> {

    private SessionManager sessionManager = SessionManager.getInstance();
    private ArrayList<ItemCar> listadoItemCar = sessionManager.getShopCar();
    private Context context;

    public ShopCarAdapter(Context context){
        this.context = context;
    }

    @Override
    public ShopCarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_shop_car,null);
        return new ShopCarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShopCarViewHolder holder, int position) {
        final ItemCar itemCar = listadoItemCar.get(position);
        holder.setOnClickListenerDeleteItem(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // button eliminar
                if (sessionManager.deleteItemShopCar(itemCar)){
                    notifyItemRemoved(holder.getAdapterPosition());
                    Toast.makeText(context,context.getString(R.string.title_item_deleted),Toast.LENGTH_SHORT).show();
                    ShopCarActivity shopCarActivity = (ShopCarActivity)context;
                    shopCarActivity.calcularValorTotal();
                }
                else
                    Toast.makeText(context,context.getString(R.string.error_default),Toast.LENGTH_SHORT).show();
            }
        });
        holder.bindProducto(itemCar);
    }

    @Override
    public int getItemCount() {
        return listadoItemCar.size();
    }


    public static class ShopCarViewHolder extends RecyclerView.ViewHolder{


        private ImageLoaderView imagen;
        private TextView lblNombreProducto,lblValorTotalItemCard,lblMarca;
        private ImageButton btnEliminar;

        private View.OnClickListener onClickListenerDeleteItem;

        public ShopCarViewHolder(View itemView) {
            super(itemView);
            imagen = (ImageLoaderView) itemView.findViewById(R.id.img_producto_card_shop_card);
            lblNombreProducto= (TextView) itemView.findViewById(R.id.lbl_nombre_producto_card_shop_car);
            lblValorTotalItemCard = (TextView) itemView.findViewById(R.id.lbl_valor_total_item_card_shop_card);
            lblMarca = (TextView) itemView.findViewById(R.id.lbl_marca_card_shop_car);
            btnEliminar = (ImageButton) itemView.findViewById(R.id.img_btn_eliminar_item_car);
        }

        public void bindProducto(ItemCar itemCar) {
            lblNombreProducto.setText(itemCar.getNombreProducto());
            lblValorTotalItemCard.setText(Helper.MoneyFormat(itemCar.getValorTotal()));
            final Marca marca = itemCar.getMarca();
            if (marca != null)
                lblMarca.setText(marca.getDescripcion());
            else
                lblMarca.setVisibility(View.GONE);

            final String urlImagen = itemCar.getImage();
            if (urlImagen != null && !urlImagen.isEmpty())
                imagen.setImageURL(urlImagen);

            // Se asigna el listener al boton eliminar.
            btnEliminar.setOnClickListener(this.onClickListenerDeleteItem);
        }

        /**
         * Asigna el OnClickListener cuando se da click en el boton de eliminar item
         * @param onClickListener
         */
        public void setOnClickListenerDeleteItem(View.OnClickListener onClickListener){
            this.onClickListenerDeleteItem = onClickListener;
        }
    }
}
