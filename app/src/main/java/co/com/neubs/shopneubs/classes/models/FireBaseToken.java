package co.com.neubs.shopneubs.classes.models;

import android.content.ContentValues;
import android.database.Cursor;

import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.interfaces.ICrud;
import co.com.neubs.shopneubs.models.FireBaseTokenModel;

/**
 * Created by bikerlfh on 7/19/17.
 */

public class FireBaseToken implements ICrud {
    private int pk;
    private String token;

    private DbManager dbManager = DbManager.getInstance();

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public boolean save() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FireBaseTokenModel.TOKEN,this.token);
        return (dbManager.Insert(FireBaseTokenModel.NAME_TABLE,contentValues));
    }

    @Override
    public boolean update() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FireBaseTokenModel.TOKEN,this.token);
        return (dbManager.Update(FireBaseTokenModel.NAME_TABLE,contentValues,FireBaseTokenModel.PK,this.pk));
    }

    @Override
    public boolean delete() {
        return false;
    }
    // retorna si existe o no el registro
    @Override
    public boolean exists() {
        Cursor c = dbManager.Select(FireBaseTokenModel.NAME_TABLE,new String[] {"*"});
        if(c.moveToFirst()){
            serialize(c);
            c.close();
            return true;
        }
        return false;
    }

    @Override
    public boolean getById(int id) {
        return false;
    }

    private void serialize(Cursor c){
        this.pk = c.getInt(c.getColumnIndex(FireBaseTokenModel.PK));
        this.token = c.getString(c.getColumnIndex(FireBaseTokenModel.TOKEN));
    }
}
