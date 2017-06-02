package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.database.Cursor;
import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.models.UsuarioModel;

/**
 * Created by bikerlfh on 6/2/17.
 */

public class Usuario {
    private int idUsuario;
    private String username;
    private String email;
    private String nombre;
    private String apellido;
    private String token;

    private DbManager dbManager;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean insertUsuario(int idUsuario, String username, String email, String nombre, String apellido){
        ContentValues contentValues = new ContentValues();
        contentValues.put(UsuarioModel.PK,idUsuario);
        contentValues.put(UsuarioModel.USERNAME,username);
        contentValues.put(UsuarioModel.EMAIL,email);
        contentValues.put(UsuarioModel.NOMBRE,nombre);
        contentValues.put(UsuarioModel.APELLIDO,apellido);

        if(dbManager.Insert(UsuarioModel.NAME_TABLE,contentValues))
            return true;
        return false;
    }

    public boolean getUsuarioByid(int idUsuario){
        Cursor c = dbManager.Select(UsuarioModel.NAME_TABLE, new String[] { "*" },UsuarioModel.PK + "=?",new String[] {String.valueOf(idUsuario)});
        if (c.moveToFirst())
        {
            serializeUsuario(c);
            return true;
        }
        return false;
    }

    public boolean getAllUsuario(){

        Cursor c = dbManager.SelectAll(UsuarioModel.NAME_TABLE);
        if (c.moveToFirst())
        {
            serializeUsuario(c);
            return true;
        }
        return false;
    }

    private void serializeUsuario(Cursor c){
        this.idUsuario = c.getInt(c.getColumnIndex(UsuarioModel.PK));
        this.username = c.getString(c.getColumnIndex(UsuarioModel.USERNAME));
        this.email = c.getString(c.getColumnIndex(UsuarioModel.EMAIL));
        this.nombre = c.getString(c.getColumnIndex(UsuarioModel.NOMBRE));
        this.apellido = c.getString(c.getColumnIndex(UsuarioModel.APELLIDO));

    }
}
