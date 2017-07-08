package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.interfaces.ICrud;
import co.com.neubs.shopneubs.models.DepartamentoModel;

/**
 * Created by bikerlfh on 6/13/17.
 */

public class Departamento implements ICrud {
    @SerializedName("pk")
    private int idDepartamento;
    @SerializedName("pais")
    private int idPais;
    private String codigo;
    private String descripcion;

    private transient DbManager dbManager = DbManager.getInstance();

    public Departamento(int idDepartamento,int idPais,String codigo, String descripcion){
        this.idDepartamento = idDepartamento;
        this.idPais = idPais;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }
    public Departamento(){
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public int getIdPais() {
        return idPais;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
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
        contentValues.put(DepartamentoModel.PK,this.idDepartamento);
        contentValues.put(DepartamentoModel.ID_PAIS,this.idPais);
        contentValues.put(DepartamentoModel.CODIGO,this.codigo);
        contentValues.put(DepartamentoModel.DESCRIPCION,this.descripcion);

        return (dbManager.Insert(DepartamentoModel.NAME_TABLE,contentValues));
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
        return dbManager.exists(DepartamentoModel.NAME_TABLE,DepartamentoModel.PK,idDepartamento);
    }

    @Override
    public boolean getById(int id) {
        Cursor c = dbManager.Select(DepartamentoModel.NAME_TABLE, new String[] { "*" },DepartamentoModel.PK + "=?",new String[] {String.valueOf(id)});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    public boolean getDepartamentoByCodigo(String codigo){
        Cursor c = dbManager.Select(DepartamentoModel.NAME_TABLE, new String[] { "*" },DepartamentoModel.CODIGO + "=?",new String[] {codigo});
        if (c.moveToFirst()){
            serialize(c);
            return true;
        }
        return false;
    }

    public List<Departamento> getByIdPais(int idPais){
        List<Departamento> listDepartamento = null;
        Cursor c = dbManager.Select(DepartamentoModel.NAME_TABLE, new String[] { "*" },DepartamentoModel.ID_PAIS + "=?",new String[]{String.valueOf(idPais)});
        if (c.moveToFirst()){
            listDepartamento = new ArrayList<>();
            do {
                Departamento td = new Departamento();
                td.serialize(c);
                listDepartamento.add(td);
            }while (c.moveToNext());
        }
        return  listDepartamento;
    }

    private void serialize(Cursor c){
        this.idDepartamento = c.getInt(c.getColumnIndex(DepartamentoModel.PK));
        this.idPais = c.getInt(c.getColumnIndex(DepartamentoModel.ID_PAIS));
        this.codigo = c.getString(c.getColumnIndex(DepartamentoModel.CODIGO));
        this.descripcion = c.getString(c.getColumnIndex(DepartamentoModel.DESCRIPCION));
    }
}
