package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.Date;

import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.models.APISincronizacionModel;

/**
 * Created by bikerlfh on 6/3/17.
 */

public class APISincronizacion {
    private int idApiSincronizacion;
    private APITabla tabla;
    private Date fecha;

    private transient DbManager dbManager;

    public APISincronizacion(Context context) {
        this.initDbManager(context);
    }

    public void initDbManager(Context context) {
        this.dbManager = new DbManager(context);
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }


    /**
     * Graba el registro en la DB
     *
     * @return
     */
    public boolean save() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(APISincronizacionModel.ID_APITABLA, tabla.getIdApiTabla());
        contentValues.put(APISincronizacionModel.FECHA, fecha.toString());

        if (dbManager.Insert(APISincronizacionModel.NAME_TABLE, contentValues))
            return true;
        return false;
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
     * Valida si existe en la base de datos un registro por el id indicado
     * @param idApiSincronizacion
     * @return
     */
    public boolean exists(int idApiSincronizacion){
        Cursor c = dbManager.Select(APISincronizacionModel.NAME_TABLE, new String[]{"*"}, APISincronizacionModel.ID_APITABLA + "=?", new String[]{String.valueOf(idApiSincronizacion)});
        if (c.moveToFirst()) {
            return true;
        }
        return false;
    }

    /**
     * Serializa el cursor en el objeto
     * @param c cursor de la consulta
     */
    private void serialize(Cursor c){
        this.idApiSincronizacion = c.getInt(c.getColumnIndex(APISincronizacionModel.PK));
        this.fecha = Date.class.cast(c.getString(c.getColumnIndex(APISincronizacionModel.FECHA)));
        int idApiTabla = c.getInt(c.getColumnIndex(APISincronizacionModel.ID_APITABLA));
        if (idApiTabla > 0){
            this.tabla = new APITabla(this.dbManager.context);
            this.tabla.getAPITablaByid(idApiTabla);
        }
    }
}
