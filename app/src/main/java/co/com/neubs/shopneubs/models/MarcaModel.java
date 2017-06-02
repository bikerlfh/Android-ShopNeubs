package co.com.neubs.shopneubs.models;

/**
 * Created by bikerlfh on 5/22/17.
 */

public class MarcaModel {
    public static final String NAME_TABLE = "marca";
    public static final String PK = "idMarca";
    public static final String CODIGO = "codigo";
    public static final String DESCRIPCION = "descripcion";


    public static final String CREATE_TABLE = "create table " + NAME_TABLE + "("+
            PK + " integer primary key,"+
            CODIGO + " text not null,"+
            DESCRIPCION + " text not null)";
}
