package co.com.neubs.shopneubs.models;

/**
 * Created by bikerlfh on 6/30/17.
 */

public class APIBannerModel {


    public static final String NAME_TABLE = "ApiBanner";
    public static final String PK = "idApiBanner";
    public static final String URL_IMAGEN = "urlImagen";
    public static final String IS_CLICKABLE = "isClickable";
    public static final String ID_SALDO_INVENTARIO = "idSaldoInventario";
    public static final String URL_RESULTADO = "urlResultado";
    public static final String FERCHA = "fecha";
    public static final String ESTADO = "estado";


    public static final String CREATE_TABLE = "create table " + NAME_TABLE + "("+
            PK + " integer primary key,"+
            URL_IMAGEN + " text not null,"+
            IS_CLICKABLE + " integer not null,"+
            ID_SALDO_INVENTARIO + " integer null," +
            URL_RESULTADO + " text null,"+
            FERCHA + " text not null," +
            ESTADO + " integer not null)";
}
