package co.com.neubs.shopneubs.classes.models;

import android.content.Context;
import android.database.Cursor;

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
    private Categoria categoriaPadre;

    private DbManager dbManager;

    public Categoria(int idCategoria,String codigo, String descripcion,int idCategoriaPadre){
        this.idCategoria = idCategoria;
        this.codigo = codigo;
        this.descripcion = descripcion;
        if (idCategoriaPadre > 0){
            this.categoriaPadre = new Categoria(this.dbManager.context);
            this.categoriaPadre.getCategoriaByid(idCategoriaPadre);
        }
    }
    public Categoria(Context context){
        this.dbManager = new DbManager(context);
    }


    public boolean getCategoriaByid(int idCategoria){
        Cursor c = dbManager.Select(CategoriaModel.NAME_TABLE, new String[] { "*" },CategoriaModel.PK + "=?",new String[] {String.valueOf(idCategoria)},null,null,null,null);
        if (c.moveToFirst())
        {
            serializeCategoria(c);
            return true;
        }
        return false;
    }

    public boolean getCategoriaByCodigo(String codigo){
        Cursor c = dbManager.Select(CategoriaModel.NAME_TABLE, new String[] { "*" },CategoriaModel.CODIGO + "=?",new String[] {codigo},null,null,null,null);
        if (c.moveToFirst())
        {
            serializeCategoria(c);
            return true;
        }
        return false;
    }

    public List<Categoria> getLisCategoriaByCategoriaPadre(int idCategoriaPadre){
        List<Categoria> ListadoCategoria= new ArrayList<Categoria>();
        Cursor c = dbManager.Select(CategoriaModel.NAME_TABLE, new String[] { "*" },CategoriaModel.CATEGORIA_PADRE + "=?",new String[] {String.valueOf(idCategoriaPadre)},null,null,null,null);
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
