package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.interfaces.ICrud;
import co.com.neubs.shopneubs.models.CategoriaModel;
import co.com.neubs.shopneubs.models.MarcaModel;
import co.com.neubs.shopneubs.models.SugerenciaBusquedaModel;

/**
 * Created by bikerlfh on 6/22/17.
 */

public class SugerenciaBusqueda implements ICrud {

    private int idSugerenciaBusqueda;
    private String sugerencia;

    private transient DbManager dbManager = DbManager.getInstance();

    public SugerenciaBusqueda() {
    }

    public SugerenciaBusqueda(String sugerencia) {
        this.sugerencia = sugerencia;
    }

    public int getIdSugerenciaBusqueda() {
        return idSugerenciaBusqueda;
    }

    public void setIdSugerenciaBusqueda(int idSugerenciaBusqueda) {
        this.idSugerenciaBusqueda = idSugerenciaBusqueda;
    }

    public String getSugerencia() {
        return sugerencia;
    }

    public void setSugerencia(String sugerencia) {
        this.sugerencia = sugerencia;
    }

    @Override
    public String toString() {
        return sugerencia;
    }

    @Override
    public boolean save() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SugerenciaBusquedaModel.SUGERENCIA,sugerencia);
        return dbManager.Insert(SugerenciaBusquedaModel.NAME_TABLE,contentValues);
    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public boolean delete() {
        return dbManager.Delete(SugerenciaBusquedaModel.NAME_TABLE,SugerenciaBusquedaModel.PK + "=?",new String[] { String.valueOf(idSugerenciaBusqueda)});
    }

    @Override
    public boolean exists() {
        return dbManager.exists(SugerenciaBusquedaModel.NAME_TABLE,SugerenciaBusquedaModel.PK,idSugerenciaBusqueda);
    }

    @Override
    public boolean getById(int id) {
        Cursor c = dbManager.Select(SugerenciaBusquedaModel.NAME_TABLE, new String[] { "*" },SugerenciaBusquedaModel.PK + "=?",new String[] {String.valueOf(idSugerenciaBusqueda)});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    /**
     * consulta todas las sugerencias (incluidas las categorias y marcas)
     * @return arreglo de String con las sugerencias
     */
    public List<String> getAllSugerencias(){
        List<String> listSugerencias = new ArrayList<>();
        Cursor c = dbManager.Select(SugerenciaBusquedaModel.NAME_TABLE, new String[] { "*" });
        if (c.moveToFirst()){
            do{
                listSugerencias.add(c.getString(c.getColumnIndex(SugerenciaBusquedaModel.SUGERENCIA)));
            }while (c.moveToNext());
        }
        // Se consultan las categorias
        c = dbManager.Select(CategoriaModel.NAME_TABLE,new String[] { "*" });
        if (c.moveToFirst()){
            do{
                listSugerencias.add(c.getString(c.getColumnIndex(CategoriaModel.DESCRIPCION)));
            }while (c.moveToNext());
        }

        // Se consultan las Marcas
        c = dbManager.Select(MarcaModel.NAME_TABLE,new String[] { "*" });
        if (c.moveToFirst()){
            do{
                listSugerencias.add(c.getString(c.getColumnIndex(MarcaModel.DESCRIPCION)));
            }while (c.moveToNext());
        }
        //return listSugerencias.toArray(new String[listSugerencias.size()]);
        return listSugerencias;
    }

    private void serialize(Cursor cursor) {
        this.idSugerenciaBusqueda = cursor.getInt(cursor.getColumnIndex(SugerenciaBusquedaModel.PK));
        this.sugerencia = cursor.getString(cursor.getColumnIndex(SugerenciaBusquedaModel.SUGERENCIA));
    }
}
