package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.interfaces.ICrud;
import co.com.neubs.shopneubs.models.PaisModel;

/**
 * Created by bikerlfh on 6/13/17.
 */

public class Pais implements ICrud {
    @SerializedName("pk")
    private int idPais;
    private String codigo;
    private String descripcion;

    private transient DbManager dbManager = DbManager.getInstance();

    public Pais(int idPais,String codigo, String descripcion){
        this.idPais = idPais;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }
    public Pais(){
    }

    public int getIdPais() {
        return idPais;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }

    @Override
    public boolean save() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PaisModel.PK,this.idPais);
        contentValues.put(PaisModel.CODIGO,this.codigo);
        contentValues.put(PaisModel.DESCRIPCION,this.descripcion);

        return (dbManager.Insert(PaisModel.NAME_TABLE,contentValues));
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
        return dbManager.exists(PaisModel.NAME_TABLE,PaisModel.PK,idPais);
    }

    @Override
    public boolean getById(int id) {
        Cursor c = dbManager.Select(PaisModel.NAME_TABLE, new String[] { "*" },PaisModel.PK + "=?",new String[] {String.valueOf(id)});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    public boolean getPaisByCodigo(String codigo){
        Cursor c = dbManager.Select(PaisModel.NAME_TABLE, new String[] { "*" },PaisModel.CODIGO + "=?",new String[] {codigo});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    public List<Pais> getAll(){
        List<Pais> listPais = null;
        Cursor c = dbManager.Select(PaisModel.NAME_TABLE, new String[] { "*" });
        if (c.moveToFirst()){
            listPais = new ArrayList<>();
            do {
                Pais td = new Pais();
                td.serialize(c);
                listPais.add(td);
            }while (c.moveToNext());
        }
        return  listPais;
    }

    private void serialize(Cursor c){
        this.idPais = c.getInt(c.getColumnIndex(PaisModel.PK));
        this.codigo = c.getString(c.getColumnIndex(PaisModel.CODIGO));
        this.descripcion = c.getString(c.getColumnIndex(PaisModel.DESCRIPCION));
    }
}
