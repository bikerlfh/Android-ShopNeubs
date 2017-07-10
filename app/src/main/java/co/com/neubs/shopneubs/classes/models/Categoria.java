package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.interfaces.ICrud;
import co.com.neubs.shopneubs.models.CategoriaModel;

/**
 * Created by bikerlfh on 5/22/17.
 */

public class Categoria implements ICrud{
    private int idCategoria;
    private String codigo;
    private String descripcion;
    @SerializedName("categoriaPadre")
    private int idCategoriaPadre;
    private boolean estado;

    public transient Categoria categoriaPadre;

    private transient DbManager dbManager = DbManager.getInstance();

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return Helper.EncodingUTF8(descripcion);
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdCategoriaPadre() {
        return idCategoriaPadre;
    }

    public void setIdCategoriaPadre(int idCategoriaPadre) {
        this.idCategoriaPadre = idCategoriaPadre;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public boolean getById(int id) {
        Cursor c = dbManager.Select(CategoriaModel.NAME_TABLE, new String[] { "*" },CategoriaModel.PK + "=?",new String[] {String.valueOf(idCategoria)});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    @Override
    public boolean save(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CategoriaModel.PK,idCategoria);
        contentValues.put(CategoriaModel.CODIGO,codigo);
        contentValues.put(CategoriaModel.DESCRIPCION,descripcion);
        contentValues.put(CategoriaModel.CATEGORIA_PADRE,idCategoriaPadre);
        contentValues.put(CategoriaModel.ESTADO,estado);
        return (dbManager.Insert(CategoriaModel.NAME_TABLE,contentValues));
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
        return dbManager.exists(CategoriaModel.NAME_TABLE,CategoriaModel.PK,idCategoria);
    }

    public boolean getCategoriaByCodigo(String codigo){
        Cursor c = dbManager.Select(CategoriaModel.NAME_TABLE, new String[] { "*" },CategoriaModel.CODIGO + "=?",new String[] {codigo});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    public List<Categoria> getListCategoriaByCategoriaPadre(int idCategoriaPadre){
        List<Categoria> ListadoCategoria= new ArrayList<Categoria>();
        Cursor c = dbManager.Select(CategoriaModel.NAME_TABLE, new String[] { "*" },CategoriaModel.CATEGORIA_PADRE + "=?",new String[] {String.valueOf(idCategoriaPadre)});
        if (c.moveToFirst()){
            do {
                Categoria categoria = new Categoria();
                categoria.serialize(c);
                ListadoCategoria.add(categoria);
            }
            while (c.moveToNext());
        }
        return ListadoCategoria;
    }

    private void serialize(Cursor cursor) {
        this.idCategoria = cursor.getInt(cursor.getColumnIndex(CategoriaModel.PK));
        this.codigo = cursor.getString(cursor.getColumnIndex(CategoriaModel.CODIGO));
        this.descripcion = cursor.getString(cursor.getColumnIndex(CategoriaModel.DESCRIPCION));
        this.estado = cursor.getInt(cursor.getColumnIndex(CategoriaModel.ESTADO))==1;
        int idCategoriaPadre = cursor.getInt(cursor.getColumnIndex(CategoriaModel.CATEGORIA_PADRE));
        if (idCategoriaPadre > 0){
            this.categoriaPadre = new Categoria();
            this.categoriaPadre.getById(idCategoriaPadre);
        }
    }
}
