package co.com.neubs.shopneubs.classes.models;

import android.content.Context;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.models.SaldoInventarioModel;

/**
 * Created by bikerlfh on 5/22/17.
 */

public class SaldoInventario {
    @SerializedName("pk")
    private int idSaldoInventario;
    @SerializedName("producto")
    private Producto producto;
    private float precioVentaUnitario;
    private float precioOferta;
    private boolean estado;


    private transient DbManager dbManager;

    public SaldoInventario(Context context){
        this.dbManager = new DbManager(context);
    }

    public int getIdSaldoInventario() {
        return idSaldoInventario;
    }

    public void setIdSaldoInventario(int idSaldoInventario) {
        this.idSaldoInventario = idSaldoInventario;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public float getPrecioVentaUnitario() {
        return precioVentaUnitario;
    }

    public void setPrecioVentaUnitario(float precioVentaUnitario) {
        this.precioVentaUnitario = precioVentaUnitario;
    }

    public float getPrecioOferta() {
        return precioOferta;
    }

    public void setPrecioOferta(float precioOferta) {
        this.precioOferta = precioOferta;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public boolean getSaldoInventarioyid(int idSaldoInventario){
        Cursor c = dbManager.Select(SaldoInventarioModel.NAME_TABLE, new String[] { "*" },SaldoInventarioModel.PK + "=?",new String[] {String.valueOf(idSaldoInventario)},null,null,null,null);
        if (c.moveToFirst())
        {
            serializeSaldoInventario(c);
            return true;
        }
        return false;
    }

    private void serializeSaldoInventario(Cursor c){
        this.idSaldoInventario = c.getInt(c.getColumnIndex(SaldoInventarioModel.PK));
        this.precioVentaUnitario = c.getFloat(c.getColumnIndex(SaldoInventarioModel.PRECIO_VENTA_UNITARIO));
        this.precioOferta = c.getFloat(c.getColumnIndex(SaldoInventarioModel.PRECIO_OFERTA));
        this.estado = Boolean.parseBoolean(c.getString(c.getColumnIndex(SaldoInventarioModel.ESTADO)));
        int idProducto = c.getInt(c.getColumnIndex(SaldoInventarioModel.ID_PRODUCTO));
        if (idProducto > 0){
            //this.producto = new Producto(this.dbManager.context);
            //this.producto.getProductoByid(idProducto);
        }
    }
}
