package co.com.neubs.shopneubs.models;

/**
 * Created by bikerlfh on 6/7/17.
 * Guarda los detalles del carrito antes de enviar el pedido
 * cuando el pedido es enviado, la información de esta tabla deberá ser eliminada
 */

public class ItemCarModel {

    public static final String NAME_TABLE = "ItemCar";
    public static final String PK = "idItemCar";
    public static final String FECHA = "fecha";
    public static final String ID_SALDO_INVENTARIO = "idSaldoInventario";
    public static final String CANTIDAD = "cantidad";
    public static final String PRECIO_VENTA_UNITARIO = "precioVentaUnitario";


    public static final String CREATE_TABLE = "create table " + NAME_TABLE + "("+
            PK + " integer primary key,"+
            FECHA + " text not null,"+
            ID_SALDO_INVENTARIO + " int not null," +
            CANTIDAD + " int not null,"+
            PRECIO_VENTA_UNITARIO + " real not null)";

}
