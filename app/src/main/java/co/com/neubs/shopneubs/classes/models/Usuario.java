package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.interfaces.ICrud;
import co.com.neubs.shopneubs.models.UsuarioModel;

/**
 * Created by bikerlfh on 6/2/17.
 */

public class Usuario implements ICrud {
    private int idUsuario;
    private String username;
    private String email;
    private String nombre;
    private String apellido;
    private String token;

    private transient DbManager dbManager;

    public Usuario(Context context){
        this.initDbManager(context);
    }

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

    public void initDbManager(Context context){
        this.dbManager = new DbManager(context);
    }

    @Override
    public boolean save() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UsuarioModel.PK,idUsuario);
        contentValues.put(UsuarioModel.USERNAME,username);
        contentValues.put(UsuarioModel.EMAIL,email);
        contentValues.put(UsuarioModel.NOMBRE,nombre);
        contentValues.put(UsuarioModel.APELLIDO,apellido);
        contentValues.put(UsuarioModel.TOKEN,apellido);

        if(dbManager.Insert(UsuarioModel.NAME_TABLE,contentValues))
            return true;
        return false;
    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public boolean getById(int id) {
        Cursor c = dbManager.Select(UsuarioModel.NAME_TABLE, new String[] { "*" },UsuarioModel.PK + "=?",new String[] {String.valueOf(id)});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    public boolean getAllUsuario(){
        Cursor c = dbManager.SelectAll(UsuarioModel.NAME_TABLE);
        if (c.moveToFirst())
        {
            serialize(c);
            return true;
        }
        return false;
    }

    private void serialize(Cursor c){
        this.idUsuario = c.getInt(c.getColumnIndex(UsuarioModel.PK));
        this.username = c.getString(c.getColumnIndex(UsuarioModel.USERNAME));
        this.email = c.getString(c.getColumnIndex(UsuarioModel.EMAIL));
        this.nombre = c.getString(c.getColumnIndex(UsuarioModel.NOMBRE));
        this.apellido = c.getString(c.getColumnIndex(UsuarioModel.APELLIDO));
        this.token = c.getString(c.getColumnIndex(UsuarioModel.TOKEN));

    }
}
