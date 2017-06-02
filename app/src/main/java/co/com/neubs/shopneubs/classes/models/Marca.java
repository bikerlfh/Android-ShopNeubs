package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.models.MarcaModel;

/**
 * Created by bikerlfh on 5/22/17.
 */

public class Marca {
    @SerializedName("pk")
    private int idMarca;
    private String codigo;
    private String descripcion;

    private transient DbManager dbManager;

    public Marca(int idMarca,String codigo, String descripcion){
        this.idMarca = idMarca;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }
    public Marca(Context context){
        this.dbManager = new DbManager(context);
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
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void initDbManager(Context context){
        this.dbManager = new DbManager(context);
    }

    public boolean save(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MarcaModel.PK,this.idMarca);
        contentValues.put(MarcaModel.CODIGO,this.codigo);
        contentValues.put(MarcaModel.DESCRIPCION,this.descripcion);

        if(dbManager.Insert(MarcaModel.NAME_TABLE,contentValues))
            return true;
        return false;
    }
    public boolean getMarcaByid(int idMarca){
        Cursor c = dbManager.Select(MarcaModel.NAME_TABLE, new String[] { "*" },MarcaModel.PK + "=?",new String[] {String.valueOf(idMarca)});
        if (c.moveToFirst())
        {
            serializeMarca(c);
            return true;
        }
        return false;
    }

    public boolean getMarcaByCodigo(String codigo){
        Cursor c = dbManager.Select(MarcaModel.NAME_TABLE, new String[] { "*" },MarcaModel.CODIGO + "=?",new String[] {codigo});
        if (c.moveToFirst())
        {
            serializeMarca(c);
            return true;
        }
        return false;
    }

    private void serializeMarca(Cursor c){
        this.idMarca = c.getInt(c.getColumnIndex(MarcaModel.PK));
        this.codigo = c.getString(c.getColumnIndex(MarcaModel.CODIGO));
        this.descripcion = c.getString(c.getColumnIndex(MarcaModel.DESCRIPCION));
    }
}
