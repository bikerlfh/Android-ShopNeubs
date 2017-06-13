package co.com.neubs.shopneubs.models;

/**
 * Created by bikerlfh on 6/13/17.
 */

public class MunicipioModel {

    public static final String NAME_TABLE = "Municipio";
    public static final String PK = "idMunicipio";
    public static final String ID_DEPARTAMENTO = "idDepartamento";
    public static final String CODIGO = "codigo";
    public static final String DESCRIPCION = "descripcion";


    public static final String CREATE_TABLE = "create table " + NAME_TABLE + "("+
            PK + " integer primary key,"+
            ID_DEPARTAMENTO + " integer not null,"+
            CODIGO + " text not null,"+
            DESCRIPCION + " text not null)";
}
