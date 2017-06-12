package co.com.neubs.shopneubs.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.classes.models.Pedido;

/**
 * Created by bikerlfh on 6/12/17.
 */

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {

    private Pedido[] listadoPedido;

    private Context context;
    public PedidoAdapter(Context context ,Pedido[] listadoPedido) {
        this.listadoPedido = listadoPedido;
    }

    @Override
    public PedidoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_pedido,null);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PedidoViewHolder holder, int position) {
        Pedido pedido = listadoPedido[position];
        holder.bindPedido(pedido);
    }

    @Override
    public int getItemCount() {
        return listadoPedido.length;
    }

    public static class PedidoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView lblNumeroPedido;
        private TextView lblFecha;
        private TextView lblEstado;
        private TextView lblValorTotal;
        private TextView lblNumeroProductos;

        private Pedido pedido;

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

        public void bindPedido(Pedido pedido) {
            this.pedido = pedido;
            lblNumeroPedido.setText(String.valueOf(pedido.getNumeroPedido()));
            lblEstado.setText(pedido.getEstado());
            lblFecha.setText(pedido.getFecha());
            lblNumeroProductos.setText(String.valueOf(pedido.getNumeroProductos()));
            lblValorTotal.setText(Helper.MoneyFormat(pedido.getValorTotal()));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Aca se debe abrir la actividad de visualización del producto detalle
            //Toast.makeText(v.getContext(),"position " + getLayoutPosition(), Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(v.getContext(),ProductoDetalleActivity.class);
            //intent.putExtra(ProductoDetalleActivity.PARAM_ID_SALDO_INVENTARIO,this.saldoInventario.getIdSaldoInventario());
            //v.getContext().startActivity(intent);
        }
    }
}
