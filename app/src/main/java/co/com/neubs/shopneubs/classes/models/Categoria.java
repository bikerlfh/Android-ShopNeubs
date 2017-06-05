package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.classes.DbManager;
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

    public transient Categoria categoriaPadre;

    private transient DbManager dbManager;

    public Categoria(int idCategoria,String codigo, String descripcion,int idCategoriaPadre){
        this.idCategoria = idCategoria;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.idCategoriaPadre = idCategoriaPadre;
        if (idCategoriaPadre > 0){
            this.categoriaPadre = new Categoria(this.dbManager.context);
            this.categoriaPadre.getById(idCategoriaPadre);
        }
    }

    public Categoria(Context context){
        this.initDbManager(context);
    }

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
        return descripcion;
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

    public void initDbManager(Context context){
        this.dbManager = new DbManager(context);
    }

    @Override
    public boolean getById(int id) {
        Cursor c = dbManager.Select(CategoriaModel.NAME_TABLE, new String[] { "*" },CategoriaModel.PK + "=?",new String[] {String.valueOf(idCategoria)});
        if (c.moveToFirst())
        {
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

        if(dbManager.Insert(CategoriaModel.NAME_TABLE,contentValues))
            return true;
        return false;
    }

    @Override
    public boolean exists() {
        return dbManager.exists(CategoriaModel.NAME_TABLE,CategoriaModel.PK,idCategoria);
    }

    public boolean getCategoriaByCodigo(String codigo){
        Cursor c = dbManager.Select(CategoriaModel.NAME_TABLE, new String[] { "*" },CategoriaModel.CODIGO + "=?",new String[] {codigo});
        if (c.moveToFirst())
        {
            serialize(c);
            return true;
        }
        return false;
    }

    public List<Categoria> getListCategoriaByCategoriaPadre(int idCategoriaPadre){
        List<Categoria> ListadoCategoria= new ArrayList<Categoria>();
        Cursor c = dbManager.Select(CategoriaModel.NAME_TABLE, new String[] { "*" },CategoriaModel.CATEGORIA_PADRE + "=?",new String[] {String.valueOf(idCategoriaPadre)});
        if (c.moveToFirst())
        {
            do {
                Categoria categoria = new Categoria(this.dbManager.context);
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
        int idCategoriaPadre = cursor.getInt(cursor.getColumnIndex(CategoriaModel.CATEGORIA_PADRE));
        if (idCategoriaPadre > 0){
            this.categoriaPadre = new Categoria(this.dbManager.context);
            this.categoriaPadre.getById(idCategoriaPadre);
        }
    }
}
