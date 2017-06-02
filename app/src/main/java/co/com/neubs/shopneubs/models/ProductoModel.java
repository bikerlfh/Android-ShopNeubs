package co.com.neubs.shopneubs.models;

/**
 * Created by bikerlfh on 5/22/17.
 */

public class ProductoModel {
    public static final String NAME_TABLE = "producto";
    public static final String PK = "idProducto";
    public static final String ID_CATEGORIA = "idCategoria";
    public static final String ID_MARCA = "idMarca";
    public static final String NUMERO_PRODUCTO = "numeroProducto";
    public static final String NOMBRE = "nombre";
    public static final String DESCRIPCION = "descripcion";
    public static final String ESPECIFICACION = "especificacion";
    public static final String URL_DESCRIPCION = "ulrDescripcion";


    public static final String CREATE_TABLE = "create table " + NAME_TABLE + "("+
            PK + " integer primary key,"+
            ID_CATEGORIA + " integer not null,"+
            ID_MARCA + " decimal not null,"+
            NUMERO_PRODUCTO + " decimal not null,"+
            NOMBRE + " text null,"+
            DESCRIPCION + " text null,"+
            ESPECIFICACION + " text null,"+
            URL_DESCRIPCION + " text null)";
}
