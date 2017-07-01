package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.interfaces.ICrud;
import co.com.neubs.shopneubs.models.APIBannerModel;

/**
 * Created by bikerlfh on 6/30/17.
 */

public class APIBanner implements ICrud {

    private int idApiBanner;
    private String urlImagen;
    /**
     * indica si el banner es clickeable
     */
    private boolean isClickable;
    /**
     * el id del saldo inventario que debe abrir cuando se hace el click
     */
    @Nullable
    private int idSaldoInventario;
    /**
     * Url de los resultados que debe arrojar cuando se da click en el banner
     */
    @Nullable
    private String urlResultado;
    private String fecha;
    /**
     * Especifica si el banner está activo o no
     */
    private boolean estado;


    private transient DbManager dbManager = DbManager.getInstance();

    public int getIdApiBanner() {
        return idApiBanner;
    }

    public void setIdApiBanner(int idApiBanner) {
        this.idApiBanner = idApiBanner;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    @Nullable
    public int getIdSaldoInventario() {
        return idSaldoInventario;
    }

    public void setIdSaldoInventario(int idSaldoInventario) {
        this.idSaldoInventario = idSaldoInventario;
    }

    @Nullable
    public String getUrlResultado() {
        return urlResultado;
    }

    public void setUrlResultado(@Nullable String urlResultado) {
        this.urlResultado = urlResultado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public boolean save() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(APIBannerModel.PK,idApiBanner);
        contentValues.put(APIBannerModel.URL_IMAGEN,urlImagen);
        contentValues.put(APIBannerModel.IS_CLICKABLE,isClickable);
        contentValues.put(APIBannerModel.ID_SALDO_INVENTARIO,idSaldoInventario);
        contentValues.put(APIBannerModel.URL_RESULTADO,urlResultado);
        contentValues.put(APIBannerModel.FERCHA,fecha);
        contentValues.put(APIBannerModel.ESTADO,estado);
        return (dbManager.Insert(APIBannerModel.NAME_TABLE,contentValues));
    }

    @Override
    public boolean update() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(APIBannerModel.URL_IMAGEN,urlImagen);
        contentValues.put(APIBannerModel.IS_CLICKABLE,isClickable);
        contentValues.put(APIBannerModel.ID_SALDO_INVENTARIO,idSaldoInventario);
        contentValues.put(APIBannerModel.URL_RESULTADO,urlResultado);
        contentValues.put(APIBannerModel.FERCHA,fecha);
        contentValues.put(APIBannerModel.ESTADO,estado);
        return (dbManager.Update(APIBannerModel.NAME_TABLE,contentValues, APIBannerModel.PK,idApiBanner));
    }

    @Override
    public boolean delete() {
        return dbManager.Delete(APIBannerModel.NAME_TABLE, APIBannerModel.PK ,idApiBanner);
    }

    @Override
    public boolean exists() {
        return dbManager.exists(APIBannerModel.NAME_TABLE, APIBannerModel.PK,idApiBanner);
    }

    @Override
    public boolean getById(int id) {
        Cursor c = dbManager.Select(APIBannerModel.NAME_TABLE, new String[] { "*" }, APIBannerModel.PK + "=?",new String[] {String.valueOf(idApiBanner)});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    /**
     * Obtiene todos los APIBanner
     * @param estado estado de los banner
     * @return listado de ApiBanner
     */
    public List<APIBanner> getAllApiBanner(boolean estado){
        List<APIBanner> listadoApiBanner = new ArrayList<>();
        Cursor c = dbManager.Select(APIBannerModel.NAME_TABLE,new String[] {"*"},APIBannerModel.ESTADO + "=?",new String[] {String.valueOf(estado?1:0)});
        if (c.moveToFirst()){
            do{
                APIBanner apiBanner = new APIBanner();
                apiBanner.serialize(c);
                listadoApiBanner.add(apiBanner);
            }while (c.moveToNext());
        }
        return listadoApiBanner;
    }

    private void serialize(Cursor cursor) {
        this.idApiBanner = cursor.getInt(cursor.getColumnIndex(APIBannerModel.PK));
        this.urlImagen = cursor.getString(cursor.getColumnIndex(APIBannerModel.URL_IMAGEN));
        this.isClickable = cursor.getInt(cursor.getColumnIndex(APIBannerModel.IS_CLICKABLE))==1;
        this.idSaldoInventario = cursor.getInt(cursor.getColumnIndex(APIBannerModel.ID_SALDO_INVENTARIO));
        this.urlResultado = cursor.getString(cursor.getColumnIndex(APIBannerModel.URL_RESULTADO));
        this.fecha = cursor.getString(cursor.getColumnIndex(APIBannerModel.FERCHA));
        this.estado = cursor.getInt(cursor.getColumnIndex(APIBannerModel.ESTADO))==1;
    }
}
