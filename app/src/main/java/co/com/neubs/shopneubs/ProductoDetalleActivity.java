package co.com.neubs.shopneubs;

import android.content.Intent;
import android.graphics.Paint;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.adapters.ViewPagerAdapter;
import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.APIValidations;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.classes.models.Imagen;
import co.com.neubs.shopneubs.classes.models.Producto;
import co.com.neubs.shopneubs.classes.models.SaldoInventario;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

import static android.view.Gravity.CENTER;

public class ProductoDetalleActivity extends AppCompatActivity {

    public final static String PARAM_ID_SALDO_INVENTARIO = "idSaldoInventario";

    private Toolbar toolbar;

    private TextView title_descripcion, nombreProducto, descripcionProducto, marcaProducto, especificacionProducto, precioProducto, precioAnterior;

    private CollapsingToolbarLayout toolbarLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_detalle);
        toolbar = (Toolbar) findViewById(R.id.toolbar_producto_detalle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager)findViewById(R.id.viewPager_producto_detalle);

        title_descripcion = (TextView) findViewById(R.id.title_descripcion);
        nombreProducto = (TextView) findViewById(R.id.lbl_nombre_producto_detalle);
        marcaProducto = (TextView) findViewById(R.id.lbl_marca_producto_detalle);
        precioProducto = (TextView) findViewById(R.id.lbl_precio_producto_detalle);
        precioAnterior = (TextView) findViewById(R.id.lbl_precio_anterior_detalle);

        precioAnterior.setPaintFlags(precioProducto.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        descripcionProducto = (TextView) findViewById(R.id.lbl_descripcion_producto_detalle);
        especificacionProducto = (TextView) findViewById(R.id.lbl_especificacion_producto_detalle);

        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        Intent intentExtra = getIntent();
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
                        producto.initDbManager(ProductoDetalleActivity.this);

                        toolbarLayout.setTitle(producto.getNombre());
                        nombreProducto.setText(producto.getNombre());
                        marcaProducto.setText(producto.getMarca().getDescripcion());


                        if (saldoInventario.getPrecioOferta()>0){
                            precioProducto.setText(Helper.MoneyFormat(saldoInventario.getPrecioOferta()));
                            precioAnterior.setText(Helper.MoneyFormat(saldoInventario.getPrecioVentaUnitario()));
                        }
                        else {
                            precioProducto.setGravity(CENTER);
                            precioProducto.setText(Helper.MoneyFormat(saldoInventario.getPrecioVentaUnitario()));
                            precioAnterior.setVisibility(View.GONE);

                        }
                        if (producto.getDescripcion().length() > 0) {
                            descripcionProducto.setText(producto.getDescripcion());
                            especificacionProducto.setText(producto.getEspecificacion());
                        }
                        else{
                            title_descripcion.setText(R.string.title_specification);
                            descripcionProducto.setText(producto.getEspecificacion());
                        }

                        final ArrayList<Imagen> listadoImagenes = producto.getImagenes();
                        // Se obtiene la imagen y se guarda en el cache
                        //Glide.with(ProductoDetalleActivity.this).load(listadoImagenes.get(0).getUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.circular_progress_bar).into(imageView);
                        List<String> images = new ArrayList<>();
                        for (Imagen img: listadoImagenes) {
                            images.add(img.getUrl());
                        }
                        viewPagerAdapter = new ViewPagerAdapter(ProductoDetalleActivity.this,images);
                        viewPager.setAdapter(viewPagerAdapter);
                    }
                }

                @Override
                public void onError(String message_error, APIValidations apiValidations) {
                    Toast.makeText(ProductoDetalleActivity.this,message_error,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        return true;
    }
}
