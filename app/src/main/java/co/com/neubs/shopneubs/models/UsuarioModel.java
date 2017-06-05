package co.com.neubs.shopneubs.models;

/**
 * Created by bikerlfh on 6/2/17.
 */

public class UsuarioModel {

    public static final String NAME_TABLE = "Usuario";
    public static final String PK = "idUsuario";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String NOMBRE = "nombre";
    public static final String APELLIDO = "apellido";


    public static final String CREATE_TABLE = "create table " + NAME_TABLE + "("+
            PK + " integer primary key,"+
            USERNAME + " text not null,"+
            EMAIL + " text not null," +
            NOMBRE + " text not null," +
            APELLIDO + " text not null)";
}
