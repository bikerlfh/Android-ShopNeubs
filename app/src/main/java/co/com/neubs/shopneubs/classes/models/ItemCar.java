package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.interfaces.ICrud;
import co.com.neubs.shopneubs.models.ItemCarModel;

/**
 * Created by bikerlfh on 6/7/17.
 */

public class ItemCar implements ICrud {

    private transient long idItemCar;
    private transient String fecha;
    private int idSaldoInventario;
    private transient String nombreProducto;
    private transient int idMarca;
    private transient String image;
    private int cantidad;
    private transient float precioVentaUnitario;


    private transient Marca marca;
    private transient DbManager dbManager = DbManager.getInstance();

    public ItemCar() {
    }

    public long getIdItemCar() {
        return idItemCar;
    }

    public void setIdItemCar(long idItemCar) {
        this.idItemCar = idItemCar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdSaldoInventario() {
        return idSaldoInventario;
    }

    public void setIdSaldoInventario(int idSaldoInventario) {
        this.idSaldoInventario = idSaldoInventario;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecioVentaUnitario() {
        return precioVentaUnitario;
    }

    public void setPrecioVentaUnitario(float precioVentaUnitario) {
        this.precioVentaUnitario = precioVentaUnitario;
    }

    public float getValorTotal(){
        return this.cantidad * this.precioVentaUnitario;
    }

    public Marca getMarca(){
        if (marca == null && idMarca >0){
            marca = new Marca();
            marca.getById(idMarca);
        }
        return marca;
    }

    @Override
    public boolean save() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemCarModel.FECHA,fecha);
        contentValues.put(ItemCarModel.ID_SALDO_INVENTARIO,idSaldoInventario);
        contentValues.put(ItemCarModel.NOMBRE_PRODUCTO,nombreProducto);
        contentValues.put(ItemCarModel.ID_MARCA,idMarca);
        contentValues.put(ItemCarModel.IMAGEN,image);
        contentValues.put(ItemCarModel.CANTIDAD,cantidad);
        contentValues.put(ItemCarModel.PRECIO_VENTA_UNITARIO,precioVentaUnitario);

        if(dbManager.Insert(ItemCarModel.NAME_TABLE,contentValues)) {
            idItemCar = dbManager.getIdentity();
            return true;
        }
        return false;
    }

    /**
     * Solo se puede modificar la fecha, cantidad y precioVentaUnitario
     * @return true si actualiza
     */
    @Override
    public boolean update() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemCarModel.FECHA,fecha);
        contentValues.put(ItemCarModel.CANTIDAD,cantidad);
        contentValues.put(ItemCarModel.PRECIO_VENTA_UNITARIO,precioVentaUnitario);
        return dbManager.Update(ItemCarModel.NAME_TABLE,contentValues,ItemCarModel.PK +"=?",new String[]{String.valueOf(idItemCar)});
    }

    @Override
    public boolean delete(){
        return dbManager.Delete(ItemCarModel.NAME_TABLE, ItemCarModel.PK +"=?",new String[]{String.valueOf(idItemCar)});
    }

    @Override
    public boolean exists() {
        return dbManager.exists(ItemCarModel.NAME_TABLE, ItemCarModel.PK,idItemCar);
    }

    @Override
    public boolean getById(int id) {
        Cursor c = dbManager.Select(ItemCarModel.NAME_TABLE, new String[] { "*" }, ItemCarModel.PK + "=?",new String[] {String.valueOf(id)});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    /**
     * Obtiene todos los productos que estan el carrito.
     * @return null o listado ItemCar
     */
    public ArrayList<ItemCar> getAllItemCar() {
        ArrayList<ItemCar> listShopCar = null;
        Cursor c = dbManager.Select(ItemCarModel.NAME_TABLE, new String[] { "*" });
        if (c.moveToFirst()){
            listShopCar = new ArrayList<>();
            do{
                ItemCar item = new ItemCar();
                item.serialize(c);
                listShopCar.add(item);
            }while (c.moveToNext());
        }
        return listShopCar;
    }


    private void serialize(Cursor c){
        this.idItemCar = c.getLong(c.getColumnIndex(ItemCarModel.PK));
        this.fecha = c.getString(c.getColumnIndex(ItemCarModel.FECHA));
        this.idSaldoInventario = c.getInt(c.getColumnIndex(ItemCarModel.ID_SALDO_INVENTARIO));
        this.nombreProducto = c.getString(c.getColumnIndex(ItemCarModel.NOMBRE_PRODUCTO));
        this.idMarca = c.getInt(c.getColumnIndex(ItemCarModel.ID_MARCA));
        this.image = c.getString(c.getColumnIndex(ItemCarModel.IMAGEN));
        this.cantidad = c.getInt(c.getColumnIndex(ItemCarModel.CANTIDAD));
        this.precioVentaUnitario = c.getFloat(c.getColumnIndex(ItemCarModel.PRECIO_VENTA_UNITARIO));
    }
}
