package co.com.neubs.shopneubs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.models.Imagen;
import co.com.neubs.shopneubs.classes.models.Producto;
import co.com.neubs.shopneubs.classes.models.SaldoInventario;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

public class ProductoDetalleActivity extends AppCompatActivity {

    public final static String PARAM_ID_SALDO_INVENTARIO = "idSaldoInventario";
    private TextView title_descripcion;
    private TextView nombreProducto;
    private TextView descripcionProducto;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_detalle);
        Intent intentExtra = getIntent();

        title_descripcion = (TextView) findViewById(R.id.title_descripcion);
        nombreProducto = (TextView) findViewById(R.id.txt_nombre_producto_detalle);
        descripcionProducto = (TextView) findViewById(R.id.txt_descripcion_producto_detalle);
        imageView = (ImageView) findViewById(R.id.img_producto_detalle);
        if (intentExtra.getExtras().isEmpty())
            finish();

        int idSaldoInventario = intentExtra.getExtras().getInt(PARAM_ID_SALDO_INVENTARIO,0);

        if (idSaldoInventario > 0){
            APIRest.Async.get("producto/"+String.valueOf(idSaldoInventario), new IServerCallback() {
                @Override
                public void onSuccess(String json) {
                    final SaldoInventario saldoInventario = APIRest.serializeObjectFromJson(json, SaldoInventario.class);
                    if (saldoInventario != null){
                        final Producto producto = saldoInventario.getProducto();
                        nombreProducto.setText(producto.getNombre());
                        if (producto.getDescripcion().length() > 0)
                            descripcionProducto.setText(producto.getDescripcion());
                        else{
                            title_descripcion.setText(R.string.title_specification);
                            descripcionProducto.setText(producto.getEspecificacion());
                        }

                        final ArrayList<Imagen> listadoImagenes = producto.getImagenes();
                        // Se obtiene la imagen y se guarda en el cache
                        Glide.with(ProductoDetalleActivity.this).load(listadoImagenes.get(0).getUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

                    }

                }

                @Override
                public void onError(String message_error) {
                    Toast.makeText(ProductoDetalleActivity.this,message_error,Toast.LENGTH_SHORT).show();
                }
            });

        }


    }
}
