package co.com.neubs.shopneubs.models;

/**
 * Created by bikerlfh on 5/22/17.
 */

public class CategoriaModel {

    public static final String NAME_TABLE = "Categoria";
    public static final String PK = "idCategoria";
    public static final String CODIGO = "codigo";
    public static final String DESCRIPCION = "descripcion";
    public static final String CATEGORIA_PADRE = "idCategoriaPadre";
    public static final String ESTADO = "estado";


    public static final String CREATE_TABLE = "create table " + NAME_TABLE + "("+
            PK + " integer primary key,"+
            CODIGO + " text not null,"+
            DESCRIPCION + " text not null," +
            CATEGORIA_PADRE + " integer not null,"+
            ESTADO + " integer not null)";
}
