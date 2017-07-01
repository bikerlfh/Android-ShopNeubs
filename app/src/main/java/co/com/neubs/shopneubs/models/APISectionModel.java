package co.com.neubs.shopneubs.models;

/**
 * Created by bikerlfh on 7/1/17.
 */

public class APISectionModel {

    public static final String NAME_TABLE = "ApiSection";
    public static final String PK = "idApiSection";
    public static final String TITLE = "title";
    public static final String SUBTITLE= "subTitle";
    public static final String URL_REQUEST_PRODUCTOS= "urlRequestProductos";
    public static final String URL_REQUEST_MAS = "urlRequestMas";
    public static final String ESTADO = "estado";


    public static final String CREATE_TABLE = "create table " + NAME_TABLE + "("+
            PK + " integer primary key,"+
            TITLE + " text not null,"+
            SUBTITLE + " text null,"+
            URL_REQUEST_PRODUCTOS + " text not null," +
            URL_REQUEST_MAS + " text null,"+
            ESTADO + " text not null)";
}
