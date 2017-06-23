package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.interfaces.ICrud;
import co.com.neubs.shopneubs.models.TipoDocumentoModel;

/**
 * Created by bikerlfh on 6/13/17.
 */

public class TipoDocumento implements ICrud {
    @SerializedName("pk")
    private int idTipoDocumento;
    private String codigo;
    private String descripcion;

    private transient DbManager dbManager = DbManager.getInstance();

    public TipoDocumento(int idTipoDocumento,String codigo, String descripcion){
        this.idTipoDocumento = idTipoDocumento;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }
    public TipoDocumento(){
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
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

    /**
     * Se rescribe el toString para el ArrayAdapter del spinner
     * @return toString
     */
    @Override
    public String toString() {
        return codigo;
    }

    @Override
    public boolean save() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TipoDocumentoModel.PK,this.idTipoDocumento);
        contentValues.put(TipoDocumentoModel.CODIGO,this.codigo);
        contentValues.put(TipoDocumentoModel.DESCRIPCION,this.descripcion);

        return (dbManager.Insert(TipoDocumentoModel.NAME_TABLE,contentValues));
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
        return dbManager.exists(TipoDocumentoModel.NAME_TABLE,TipoDocumentoModel.PK,idTipoDocumento);
    }

    @Override
    public boolean getById(int id) {
        Cursor c = dbManager.Select(TipoDocumentoModel.NAME_TABLE, new String[] { "*" },TipoDocumentoModel.PK + "=?",new String[] {String.valueOf(id)});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    public boolean getTipoDocumentoByCodigo(String codigo){
        Cursor c = dbManager.Select(TipoDocumentoModel.NAME_TABLE, new String[] { "*" },TipoDocumentoModel.CODIGO + "=?",new String[] {codigo});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    public List<TipoDocumento> getAll(){
        List<TipoDocumento> listTipoDocumento = null;
        Cursor c = dbManager.Select(TipoDocumentoModel.NAME_TABLE, new String[] { "*" });
        if (c.moveToFirst()){
            listTipoDocumento = new ArrayList<>();
            do {
                TipoDocumento td = new TipoDocumento();
                td.serialize(c);
                listTipoDocumento.add(td);
            }while (c.moveToNext());
        }
        return  listTipoDocumento;
    }

    private void serialize(Cursor c){
        this.idTipoDocumento = c.getInt(c.getColumnIndex(TipoDocumentoModel.PK));
        this.codigo = c.getString(c.getColumnIndex(TipoDocumentoModel.CODIGO));
        this.descripcion = c.getString(c.getColumnIndex(TipoDocumentoModel.DESCRIPCION));
    }
}
