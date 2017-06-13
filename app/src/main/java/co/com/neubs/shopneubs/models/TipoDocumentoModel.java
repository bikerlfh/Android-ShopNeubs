package co.com.neubs.shopneubs.models;

/**
 * Created by bikerlfh on 6/13/17.
 */

public class TipoDocumentoModel {

    public static final String NAME_TABLE = "TipoDocumento";
    public static final String PK = "idTipoDocumento";
    public static final String CODIGO = "codigo";
    public static final String DESCRIPCION = "descripcion";


    public static final String CREATE_TABLE = "create table " + NAME_TABLE + "("+
            PK + " integer primary key,"+
            CODIGO + " text not null,"+
            DESCRIPCION + " text not null)";
}
