package co.com.neubs.shopneubs.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.com.neubs.shopneubs.PedidoDetalleActivity;
import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.classes.models.PedidoVenta;

/**
 * Created by bikerlfh on 6/12/17.
 */

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {

    private PedidoVenta[] listadoPedidoVenta;

    private Context context;
    public PedidoAdapter(Context context ,PedidoVenta[] listadoPedidoVenta) {
        this.listadoPedidoVenta = listadoPedidoVenta;
    }

    @Override
    public PedidoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_pedido,null);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PedidoViewHolder holder, int position) {
        PedidoVenta pedidoVenta = listadoPedidoVenta[position];
        holder.bindPedido(pedidoVenta);
    }

    @Override
    public int getItemCount() {
        return listadoPedidoVenta.length;
    }

    public static class PedidoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView lblNumeroPedido;
        private TextView lblFecha;
        private TextView lblEstado;
        private TextView lblValorTotal;
        private TextView lblNumeroProductos;

        private PedidoVenta pedidoVenta;

        private Context context;
        public PedidoViewHolder(View itemView) {
            super(itemView);

            lblNumeroPedido = (TextView)itemView.findViewById(R.id.lbl_numero_pedido);
            lblEstado = (TextView) itemView.findViewById(R.id.lbl_estado);
            lblFecha = (TextView)itemView.findViewById(R.id.lbl_fecha);
            lblValorTotal = (TextView)itemView.findViewById(R.id.lbl_valor_total);
            lblNumeroProductos = (TextView)itemView.findViewById(R.id.lbl_numero_productos);
            context = itemView.getContext();
        }

        public void bindPedido(PedidoVenta pedidoVenta) {
            this.pedidoVenta = pedidoVenta;
            lblNumeroPedido.setText(String.valueOf(pedidoVenta.getNumeroPedido()));
            lblEstado.setText(pedidoVenta.getEstado());
            lblFecha.setText(pedidoVenta.getFecha());
            lblNumeroProductos.setText(String.valueOf(pedidoVenta.getNumeroProductos()));
            lblValorTotal.setText(Helper.MoneyFormat(pedidoVenta.getValorTotal()));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Aca se debe abrir la actividad de visualización del producto detalle
            //Toast.makeText(v.getContext(),"position " + getLayoutPosition(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(),PedidoDetalleActivity.class);
            intent.putExtra(PedidoDetalleActivity.PARAM_URL,this.pedidoVenta.getDetalle());
            v.getContext().startActivity(intent);
        }
    }
}
