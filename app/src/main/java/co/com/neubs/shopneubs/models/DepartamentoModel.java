package co.com.neubs.shopneubs.models;

/**
 * Created by bikerlfh on 6/13/17.
 */

public class DepartamentoModel {
    public static final String NAME_TABLE = "Departamento";
    public static final String PK = "idDepartamento";
    public static final String ID_PAIS = "idPais";
    public static final String CODIGO = "codigo";
    public static final String DESCRIPCION = "descripcion";


    public static final String CREATE_TABLE = "create table " + NAME_TABLE + "("+
            PK + " integer primary key,"+
            ID_PAIS + " integer not null,"+
            CODIGO + " text not null,"+
            DESCRIPCION + " text not null)";
}
