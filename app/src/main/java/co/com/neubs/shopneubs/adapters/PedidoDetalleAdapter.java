package co.com.neubs.shopneubs.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import co.com.neubs.shopneubs.ProductoDetalleActivity;
import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.classes.models.PedidoVentaPosicion;
import co.com.neubs.shopneubs.classes.models.Producto;
import co.com.neubs.shopneubs.controls.ImageLoaderView;

/**
 * Created by bikerlfh on 6/15/17.
 */

public class PedidoDetalleAdapter extends RecyclerView.Adapter<PedidoDetalleAdapter.PedidoDetalleViewHolder> {

    private ArrayList<PedidoVentaPosicion> listadoPedidoVentaPosicion;
    private Context context;
    private int idResourceLayoutToInflate;

    public PedidoDetalleAdapter(Context context,ArrayList<PedidoVentaPosicion> listadoPedidoVentaPosicion) {
        this.listadoPedidoVentaPosicion = listadoPedidoVentaPosicion;
        this.idResourceLayoutToInflate = R.layout.cardview_pedido_detalle;
    }

    public PedidoDetalleAdapter(Context context,ArrayList<PedidoVentaPosicion> listadoPedidoVentaPosicion,int idResourceLayoutToInflate) {
        this.listadoPedidoVentaPosicion = listadoPedidoVentaPosicion;
        this.idResourceLayoutToInflate = idResourceLayoutToInflate;
    }

    @Override
    public PedidoDetalleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate layout CartView
        View view = LayoutInflater.from(parent.getContext()).inflate(this.idResourceLayoutToInflate,null);
        return new PedidoDetalleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PedidoDetalleViewHolder holder, int position) {
        PedidoVentaPosicion pedidoVentaPosicion = listadoPedidoVentaPosicion.get(position);
        holder.bindPedidoVentaPosicion(pedidoVentaPosicion);
    }

    @Override
    public int getItemCount() {
        return listadoPedidoVentaPosicion.size();
    }


    class PedidoDetalleViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txtNombreProducto,txtCantidad,txtCostoTotal;
        private ImageLoaderView imagenView;
        private Context context;
        private PedidoVentaPosicion pedidoVentaPosicion;

        public PedidoDetalleViewHolder(View itemView) {
            super(itemView);

            txtNombreProducto = (TextView) itemView.findViewById(R.id.lbl_nombre_producto_pedido_detalle);
            txtCantidad = (TextView) itemView.findViewById(R.id.lbl_cantidad_pedido_detalle);
            txtCostoTotal = (TextView) itemView.findViewById(R.id.lbl_costo_total_pedido_detalle);
            imagenView = (ImageLoaderView) itemView.findViewById(R.id.img_pedido_detalle);
            context = itemView.getContext();

            itemView.setOnClickListener(this);
        }

        public void bindPedidoVentaPosicion(PedidoVentaPosicion pedidoVentaPosicion) {
            this.pedidoVentaPosicion = pedidoVentaPosicion;
            Producto producto = pedidoVentaPosicion.getProducto();
            txtNombreProducto.setText(producto.getNombre());
            txtCantidad.setText(context.getString(R.string.title_num_products) + " ("+ String.valueOf(pedidoVentaPosicion.getCantidad())+")");
            txtCostoTotal.setText(Helper.MoneyFormat(pedidoVentaPosicion.getCostoTotal()));
            imagenView.setImageURL(producto.getImagen());



        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(),ProductoDetalleActivity.class);
            intent.putExtra(ProductoDetalleActivity.PARAM_ID_SALDO_INVENTARIO,this.pedidoVentaPosicion.getIdSaldoInventario());
            intent.putExtra(ProductoDetalleActivity.PARAM_NOMBRE_PRODUCTO,this.pedidoVentaPosicion.getProducto().getNombre());
            v.getContext().startActivity(intent);
        }
    }


}
