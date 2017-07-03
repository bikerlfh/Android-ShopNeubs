package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.interfaces.ICrud;
import co.com.neubs.shopneubs.models.MarcaModel;

/**
 * Created by bikerlfh on 5/22/17.
 */

public class Marca implements ICrud {
    @SerializedName("pk")
    private int idMarca;
    private String codigo;
    private String descripcion;

    private transient DbManager dbManager = DbManager.getInstance();

    public Marca(int idMarca,String codigo, String descripcion){
        this.idMarca = idMarca;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }
    public Marca(){
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return Helper.EncodingUTF8(descripcion);
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean save() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MarcaModel.PK,this.idMarca);
        contentValues.put(MarcaModel.CODIGO,this.codigo);
        contentValues.put(MarcaModel.DESCRIPCION,this.descripcion);

        return (dbManager.Insert(MarcaModel.NAME_TABLE,contentValues));
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
        return dbManager.exists(MarcaModel.NAME_TABLE,MarcaModel.PK,idMarca);
    }

    @Override
    public boolean getById(int id) {
        Cursor c = dbManager.Select(MarcaModel.NAME_TABLE, new String[] { "*" },MarcaModel.PK + "=?",new String[] {String.valueOf(id)});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    public boolean getMarcaByCodigo(String codigo){
        Cursor c = dbManager.Select(MarcaModel.NAME_TABLE, new String[] { "*" },MarcaModel.CODIGO + "=?",new String[] {codigo});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    public List<Marca> getAll(){
        List<Marca> listMarca = new ArrayList<>();
        Cursor c = dbManager.SelectAll(MarcaModel.NAME_TABLE);
        if (c.moveToFirst()){
            do {
                Marca marca = new Marca();
                marca.serialize(c);
                listMarca.add(marca);
            }while (c.moveToNext());
        }
        return listMarca;
    }

    private void serialize(Cursor c){
        this.idMarca = c.getInt(c.getColumnIndex(MarcaModel.PK));
        this.codigo = c.getString(c.getColumnIndex(MarcaModel.CODIGO));
        this.descripcion = c.getString(c.getColumnIndex(MarcaModel.DESCRIPCION));
    }
}
