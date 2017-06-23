package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.interfaces.ICrud;
import co.com.neubs.shopneubs.models.MunicipioModel;

/**
 * Created by bikerlfh on 6/13/17.
 */

public class Municipio implements ICrud {
    @SerializedName("pk")
    private int idMunicipio;
    @SerializedName("departamento")
    private int idDepartamento;
    private String codigo;
    private String descripcion;

    private transient DbManager dbManager = DbManager.getInstance();

    public Municipio(int idMunicipio,int idDepartamento,String codigo, String descripcion){
        this.idMunicipio = idMunicipio;
        this.idDepartamento = idDepartamento;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }
    public Municipio(){
    }

    public int getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(int idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
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

    @Override
    public String toString() {
        return this.descripcion;
    }

    @Override
    public boolean save() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MunicipioModel.PK,this.idMunicipio);
        contentValues.put(MunicipioModel.ID_DEPARTAMENTO,this.idDepartamento);
        contentValues.put(MunicipioModel.CODIGO,this.codigo);
        contentValues.put(MunicipioModel.DESCRIPCION,this.descripcion);

        return (dbManager.Insert(MunicipioModel.NAME_TABLE,contentValues));
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
        return dbManager.exists(MunicipioModel.NAME_TABLE,MunicipioModel.PK,idMunicipio);
    }

    @Override
    public boolean getById(int id) {
        Cursor c = dbManager.Select(MunicipioModel.NAME_TABLE, new String[] { "*" },MunicipioModel.PK + "=?",new String[] {String.valueOf(id)});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    public boolean getMunicipioByCodigo(String codigo){
        Cursor c = dbManager.Select(MunicipioModel.NAME_TABLE, new String[] { "*" },MunicipioModel.CODIGO + "=?",new String[] {codigo});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    public List<Municipio> getByIdDepartamento(int idDepartamento){
        List<Municipio> listMunicipio = null;
        Cursor c = dbManager.Select(MunicipioModel.NAME_TABLE, new String[] { "*" },MunicipioModel.ID_DEPARTAMENTO + "=?",new String[]{String.valueOf(idDepartamento)});
        if (c.moveToFirst()){
            listMunicipio = new ArrayList<>();
            do {
                Municipio td = new Municipio();
                td.serialize(c);
                listMunicipio.add(td);
            }while (c.moveToNext());
        }
        return  listMunicipio;
    }

    private void serialize(Cursor c){
        this.idMunicipio = c.getInt(c.getColumnIndex(MunicipioModel.PK));
        this.idDepartamento = c.getInt(c.getColumnIndex(MunicipioModel.ID_DEPARTAMENTO));
        this.codigo = c.getString(c.getColumnIndex(MunicipioModel.CODIGO));
        this.descripcion = c.getString(c.getColumnIndex(MunicipioModel.DESCRIPCION));
    }
}
