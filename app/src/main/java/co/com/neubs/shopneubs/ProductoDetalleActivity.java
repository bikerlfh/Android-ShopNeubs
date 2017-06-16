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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.adapters.ViewPagerAdapter;
import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.APIValidations;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.classes.SessionManager;
import co.com.neubs.shopneubs.classes.models.Imagen;
import co.com.neubs.shopneubs.classes.models.ItemCar;
import co.com.neubs.shopneubs.classes.models.Producto;
import co.com.neubs.shopneubs.classes.models.SaldoInventario;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

import static android.view.Gravity.CENTER;

public class ProductoDetalleActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String PARAM_ID_SALDO_INVENTARIO = "idSaldoInventario";
    public final static String PARAM_NOMBRE_PRODUCTO = "nombreProducto";
    public final static String PARAM_PRECIO_OFERTA = "precioOferta";
    public final static String PARAM_PRECIO_VENTA_UNITARIO = "precioValorUnitario";
    public final static String PARAM_ESTADO = "estado";


    private SessionManager sessionManager = SessionManager.getInstance();
    private SaldoInventario saldoInventario;

    private Toolbar toolbar;

    private TextView titleDescripcion,titleEspecificacion, nombreProducto, descripcionProducto, marcaProducto, especificacionProducto, precioProducto, precioAnterior;
    private Button btnAgregarItemCar;

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

        titleDescripcion = (TextView) findViewById(R.id.title_descripcion);
        titleEspecificacion = (TextView) findViewById(R.id.title_especificacion);
        nombreProducto = (TextView) findViewById(R.id.lbl_nombre_producto_detalle);
        marcaProducto = (TextView) findViewById(R.id.lbl_marca_producto_detalle);
        precioProducto = (TextView) findViewById(R.id.lbl_precio_producto_detalle);
        precioAnterior = (TextView) findViewById(R.id.lbl_precio_anterior_detalle);

        precioAnterior.setPaintFlags(precioProducto.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        descripcionProducto = (TextView) findViewById(R.id.lbl_descripcion_producto_detalle);
        especificacionProducto = (TextView) findViewById(R.id.lbl_especificacion_producto_detalle);

        btnAgregarItemCar = (Button) findViewById(R.id.btn_agregar_item_car);
        btnAgregarItemCar.setOnClickListener(this);

        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        Intent intentExtra = getIntent();
        if (intentExtra.getExtras().isEmpty())
            finish();

        final int idSaldoInventario = intentExtra.getExtras().getInt(PARAM_ID_SALDO_INVENTARIO,0);
        final String nomProducto = intentExtra.getExtras().getString(PARAM_NOMBRE_PRODUCTO,"");
        final float precioOferta = intentExtra.getExtras().getFloat(PARAM_PRECIO_OFERTA,0);
        final float precioVentaUnitario = intentExtra.getExtras().getFloat(PARAM_PRECIO_VENTA_UNITARIO,0);
        final boolean estado = intentExtra.getExtras().getBoolean(PARAM_ESTADO,false);

        toolbarLayout.setTitle(nomProducto);
        nombreProducto.setText(nomProducto);

        // Si hay precio oferta, este se debe mostrar como el precio actual
        if (precioOferta>0){
            precioProducto.setText(Helper.MoneyFormat(precioOferta));
            precioAnterior.setText(Helper.MoneyFormat(precioVentaUnitario));
        }
        else {
            precioProducto.setGravity(CENTER);
            precioProducto.setText(Helper.MoneyFormat(precioVentaUnitario));
            precioAnterior.setVisibility(View.GONE);
        }

        if (idSaldoInventario > 0){
            APIRest.Async.get("producto/"+String.valueOf(idSaldoInventario), new IServerCallback() {
                @Override
                public void onSuccess(String json) {
                    saldoInventario = APIRest.serializeObjectFromJson(json, SaldoInventario.class);
                    if (saldoInventario != null){
                        final Producto producto = saldoInventario.getProducto();
                        producto.initDbManager(ProductoDetalleActivity.this);

                        marcaProducto.setText(producto.getMarca().getDescripcion());

                        if (producto.getDescripcion().length() > 0) {
                            descripcionProducto.setText(producto.getDescripcion());
                            especificacionProducto.setText(producto.getEspecificacion());
                        }
                        else{
                            titleDescripcion.setText(R.string.title_specification);
                            descripcionProducto.setText(producto.getEspecificacion());
                        }

                        if (producto.getEspecificacion() == null || producto.getEspecificacion().length() == 0){
                            titleEspecificacion.setVisibility(View.GONE);
                            especificacionProducto.setVisibility(View.GONE);
                        }

                        List<String> images = new ArrayList<>();
                        for (Imagen img: producto.getImagenes()) {
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

    @Override
    public void onClick(View v) {

        // Se agrega el item al carrito
        if (sessionManager.getItemCarByIdSaldoInventario(saldoInventario.getIdSaldoInventario()) == null) {
            ItemCar itemCar = new ItemCar(this);
            itemCar.setNombreProducto(saldoInventario.getProducto().getNombre());
            itemCar.setIdSaldoInventario(saldoInventario.getIdSaldoInventario());
            itemCar.setImage(saldoInventario.getProducto().getImagenes().get(0).getUrl());
            itemCar.setIdMarca(saldoInventario.getProducto().getIdMarca());
            itemCar.setCantidad(1);
            itemCar.setPrecioVentaUnitario(saldoInventario.getPrecioVentaUnitario());
            if (sessionManager.addItemCar(itemCar)) {
                Toast.makeText(this,"Se agregó el item al carro",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"No se agregó el item al carro",Toast.LENGTH_SHORT).show();
            }
        }
        else
            Toast.makeText(this,"EL item ya esta agregado en el carro",Toast.LENGTH_SHORT).show();
    }
}
