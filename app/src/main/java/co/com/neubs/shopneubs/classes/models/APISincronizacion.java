package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
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

    private transient DbManager dbManager = DbManager.getInstance();

    public APISincronizacion() {

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

    /**
     * Graba el registro en la DB
     * @return true si guarda
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
        // se modifica la api que este guardado como ultimo.
        if (ultima){
            List<APISincronizacion> listadoApiSincronizacion = getUltimaApiSincronizacion(true);
            for (APISincronizacion apiSincronizacion: listadoApiSincronizacion) {
                if (tabla == apiSincronizacion.tabla){
                    apiSincronizacion.ultima = false;
                    apiSincronizacion.save();
                }
            }
        }
        return (dbManager.Insert(APISincronizacionModel.NAME_TABLE, contentValues));
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
        return dbManager.exists(APISincronizacionModel.NAME_TABLE, APISincronizacionModel.PK ,idApiSincronizacion);
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
        List<APISincronizacion> listadoApi = new ArrayList<>();
        Cursor c = dbManager.Select(APISincronizacionModel.NAME_TABLE, new String[]{"*"}, APISincronizacionModel.ID_APITABLA +"=? AND "+APISincronizacionModel.ULTIMA + "=?",
                new String[]{String.valueOf(idApiTabla),String.valueOf(ultima)});
        if (c.moveToFirst()){
            do{
                APISincronizacion apiSincronizacion = new APISincronizacion();
                apiSincronizacion.serialize(c);
                listadoApi.add(apiSincronizacion);
            }while(c.moveToNext());
        }
        return listadoApi;
    }

    public List<APISincronizacion> getUltimaApiSincronizacion(boolean ultima){
        List<APISincronizacion> listadoApi = new ArrayList<>();
        Cursor c = dbManager.Select(APISincronizacionModel.NAME_TABLE, new String[]{"*"}, APISincronizacionModel.ULTIMA + "=?", new String[]{String.valueOf(ultima)});
        if (c.moveToFirst()){
            do{
                APISincronizacion apiSincronizacion = new APISincronizacion();
                apiSincronizacion.serialize(c);
                listadoApi.add(apiSincronizacion);
            }while(c.moveToNext());
        }
        return listadoApi;
    }
    /**
     * Obtiene la ultima sincronización realizada
     * @return true si encuentra la ultima sincronización
     */
    public boolean getLastGeneral() {
        Cursor c = dbManager.RawQuery("SELECT * FROM "+ APISincronizacionModel.NAME_TABLE +" WHERE " + APISincronizacionModel.ID_APITABLA + " IS NULL AND " + APISincronizacionModel.ULTIMA + " = 1",null);
        if (c.moveToFirst()) {
            serialize(c);
            return true;
        }
        return false;
    }

    /**
     * Obtiene la ultima sincronización realizada por el código de la ApiTabla
     * @param codigoApiTabla codigo de la ApiTabla
     * @return true si encuentra la ultima sincronización
     */
    public boolean getLast(String codigoApiTabla) {
        APITabla apiTabla = new APITabla();
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
        this.ultima = c.getInt(c.getColumnIndex(APISincronizacionModel.ULTIMA)) ==1;
        int idApiTabla = c.getInt(c.getColumnIndex(APISincronizacionModel.ID_APITABLA));
        if (idApiTabla > 0){
            this.tabla = new APITabla();
            this.tabla.getById(idApiTabla);
        }
    }
}
