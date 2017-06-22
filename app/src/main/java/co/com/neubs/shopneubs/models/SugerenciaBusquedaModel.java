package co.com.neubs.shopneubs.models;

/**
 * Created by bikerlfh on 6/22/17.
 */

public class SugerenciaBusquedaModel {

    public static final String NAME_TABLE = "SugerenciaBusqueda";
    public static final String PK = "idSugerenciaBusqueda";
    public static final String SUGERENCIA = "sugerencia";


    public static final String CREATE_TABLE = "create table " + NAME_TABLE + "("+
            PK + " integer primary key AUTOINCREMENT not null,"+
            SUGERENCIA + " text not null)";
}
