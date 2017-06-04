package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.models.CategoriaModel;

/**
 * Created by bikerlfh on 5/22/17.
 */

public class Categoria {
    private int idCategoria;
    private String codigo;
    private String descripcion;
    private int idCategoriaPadre;

    public Categoria categoriaPadre;

    private DbManager dbManager;

    public Categoria(int idCategoria,String codigo, String descripcion,int idCategoriaPadre){
        this.idCategoria = idCategoria;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.idCategoriaPadre = idCategoriaPadre;
        if (idCategoriaPadre > 0){
            this.categoriaPadre = new Categoria(this.dbManager.context);
            this.categoriaPadre.getCategoriaByid(idCategoriaPadre);
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

    public boolean getCategoriaByid(int idCategoria){
        Cursor c = dbManager.Select(CategoriaModel.NAME_TABLE, new String[] { "*" },CategoriaModel.PK + "=?",new String[] {String.valueOf(idCategoria)});
        if (c.moveToFirst())
        {
            serializeCategoria(c);
            return true;
        }
        return false;
    }

    public boolean getCategoriaByCodigo(String codigo){
        Cursor c = dbManager.Select(CategoriaModel.NAME_TABLE, new String[] { "*" },CategoriaModel.CODIGO + "=?",new String[] {codigo});
        if (c.moveToFirst())
        {
            serializeCategoria(c);
            return true;
        }
        return false;
    }

    public List<Categoria> getLisCategoriaByCategoriaPadre(int idCategoriaPadre){
        List<Categoria> ListadoCategoria= new ArrayList<Categoria>();
        Cursor c = dbManager.Select(CategoriaModel.NAME_TABLE, new String[] { "*" },CategoriaModel.CATEGORIA_PADRE + "=?",new String[] {String.valueOf(idCategoriaPadre)});
        if (c.moveToFirst())
        {
            do {
                Categoria categoria = new Categoria(this.dbManager.context);
                categoria.serializeCategoria(c);
                ListadoCategoria.add(categoria);
            }
            while (c.moveToNext());
        }
        return ListadoCategoria;
    }

    private void serializeCategoria(Cursor c){
        this.idCategoria = c.getInt(c.getColumnIndex(CategoriaModel.PK));
        this.codigo = c.getString(c.getColumnIndex(CategoriaModel.CODIGO));
        this.descripcion = c.getString(c.getColumnIndex(CategoriaModel.DESCRIPCION));
        int idCategoriaPadre = c.getInt(c.getColumnIndex(CategoriaModel.CATEGORIA_PADRE));
        if (idCategoriaPadre > 0){
            this.categoriaPadre = new Categoria(this.dbManager.context);
            this.categoriaPadre.getCategoriaByid(idCategoriaPadre);
        }
    }
}
