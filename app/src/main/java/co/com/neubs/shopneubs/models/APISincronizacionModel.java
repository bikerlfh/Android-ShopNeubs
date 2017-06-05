package co.com.neubs.shopneubs.models;

/**
 * Created by bikerlfh on 6/3/17.
 */

public class APISincronizacionModel {
    public static final String NAME_TABLE = "ApiSincronizacion";
    public static final String PK = "idApiSincronizacion";
    /**
     * especifica que tabla fue  sincronizada
     * Sí es NULL, es una sincronización de la API completa
     */
    public static final String ID_APITABLA = "idApiTabla";
    public static final String FECHA = "fecha";
    public static final String ULTIMA = "ultima";


    public static final String CREATE_TABLE = "create table " + NAME_TABLE + "("+
            PK + " integer primary key,"+
            ID_APITABLA +" integer null,"+
            FECHA +" text null,"+
            ULTIMA + " int not null)";
}
