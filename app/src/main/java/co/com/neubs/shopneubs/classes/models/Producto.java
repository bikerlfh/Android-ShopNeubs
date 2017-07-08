package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.interfaces.ICrud;
import co.com.neubs.shopneubs.models.ProductoModel;

/**
 * Created by bikerlfh on 5/22/17.
 */

public class Producto implements ICrud {
    @SerializedName("idProducto")
    private int idProducto;
    @SerializedName("idCategoria")
    private int idCategoria;
    @SerializedName("idMarca")
    private int idMarca;
    @SerializedName("numeroProducto")
    private int numeroProducto;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("descripcion")
    private String descripcion;
    @SerializedName("especificacion")
    private String especificacion;
    @SerializedName("urldescripcion")
    private String urlDescripcion;
    @SerializedName("imagenes")
    private ArrayList<Imagen> imagenes;
    @SerializedName("imagen")
    private String imagen;

    private transient Marca marca;

    private transient DbManager dbManager = DbManager.getInstance();

    public Producto(){
    }

    public Producto(int idProducto,int idCategoria, int idMarca,int numeroProducto,
                    String nombre,String descripcion,String especificacion,String urlDescripcion){
        this.idProducto = idProducto;
        this.idCategoria = idCategoria;
        this.idMarca = idMarca;
        this.numeroProducto = numeroProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.especificacion = especificacion;
        this.urlDescripcion = urlDescripcion;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public int getNumeroProducto() {
        return numeroProducto;
    }

    public void setNumeroProducto(int numeroProducto) {
        this.numeroProducto = numeroProducto;
    }

    public String getNombre() {
        return Helper.EncodingUTF8(nombre);
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
       return Helper.EncodingUTF8(descripcion);
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEspecificacion() {
        return Helper.EncodingUTF8(especificacion);
    }

    public void setEspecificacion(String especificacion) {
        this.especificacion = especificacion;
    }

    public String getUrlDescripcion() {
        return urlDescripcion;
    }

    public void setUrlDescripcion(String urlDescripcion) {
        this.urlDescripcion = urlDescripcion;
    }

    public ArrayList<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(ArrayList<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public String getImagen() {
        return this.imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Marca getMarca() {
        if (idMarca>0 && marca==null){
            marca = new Marca();
            marca.getById(idMarca);
        }
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    @Override
    public boolean save() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductoModel.PK,idProducto);
        contentValues.put(ProductoModel.NUMERO_PRODUCTO,numeroProducto);
        contentValues.put(ProductoModel.NOMBRE,nombre);
        contentValues.put(ProductoModel.DESCRIPCION,descripcion);
        contentValues.put(ProductoModel.ESPECIFICACION,especificacion);
        contentValues.put(ProductoModel.URL_DESCRIPCION,urlDescripcion);

        return (dbManager.Insert(ProductoModel.NAME_TABLE,contentValues));
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
        return dbManager.exists(ProductoModel.NAME_TABLE,ProductoModel.PK,idProducto);
    }

    @Override
    public boolean getById(int id) {
        Cursor c = dbManager.Select(ProductoModel.NAME_TABLE, new String[] { "*" },ProductoModel.PK + "=?",new String[] {String.valueOf(idProducto)});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    public boolean getProductoByNumeroProducto(int numeroProducto){
        Cursor c = dbManager.Select(ProductoModel.NAME_TABLE, new String[] { "*" },ProductoModel.NUMERO_PRODUCTO + "=?",new String[] {String.valueOf(numeroProducto)});
        if (c.moveToFirst()) {
            serialize(c);
            return true;
        }
        return false;
    }

    private void serialize(Cursor c){
        this.idProducto = c.getInt(c.getColumnIndex(ProductoModel.PK));
        this.idCategoria = c.getInt(c.getColumnIndex(ProductoModel.ID_CATEGORIA));
        this.idMarca = c.getInt(c.getColumnIndex(ProductoModel.ID_MARCA));
        this.numeroProducto = c.getInt(c.getColumnIndex(ProductoModel.NUMERO_PRODUCTO));
        this.nombre = c.getString(c.getColumnIndex(ProductoModel.NOMBRE));
        this.descripcion = c.getString(c.getColumnIndex(ProductoModel.DESCRIPCION));
        this.especificacion = c.getString(c.getColumnIndex(ProductoModel.ESPECIFICACION));
        this.urlDescripcion = c.getString(c.getColumnIndex(ProductoModel.URL_DESCRIPCION));
    }
}
