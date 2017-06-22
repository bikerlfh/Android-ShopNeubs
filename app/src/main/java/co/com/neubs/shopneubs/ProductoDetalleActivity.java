package co.com.neubs.shopneubs;

import android.content.Intent;
import android.graphics.Paint;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
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


    private SessionManager sessionManager;
    private SaldoInventario saldoInventario;

    private Toolbar toolbar;

    private TextView mTitleDescripcion,mTitleEspecificacion, mNombreProducto, mDescripcionProducto, mMarcaProducto, mEspecificacionProducto, mPrecioProducto, mPrecioAnterior;
    private Button mBtnAgregarItemCar;

    private CollapsingToolbarLayout mToolbarLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_detalle);
        toolbar = (Toolbar) findViewById(R.id.toolbar_producto_detalle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = SessionManager.getInstance(this);
        mViewPager = (ViewPager)findViewById(R.id.viewPager_producto_detalle);

        mTitleDescripcion = (TextView) findViewById(R.id.title_descripcion);
        mTitleEspecificacion = (TextView) findViewById(R.id.title_especificacion);
        mNombreProducto = (TextView) findViewById(R.id.lbl_nombre_producto_detalle);
        mMarcaProducto = (TextView) findViewById(R.id.lbl_marca_producto_detalle);
        mPrecioProducto = (TextView) findViewById(R.id.lbl_precio_producto_detalle);
        mPrecioAnterior = (TextView) findViewById(R.id.lbl_precio_anterior_detalle);

        mPrecioAnterior.setPaintFlags(mPrecioProducto.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        mDescripcionProducto = (TextView) findViewById(R.id.lbl_descripcion_producto_detalle);
        mEspecificacionProducto = (TextView) findViewById(R.id.lbl_especificacion_producto_detalle);

        mBtnAgregarItemCar = (Button) findViewById(R.id.btn_agregar_item_car);

        mBtnAgregarItemCar.setEnabled(false);
        mBtnAgregarItemCar.setOnClickListener(this);

        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        Intent intentExtra = getIntent();
        if (intentExtra.getExtras().isEmpty())
            finish();

        final int idSaldoInventario = intentExtra.getExtras().getInt(PARAM_ID_SALDO_INVENTARIO,0);
        final String nomProducto = intentExtra.getExtras().getString(PARAM_NOMBRE_PRODUCTO,"");
        final float precioOferta = intentExtra.getExtras().getFloat(PARAM_PRECIO_OFERTA,0);
        final float precioVentaUnitario = intentExtra.getExtras().getFloat(PARAM_PRECIO_VENTA_UNITARIO,0);
        final boolean estado = intentExtra.getExtras().getBoolean(PARAM_ESTADO,false);

        if (estado)
            mBtnAgregarItemCar.setEnabled(true);
        mToolbarLayout.setTitle(nomProducto);
        mNombreProducto.setText(nomProducto);

        // SE asignan los precios
        setPrecio(precioVentaUnitario,precioOferta);

        if (idSaldoInventario > 0){
            APIRest.Async.get(APIRest.URL_PRODUCTO_DETALLE + String.valueOf(idSaldoInventario), new IServerCallback() {
                @Override
                public void onSuccess(String json) {
                    saldoInventario = APIRest.serializeObjectFromJson(json, SaldoInventario.class);
                    if (saldoInventario != null){
                        final Producto producto = saldoInventario.getProducto();
                        producto.initDbManager(ProductoDetalleActivity.this);

                        mMarcaProducto.setText(producto.getMarca().getDescripcion());

                        if (producto.getDescripcion().length() > 0) {
                            mDescripcionProducto.setText(producto.getDescripcion());
                            mEspecificacionProducto.setText(producto.getEspecificacion());
                        }
                        else if(producto.getEspecificacion().length() > 0){

                            mTitleDescripcion.setText(R.string.title_specification);
                            mDescripcionProducto.setText(producto.getEspecificacion());
                            // Se esconden los campos de especificacion
                            setVisibilityCamposEspecificacion(View.GONE);
                        }
                        else{
                            mDescripcionProducto.setText(getString(R.string.text_view_empy));
                            // Se esconden los campos de especificacion
                            setVisibilityCamposEspecificacion(View.GONE);
                        }
                        // Si no hay especificación, se esconden los respectivos campos
                        if (producto.getEspecificacion() == null || producto.getEspecificacion().length() == 0){
                            setVisibilityCamposEspecificacion(View.GONE);
                        }

                        // Se validan los precios que se reciben del servidor con los que se pasaron a la actividad
                        if (precioVentaUnitario != saldoInventario.getPrecioVentaUnitario() ||
                            precioOferta != saldoInventario.getPrecioOferta()){
                            setPrecio(saldoInventario.getPrecioVentaUnitario(),saldoInventario.getPrecioOferta());
                        }
                        // Se cambia el estado
                        if (saldoInventario.getEstado())
                            mBtnAgregarItemCar.setEnabled(true);

                        List<String> images = new ArrayList<>();
                        for (Imagen img: producto.getImagenes()) {
                            images.add(img.getUrl());
                        }
                        viewPagerAdapter = new ViewPagerAdapter(ProductoDetalleActivity.this,images);
                        mViewPager.setAdapter(viewPagerAdapter);
                    }
                }

                @Override
                public void onError(String message_error, APIValidations apiValidations) {
                    Toast.makeText(ProductoDetalleActivity.this,message_error,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Asigna el precio a los campos
     * @param precioVentaUnitario precio normal
     * @param precioOferta precio de oferta
     */
    private void setPrecio(float precioVentaUnitario,float precioOferta){
        // Si hay precio oferta, este se debe mostrar como el precio actual
        if (precioOferta > 0){
            mPrecioProducto.setText(Helper.MoneyFormat(precioOferta));
            mPrecioAnterior.setText(Helper.MoneyFormat(precioVentaUnitario));
        }
        else {
            mPrecioProducto.setGravity(CENTER);
            mPrecioProducto.setText(Helper.MoneyFormat(precioVentaUnitario));
            mPrecioAnterior.setVisibility(View.GONE);
        }
    }

    /**
     * Cambia la visibilidad de los campos titleEspecificacion y especificacionProducto
     * @param value View.GONE, View.VISIBLE
     */
    private void setVisibilityCamposEspecificacion(int value){
        mTitleEspecificacion.setVisibility(value);
        mEspecificacionProducto.setVisibility(value);
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
            if (saldoInventario.getPrecioOferta() > 0)
                itemCar.setPrecioVentaUnitario(saldoInventario.getPrecioOferta());
            else
                itemCar.setPrecioVentaUnitario(saldoInventario.getPrecioVentaUnitario());
            if (sessionManager.addItemCar(itemCar)) {
                Toast.makeText(this,getString(R.string.title_item_added),Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,getString(R.string.error_default),Toast.LENGTH_SHORT).show();
            }
        }
        else
            Toast.makeText(this,getString(R.string.error_item_in_car),Toast.LENGTH_SHORT).show();
    }
}
