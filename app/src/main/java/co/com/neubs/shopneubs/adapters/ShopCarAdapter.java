package co.com.neubs.shopneubs.adapters;

import android.content.Context;
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
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.classes.SessionManager;
import co.com.neubs.shopneubs.classes.models.ItemCar;
import co.com.neubs.shopneubs.classes.models.Marca;
import co.com.neubs.shopneubs.controls.ImageLoaderView;

/**
 * Created by bikerlfh on 6/16/17.
 */

public class ShopCarAdapter extends RecyclerView.Adapter<ShopCarAdapter.ShopCarViewHolder> {

    private ArrayList<ItemCar> listadoItemCar = SessionManager.getInstance().getShopCar();
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
    public void onBindViewHolder(ShopCarViewHolder holder, int position) {
        ItemCar itemCar = listadoItemCar.get(position);
        holder.bindProducto(itemCar);
    }

    @Override
    public int getItemCount() {
        return listadoItemCar.size();
    }


    public static class ShopCarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private SessionManager sessionManager = SessionManager.getInstance();
        private ImageLoaderView imagen;
        private TextView lblNombreProducto,lblValorTotalItemCard,lblMarca;
        private ImageButton btnEliminar;

        private ItemCar itemCar;

        public ShopCarViewHolder(View itemView) {
            super(itemView);
            imagen = (ImageLoaderView) itemView.findViewById(R.id.img_producto_card_shop_card);
            lblNombreProducto= (TextView) itemView.findViewById(R.id.lbl_nombre_producto_card_shop_car);
            lblValorTotalItemCard = (TextView) itemView.findViewById(R.id.lbl_valor_total_item_card_shop_card);
            lblMarca = (TextView) itemView.findViewById(R.id.lbl_marca_card_shop_car);
            btnEliminar = (ImageButton) itemView.findViewById(R.id.img_btn_eliminar_item_car);
        }

        public void bindProducto(ItemCar itemCar) {
            this.itemCar = itemCar;
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

            btnEliminar.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // button eliminar
            if (v.getId() == btnEliminar.getId()){
                if (sessionManager.deleteItemShopCar(itemCar)){
                    Toast.makeText(v.getContext(),"Item eliminado",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(v.getContext(),"No se pudo eliminar el item",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
