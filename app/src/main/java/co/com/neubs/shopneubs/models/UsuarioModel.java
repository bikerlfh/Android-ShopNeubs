package co.com.neubs.shopneubs.models;

/**
 * Created by bikerlfh on 6/2/17.
 */

public class UsuarioModel {

    public static final String NAME_TABLE = "Usuario";
    public static final String PK = "idUsuario";
    public static final String ID_CLIENTE = "idCliente";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String NOMBRE = "nombre";
    public static final String APELLIDO = "apellido";
    public static final String TOKEN = "token";


    public static final String CREATE_TABLE = "create table " + NAME_TABLE + "("+
            PK + " integer primary key,"+
            ID_CLIENTE + " integer null,"+
            USERNAME + " text null,"+
            EMAIL + " text null," +
            NOMBRE + " text null," +
            APELLIDO + " text null," +
            TOKEN + " text null)";
}
