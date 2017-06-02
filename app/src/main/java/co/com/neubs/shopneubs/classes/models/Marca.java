package co.com.neubs.shopneubs.classes.models;

import android.content.Context;
import android.database.Cursor;

import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.models.MarcaModel;

/**
 * Created by bikerlfh on 5/22/17.
 */

public class Marca {
    private int idMarca;
    private String codigo;
    private String descripcion;

    private DbManager dbManager;

    public Marca(int idMarca,String codigo, String descripcion){
        this.idMarca = idMarca;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }
    public Marca(Context context){
        this.dbManager = new DbManager(context);
    }

    public boolean getMarcaByid(int idMarca){
        Cursor c = dbManager.Select(MarcaModel.NAME_TABLE, new String[] { "*" },MarcaModel.PK + "=?",new String[] {String.valueOf(idMarca)},null,null,null,null);
        if (c.moveToFirst())
        {
            serializeMarca(c);
            return true;
        }
        return false;
    }

    public boolean getMarcaByCodigo(String codigo){
        Cursor c = dbManager.Select(MarcaModel.NAME_TABLE, new String[] { "*" },MarcaModel.CODIGO + "=?",new String[] {codigo},null,null,null,null);
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
