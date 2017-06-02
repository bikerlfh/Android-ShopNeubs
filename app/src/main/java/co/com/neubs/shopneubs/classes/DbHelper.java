package co.com.neubs.shopneubs.classes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import co.com.neubs.shopneubs.models.CategoriaModel;
import co.com.neubs.shopneubs.models.MarcaModel;
import co.com.neubs.shopneubs.models.ProductoModel;
import co.com.neubs.shopneubs.models.SaldoInventarioModel;


/**
 * Created by TatisRamos on 13/05/2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "shopneubs.sqlite";
    private static final int DB_SCHEMA_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_SCHEMA_VERSION);
    }


    public void onCreate (SQLiteDatabase db){
        db.execSQL(CategoriaModel.CREATE_TABLE);
        db.execSQL(MarcaModel.CREATE_TABLE);
        db.execSQL(ProductoModel.CREATE_TABLE);
        db.execSQL(SaldoInventarioModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion)
        {

        }
    }
}


