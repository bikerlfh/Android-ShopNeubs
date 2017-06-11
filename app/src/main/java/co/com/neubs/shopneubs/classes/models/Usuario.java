package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.interfaces.ICrud;
import co.com.neubs.shopneubs.models.UsuarioModel;

/**
 * Created by bikerlfh on 6/2/17.
 */

public class Usuario implements ICrud {
    private int idUsuario;
    private int idCliente;
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

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
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
        contentValues.put(UsuarioModel.ID_CLIENTE,idCliente);
        contentValues.put(UsuarioModel.USERNAME,username);
        contentValues.put(UsuarioModel.EMAIL,email);
        contentValues.put(UsuarioModel.NOMBRE,nombre);
        contentValues.put(UsuarioModel.APELLIDO,apellido);
        contentValues.put(UsuarioModel.TOKEN,token);

        if(dbManager.Insert(UsuarioModel.NAME_TABLE,contentValues))
            return true;
        return false;
    }

    @Override
    public boolean update() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UsuarioModel.ID_CLIENTE,idCliente);
        contentValues.put(UsuarioModel.USERNAME,username);
        contentValues.put(UsuarioModel.EMAIL,email);
        contentValues.put(UsuarioModel.NOMBRE,nombre);
        contentValues.put(UsuarioModel.APELLIDO,apellido);
        contentValues.put(UsuarioModel.TOKEN,token);
        if (dbManager.Update(UsuarioModel.NAME_TABLE,contentValues,UsuarioModel.PK+"=?",new String[]{String.valueOf(idUsuario)}))
            return true;
        return false;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public boolean exists() {
        Cursor c = dbManager.Select(UsuarioModel.NAME_TABLE, new String[] { "*" },UsuarioModel.PK + "=?",new String[] {String.valueOf(idUsuario)});
        if (c.moveToFirst()){
            return true;
        }
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

    public boolean getByIdCliente(int idCliente) {
        Cursor c = dbManager.Select(UsuarioModel.NAME_TABLE, new String[] { "*" },UsuarioModel.PK + "=?",new String[] {String.valueOf(idCliente)});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    public boolean getByEmail(String email) {
        Cursor c = dbManager.Select(UsuarioModel.NAME_TABLE, new String[] { "*" },UsuarioModel.EMAIL + "=?",new String[] {email});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    public boolean getByUserName(String username) {
        Cursor c = dbManager.Select(UsuarioModel.NAME_TABLE, new String[] { "*" },UsuarioModel.USERNAME + "=?",new String[] {username});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    /**
     * Consulta los usuarios que tengan el token guardado (que esten logeados)
     * @return Listado de usuarios logeados
     */
    public List<Usuario> getLoginUser() {
        List<Usuario> usuarios = null;
        Cursor c = dbManager.RawQuery("SELECT * FROM Usuario WHERE token IS NOT NULL",null);
        if (c.moveToFirst())
        {
            usuarios = new ArrayList<>();
            do{
                Usuario user = new Usuario(dbManager.context);
                user.serialize(c);
                usuarios.add(user);
            }while(c.moveToNext());
        }
        return usuarios;
    }

    public List<Usuario> getAllUsuario(){
        List<Usuario> usuarios = null;
        Cursor c = dbManager.SelectAll(UsuarioModel.NAME_TABLE);
        if (c.moveToFirst())
        {
            usuarios = new ArrayList<>();
            do{
                Usuario user = new Usuario(dbManager.context);
                user.serialize(c);
                usuarios.add(user);
            }while(c.moveToNext());
        }
        return usuarios;
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
