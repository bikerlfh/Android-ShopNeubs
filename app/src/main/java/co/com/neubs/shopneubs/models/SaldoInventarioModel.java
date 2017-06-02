package co.com.neubs.shopneubs.models;

/**
 * Created by bikerlfh on 5/22/17.
 */

public class SaldoInventarioModel {
    public static final String NAME_TABLE = "saldoinventario";
    public static final String PK = "idSaldoInventario";
    public static final String ID_PRODUCTO = "idProducto";
    public static final String PRECIO_VENTA_UNITARIO = "precioVentaUnitario";
    public static final String PRECIO_OFERTA = "precioOferta";
    public static final String ESTADO = "estado";


    public static final String CREATE_TABLE = "create table " + NAME_TABLE + "("+
            PK + " integer primary key,"+
            ID_PRODUCTO + " integer not null,"+
            PRECIO_VENTA_UNITARIO + " real not null,"+
            PRECIO_OFERTA + " real null,"+
            ESTADO + " int not null)";
}
