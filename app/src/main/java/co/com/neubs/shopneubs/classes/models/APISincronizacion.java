package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.interfaces.ICrud;
import co.com.neubs.shopneubs.models.APISincronizacionModel;

/**
 * Created by bikerlfh on 6/3/17.
 */

public class APISincronizacion implements ICrud {
    private int idApiSincronizacion;
    private APITabla tabla;
    private String fecha;
    private boolean ultima;

    private transient DbManager dbManager;

    public APISincronizacion(Context context) {
        this.initDbManager(context);
    }

    public int getIdApiSincronizacion() {
        return idApiSincronizacion;
    }

    public void setIdApiSincronizacion(int idApiSincronizacion) {
        this.idApiSincronizacion = idApiSincronizacion;
    }

    public void setTabla(APITabla tabla) {
        this.tabla = tabla;
    }

    public APITabla getTabla() {
        return tabla;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public boolean getUltima() {
        return ultima;
    }

    public void setUltima(boolean ultima) {
        this.ultima = ultima;
    }

    public void initDbManager(Context context){
        this.dbManager = new DbManager(context);
    }

    /**
     * Graba el registro en la DB
     *
     * @return
     */
    @Override
    public boolean save() {
        ContentValues contentValues = new ContentValues();
        if (tabla != null)
            contentValues.put(APISincronizacionModel.ID_APITABLA, tabla.getIdApiTabla());
        contentValues.put(APISincronizacionModel.PK, idApiSincronizacion);
        contentValues.put(APISincronizacionModel.FECHA, fecha);
        contentValues.put(APISincronizacionModel.ULTIMA, ultima);

        // Si se va a guardar la ultima
        // se modifica la api que este guardad como ultimo.
        if (ultima){
            List<APISincronizacion> listadoApiSincronizacion = getUltimaApiSincronizacion(ultima);
            for (APISincronizacion apiSincronizacion: listadoApiSincronizacion) {
                if (tabla == apiSincronizacion.tabla){
                    apiSincronizacion.ultima = false;
                    apiSincronizacion.initDbManager(dbManager.context);
                    apiSincronizacion.save();
                }
            }
        }
        if (dbManager.Insert(APISincronizacionModel.NAME_TABLE, contentValues)){
            return true;
        }
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
        Cursor c = dbManager.Select(APISincronizacionModel.NAME_TABLE, new String[]{"*"}, APISincronizacionModel.PK + "=?", new String[]{String.valueOf(idApiSincronizacion)});
        if (c.moveToFirst()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean getById(int id) {
        Cursor c = dbManager.Select(APISincronizacionModel.NAME_TABLE, new String[]{"*"}, APISincronizacionModel.PK +"=?", new String[]{String.valueOf(id)});
        if (c.moveToFirst()) {
            serialize(c);
            return true;
        }
        return false;
    }

    public List<APISincronizacion> getApiSincronizacionByTabla(int idApiTabla, boolean ultima){
        List<APISincronizacion> listadoApi = new ArrayList<APISincronizacion>();
        Cursor c = dbManager.Select(APISincronizacionModel.NAME_TABLE, new String[]{"*"}, APISincronizacionModel.ID_APITABLA +"=? AND "+APISincronizacionModel.ULTIMA + "=?",
                new String[]{String.valueOf(idApiTabla),String.valueOf(ultima)});
        if (c.moveToFirst()){
            do{
                APISincronizacion apiSincronizacion = new APISincronizacion(dbManager.context);
                apiSincronizacion.serialize(c);
                listadoApi.add(apiSincronizacion);
            }while(c.moveToNext());
        }
        return listadoApi;
    }

    public List<APISincronizacion> getUltimaApiSincronizacion(boolean ultima){
        List<APISincronizacion> listadoApi = new ArrayList<APISincronizacion>();
        Cursor c = dbManager.Select(APISincronizacionModel.NAME_TABLE, new String[]{"*"}, APISincronizacionModel.ULTIMA + "=?", new String[]{String.valueOf(ultima)});
        if (c.moveToFirst()){
            do{
                APISincronizacion apiSincronizacion = new APISincronizacion(dbManager.context);
                apiSincronizacion.serialize(c);
                listadoApi.add(apiSincronizacion);
            }while(c.moveToNext());
        }
        return listadoApi;
    }
    /**
     * Obtiene la ultima sincronización realizada
     *
     * @return
     */
    public boolean getLast() {
        Cursor c = dbManager.Select(APISincronizacionModel.NAME_TABLE, new String[]{"*"}, "ultima = ?", new String[]{"1"});
        if (c.moveToFirst()) {
            serialize(c);
            return true;
        }
        return false;
    }

    /**
     * Obtiene la ultima sincronización realizada por el código de la ApiTabla
     *
     * @param codigoApiTabla codigo de la ApiTabla
     * @return
     */
    public boolean getLast(String codigoApiTabla) {
        APITabla apiTabla = new APITabla(this.dbManager.context);
        if (apiTabla.getAPITablaByCodigo(codigoApiTabla)) {
            Cursor c = dbManager.Select(APISincronizacionModel.NAME_TABLE, new String[]{"*"}, APISincronizacionModel.ID_APITABLA + "=? AND ultima = ?", new String[]{String.valueOf(apiTabla.getIdApiTabla()), "1"});
            if (c.moveToFirst()) {
                serialize(c);
                return true;
            }
        }
        return false;
    }

    /**
     * Serializa el cursor en el objeto
     * @param c cursor de la consulta
     */
    private void serialize(Cursor c){
        this.idApiSincronizacion = c.getInt(c.getColumnIndex(APISincronizacionModel.PK));
        this.fecha = c.getString(c.getColumnIndex(APISincronizacionModel.FECHA));
        this.ultima = boolean.class.cast(c.getInt(c.getColumnIndex(APISincronizacionModel.ULTIMA)));
        int idApiTabla = c.getInt(c.getColumnIndex(APISincronizacionModel.ID_APITABLA));
        if (idApiTabla > 0){
            this.tabla = new APITabla(this.dbManager.context);
            this.tabla.getById(idApiTabla);
        }
    }
}
