package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.interfaces.ICrud;
import co.com.neubs.shopneubs.models.UsuarioModel;

/**
 * Created by bikerlfh on 6/2/17.
 */

public class Usuario implements ICrud {
    @SerializedName("pk")
    private int idUsuario;
    private int idCliente;
    private String username;
    private String email;
    private String nombre;
    private String apellido;
    private String token;

    private transient DbManager dbManager = DbManager.getInstance();

    public Usuario(){
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

        return (dbManager.Insert(UsuarioModel.NAME_TABLE,contentValues));
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
        return dbManager.Update(UsuarioModel.NAME_TABLE,contentValues,UsuarioModel.PK+"=?",new String[]{String.valueOf(idUsuario)});
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public boolean exists() {
        return dbManager.exists(UsuarioModel.NAME_TABLE,UsuarioModel.PK ,idUsuario);
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
     * Consulta el usuario que tengan el token guardado (que este logeado)
     * @return True si encuentra un usuario logeado, de lo contrario false
     */
    public boolean getLoginUser() {
        Cursor c = dbManager.RawQuery("SELECT * FROM Usuario WHERE token IS NOT NULL",null);
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    public List<Usuario> getAllUsuario(){
        List<Usuario> usuarios = null;
        Cursor c = dbManager.SelectAll(UsuarioModel.NAME_TABLE);
        if (c.moveToFirst()){
            usuarios = new ArrayList<>();
            do{
                Usuario user = new Usuario();
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
