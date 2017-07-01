package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.interfaces.ICrud;
import co.com.neubs.shopneubs.models.APISectionModel;

/**
 * Created by Tatiana on 21/06/2017.
 */

public class APISection implements ICrud {
    private int idApiSection;
    private String title;
    private String subTitle;
    /**
     * Url de los productos a visualizar en la seccion
     */
    private String urlRequestProductos;
    /**
     * UrlRequest de la petición que se debe hacer cuando se da click en el boton mas
     */
    private String urlRequestMas;

    private boolean estado;

    private transient DbManager dbManager = DbManager.getInstance();

    public APISection() {
    }

    public APISection(String title, String subTitle, String urlProductos, String urlMas){
        this.title = title;
        this.subTitle = subTitle;
        this.urlRequestProductos = urlProductos;
        this.urlRequestMas = urlMas;
    }

    public int getIdApiSection() {
        return idApiSection;
    }

    public void setIdApiSection(int idApiSection) {
        this.idApiSection = idApiSection;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getUrlRequestProductos() {
        return urlRequestProductos;
    }

    public void setUrlRequestProductos(String urlRequestProductos) {
        this.urlRequestProductos = urlRequestProductos;
    }

    public String getUrlRequestMas() {
        return urlRequestMas;
    }

    public void setUrlRequestMas(String urlRequestMas) {
        this.urlRequestMas = urlRequestMas;
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
        contentValues.put(APISectionModel.PK,idApiSection);
        contentValues.put(APISectionModel.TITLE,title);
        contentValues.put(APISectionModel.SUBTITLE,subTitle);
        contentValues.put(APISectionModel.URL_REQUEST_PRODUCTOS,urlRequestProductos);
        contentValues.put(APISectionModel.URL_REQUEST_MAS,urlRequestMas);
        contentValues.put(APISectionModel.ESTADO,estado);
        return (dbManager.Insert(APISectionModel.NAME_TABLE,contentValues));
    }

    @Override
    public boolean update() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(APISectionModel.TITLE,title);
        contentValues.put(APISectionModel.SUBTITLE,subTitle);
        contentValues.put(APISectionModel.URL_REQUEST_PRODUCTOS,urlRequestProductos);
        contentValues.put(APISectionModel.URL_REQUEST_MAS,urlRequestMas);
        contentValues.put(APISectionModel.ESTADO,estado);
        return (dbManager.Update(APISectionModel.NAME_TABLE,contentValues,APISectionModel.PK,idApiSection));
    }

    @Override
    public boolean delete() {
        return (dbManager.Delete(APISectionModel.NAME_TABLE,APISectionModel.PK,idApiSection));
    }

    @Override
    public boolean exists() {
        return (dbManager.exists(APISectionModel.NAME_TABLE,APISectionModel.PK,idApiSection));
    }

    @Override
    public boolean getById(int id) {
        Cursor c = dbManager.Select(APISectionModel.NAME_TABLE, new String[] { "*" }, APISectionModel.PK + "=?",new String[] {String.valueOf(idApiSection)});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    /**
     * Obtiene todos los APISection filtrado por estado
     * @param estado estado de las section
     * @return listado de ApiSection
     */
    public List<APISection> getAll(boolean estado){
        List<APISection> listadoApiSection = new ArrayList<>();
        Cursor c = dbManager.Select(APISectionModel.NAME_TABLE,new String[] {"*"},APISectionModel.ESTADO + "=?",new String[] {String.valueOf(estado?1:0)});
        if (c.moveToFirst()){
            do{
                APISection apiSection = new APISection();
                apiSection.serialize(c);
                listadoApiSection.add(apiSection);
            }while (c.moveToNext());
        }
        return listadoApiSection;
    }

    private void serialize(Cursor cursor) {
        this.idApiSection = cursor.getInt(cursor.getColumnIndex(APISectionModel.PK));
        this.title = cursor.getString(cursor.getColumnIndex(APISectionModel.TITLE));
        this.subTitle = cursor.getString(cursor.getColumnIndex(APISectionModel.SUBTITLE));
        this.urlRequestProductos = cursor.getString(cursor.getColumnIndex(APISectionModel.URL_REQUEST_PRODUCTOS));
        this.urlRequestMas = cursor.getString(cursor.getColumnIndex(APISectionModel.URL_REQUEST_MAS));
        this.estado = cursor.getInt(cursor.getColumnIndex(APISectionModel.ESTADO))==1;
    }
}
