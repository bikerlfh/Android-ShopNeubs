package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.interfaces.ICrud;
import co.com.neubs.shopneubs.models.APITablaModel;

/**
 * Created by bikerlfh on 6/3/17.
 */

public class APITabla implements ICrud {
    private int idApiTabla;
    private String codigo;
    private String descripcion;

    private transient DbManager dbManager = DbManager.getInstance();

    public APITabla() {
    }

    public int getIdApiTabla() {
        return idApiTabla;
    }

    public void setIdApiTabla(int idApiTabla) {
        this.idApiTabla = idApiTabla;
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
    public boolean save() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(APITablaModel.PK,idApiTabla);
        contentValues.put(APITablaModel.CODIGO,codigo);
        contentValues.put(APITablaModel.DESCRIPCION,descripcion);

        return (dbManager.Insert(APITablaModel.NAME_TABLE,contentValues));
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
        return dbManager.exists(APITablaModel.NAME_TABLE,APITablaModel.PK,idApiTabla);
    }

    /**
     * Consulta la ApiTabla por ID
     * @param id idApiTabla
     * @return true si encuentra el objeto
     */
    @Override
    public boolean getById(int id) {
        Cursor c = dbManager.Select(APITablaModel.NAME_TABLE, new String[] { "*" },APITablaModel.PK + "=?",new String[] {String.valueOf(idApiTabla)});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    /**
     * Consulta la ApiTabla por Código
     * @param codigo de la ApiTabla
     * @return true si encuentra el objeto
     */
    public boolean getAPITablaByCodigo(String codigo){
        Cursor c = dbManager.Select(APITablaModel.NAME_TABLE, new String[] { "*" },APITablaModel.CODIGO + "=?",new String[] { codigo });
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    public List<APITabla> getAllAPITabla()
    {
        List<APITabla> listadoAPITabla = new ArrayList<>();
        Cursor c = dbManager.SelectAll(APITablaModel.NAME_TABLE);
        if (c.moveToFirst()){
            // Recorremos el cursor y llenamos el Objeto inf el cual se agrega a la ListadoMunicipio
            do{
                APITabla tabla = new APITabla();
                tabla.serialize(c);
                listadoAPITabla.add(tabla);
            }while (c.moveToNext());
        }
        return listadoAPITabla;
    }
    /**
     * Obtiene la primera tabla guardada
     * Este método se utiliza para saber si se ha guardado información en la base de datos
     * @return true si encuentra la primera tabla guardada
     */
    public boolean getFirst(){
        Cursor c = dbManager.Select(APITablaModel.NAME_TABLE, new String[] { "TOP 1 *" });
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    private void serialize(Cursor c){
        this.idApiTabla = c.getInt(c.getColumnIndex(APITablaModel.PK));
        this.codigo = c.getString(c.getColumnIndex(APITablaModel.CODIGO));
        this.descripcion = c.getString(c.getColumnIndex(APITablaModel.DESCRIPCION));
    }
}
