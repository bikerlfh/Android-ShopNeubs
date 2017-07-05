package co.com.neubs.shopneubs.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import co.com.neubs.shopneubs.PrincipalActivity;
import co.com.neubs.shopneubs.ProductoDetalleActivity;
import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.APIValidations;
import co.com.neubs.shopneubs.classes.ConsultaPaginada;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.classes.models.Producto;
import co.com.neubs.shopneubs.classes.models.SaldoInventario;
import co.com.neubs.shopneubs.controls.ImageLoaderView;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

/**
 * Created by bikerlfh on 5/24/17.
 * Adapter from view products (SaldoInventario)
 */


public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {
    private final String TAG = "ProductoAdapter";
    private ArrayList<SaldoInventario> listado_saldo_inventario;
    private ConsultaPaginada consultaPaginada;
    private String nextPage;
    private Context context;

    /**
     * ID del recurso XML Layout a cargar desde LayoutInflater
     */
    protected int idResourceLayoutToInflate;

    /**
     * Constructor
     * @param context
     * @param consultaPaginada
     * @param idResourceLayoutToInflate ID del recurso XML Layout a cargar desde LayoutInflater
     */
    public  ProductoAdapter(Context context, ConsultaPaginada consultaPaginada,int idResourceLayoutToInflate){
        this.context = context;
        this.consultaPaginada = consultaPaginada;
        this.listado_saldo_inventario = this.consultaPaginada.getResults();
        this.nextPage = this.consultaPaginada.getNext();
        this.idResourceLayoutToInflate = idResourceLayoutToInflate;
    }

    @Override
    public ProductoViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // inflate layout
        View view = LayoutInflater.from(parent.getContext()).inflate(this.idResourceLayoutToInflate ,null);
        return new ProductoViewHolder(view,idResourceLayoutToInflate == R.layout.cardview_producto_list);
    }

    @Override
    public void onBindViewHolder(ProductoViewHolder holder, final int position) {
        SaldoInventario saldoInventario = listado_saldo_inventario.get(position);
        holder.bindProducto(saldoInventario);
    }

    @Override
    public int getItemCount() {
        return listado_saldo_inventario.size();
    }

    private void addItems(int position,ArrayList<SaldoInventario> listadoSaldoInventario){
        listado_saldo_inventario.addAll(listadoSaldoInventario);
        notifyItemRangeInserted(position,listadoSaldoInventario.size());
    }

    /**
     * Consulta la siguiente pagina si existe
     * Este metodo debe ser llamado desde el evento OnScrollListener (onScrolledToBottom)
     */
    public void getNextPage(View view){
        if (this.nextPage!= null && this.nextPage.length() > 0){

            final Snackbar snackbar = Snackbar.make(view, R.string.text_loading, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Action", null);
            snackbar.show();
            // Se consulta a la api la proxima pagina
            APIRest.Async.get(this.nextPage, new IServerCallback() {
                @Override
                public void onSuccess(String json) {
                    ConsultaPaginada cPaginada = APIRest.serializeObjectFromJson(json,ConsultaPaginada.class);
                    addItems(getItemCount(),cPaginada.getResults());
                    // Se modifica nextPage asignandole la nueva (pagina siguiente)
                    nextPage = cPaginada.getNext();
                    snackbar.dismiss();
                }

                @Override
                public void onError(String message_error, APIValidations apiValidations) {
                    Log.d(TAG,"ERROR: " + message_error);
                    snackbar.dismiss();
                }
            });
        }
    }

    // The class viewHolder
    public static class ProductoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageLoaderView imagen;
        private TextView nombre_producto;
        private TextView precio;
        private TextView oferta;
        private TextView lblSinStok;

        private SaldoInventario saldoInventario;

        private boolean showAddCart;

        public ProductoViewHolder(View itemView,boolean showAddCart) {
            super(itemView);
            imagen = (ImageLoaderView)itemView.findViewById(R.id.img_producto);
            nombre_producto = (TextView)itemView.findViewById(R.id.lbl_nombre_producto_card);
            precio = (TextView)itemView.findViewById(R.id.lbl_precio_producto_card);
            oferta = (TextView)itemView.findViewById(R.id.lbl_precio_anterior_card);
            lblSinStok = (TextView) itemView.findViewById(R.id.lbl_producto_sin_stock);
            //Estilo Texto strikethrough
            oferta.setPaintFlags(precio.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            this.showAddCart = showAddCart;
        }

        public void bindProducto(final SaldoInventario saldoInventario) {
            this.saldoInventario = saldoInventario;
            Producto producto = saldoInventario.getProducto();
            nombre_producto.setText(producto.getNombre());

            if (saldoInventario.getPrecioOferta() > 0){
                oferta.setText(Helper.MoneyFormat(saldoInventario.getPrecioVentaUnitario()));
                precio.setText(Helper.MoneyFormat(saldoInventario.getPrecioOferta()));
                oferta.setVisibility(View.VISIBLE);
            }
            else{
                precio.setText(Helper.MoneyFormat(saldoInventario.getPrecioVentaUnitario()));
                oferta.setVisibility(View.GONE);
            }
            // si esta activo y es llamado desde el vista en lista
            // se visualiza  para agregar al carro
            if (saldoInventario.getEstado()) {
                if (showAddCart) {
                    lblSinStok.setVisibility(View.VISIBLE);
                    //lblSinStok.setBackgroundColor(itemView.getResources().getColor(R.color.colorAccent));
                    lblSinStok.setBackground(itemView.getResources().getDrawable(R.drawable.boton_add_cart_view_list));
                    lblSinStok.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_shopping_cart_list,0,0,0);
                    lblSinStok.setCompoundDrawablePadding(5);
                    lblSinStok.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    lblSinStok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((PrincipalActivity) v.getContext()).agregarItemCart(saldoInventario);
                        }
                    });
                }
                else
                    lblSinStok.setVisibility(View.GONE);
            }
            else {
                lblSinStok.setVisibility(View.VISIBLE);
                lblSinStok.setBackground(itemView.getResources().getDrawable(R.drawable.boton_sin_stock_view_list));
                lblSinStok.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }

            // Se obtiene la imagen y se guarda en el cache
            imagen.setImageURL(producto.getImagen());

            imagen.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            // Aca se debe abrir la actividad de visualización del producto detalle
            Intent intent = new Intent(v.getContext(),ProductoDetalleActivity.class);
            intent.putExtra(ProductoDetalleActivity.PARAM_ID_SALDO_INVENTARIO,this.saldoInventario.getIdSaldoInventario());
            intent.putExtra(ProductoDetalleActivity.PARAM_NOMBRE_PRODUCTO,this.saldoInventario.getProducto().getNombre());
            intent.putExtra(ProductoDetalleActivity.PARAM_PRECIO_OFERTA,this.saldoInventario.getPrecioOferta());
            intent.putExtra(ProductoDetalleActivity.PARAM_PRECIO_VENTA_UNITARIO,this.saldoInventario.getPrecioVentaUnitario());
            intent.putExtra(ProductoDetalleActivity.PARAM_ESTADO,this.saldoInventario.getEstado());
            v.getContext().startActivity(intent);
        }
    }
}
