package co.com.neubs.shopneubs.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import co.com.neubs.shopneubs.models.APISincronizacionModel;
import co.com.neubs.shopneubs.models.APITablaModel;
import co.com.neubs.shopneubs.models.CategoriaModel;
import co.com.neubs.shopneubs.models.ItemCarModel;
import co.com.neubs.shopneubs.models.MarcaModel;
import co.com.neubs.shopneubs.models.ProductoModel;
import co.com.neubs.shopneubs.models.SaldoInventarioModel;
import co.com.neubs.shopneubs.models.UsuarioModel;

/**
 * Created by TatisRamos on 13/05/2016.
 * Update by bikerlfh on 01/06/2017 (Se agrega los metodos de Select sin todos los parametros y SelectAll)
 */

public class DbManager
{
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    public  Context context;

    //public static final String QueryInsertFormatos = "insert into formato(codigo)";

    public DbManager(Context context) {
        this.context = context;
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public boolean Insert(String table,ContentValues values)
    {
        if(db.insert(table, null, values) > 0)
            return true;
        return false;
    }

    public boolean Update(String table, ContentValues values, String whereClause, String[] whereArgs)
    {
        //String table, ContentValues values, String whereClause, String[] whereArgs
        if(db.update(table, values, whereClause, whereArgs)>0)
            return true;
        return false;
    }

    public boolean Delete(String table, String whereClause, String[] whereArgs)
    {
        if(db.delete(table, whereClause, whereArgs) > 0)
            return true;
        return false;
    }

    public Cursor Select(String table, String[] columns, String selection, String[] selectionArgs,
                         String groupBy, String having, String orderBy, String limit)
    {
        return db.query(table, columns, selection, selectionArgs, groupBy,having, orderBy,null);
    }

    public Cursor Select(String table, String[] columns, String selection, String[] selectionArgs)
    {
        return db.query(table, columns, selection, selectionArgs, null,null, null,null);
    }

    public Cursor Select(String table, String[] columns)
    {
        return db.query(table, columns, null, null,null, null,null);
    }

    public Cursor SelectAll(String table)
    {
        return db.query(table, new String[]{"*"}, null, null,null, null,null);
    }


    public Cursor RawQuery(String sql, String[] selectionArgs)
    {
        return db.rawQuery(sql, selectionArgs);

    }

    /**
     * Verifica si un registro existe en la base de datos
     * @param NAME_TABLE Nombre de la tabla
     * @param COLUMN_PK Nombre de la columna pk
     * @param pk_value valor del PK
     * @return
     */
    public boolean exists(String NAME_TABLE,String COLUMN_PK,int pk_value){
        Cursor c = Select(NAME_TABLE, new String[] { "*" },COLUMN_PK + "=?",new String[] {String.valueOf(pk_value)});
        if (c.moveToFirst())
            return true;
        return false;
    }

    public class DbHelper extends SQLiteOpenHelper {

        public static final String DB_NAME = "shopneubs.sqlite";
        private static final int DB_SCHEMA_VERSION = 1;

        public DbHelper(Context context) {
            super(context, DB_NAME, null, DB_SCHEMA_VERSION);
        }


        public void onCreate (SQLiteDatabase db){
            db.execSQL(CategoriaModel.CREATE_TABLE);
            db.execSQL(MarcaModel.CREATE_TABLE);
            db.execSQL(ProductoModel.CREATE_TABLE);
            db.execSQL(SaldoInventarioModel.CREATE_TABLE);
            db.execSQL(APITablaModel.CREATE_TABLE);
            db.execSQL(APISincronizacionModel.CREATE_TABLE);
            db.execSQL(UsuarioModel.CREATE_TABLE);
            db.execSQL(ItemCarModel.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion < newVersion)
            {

            }
        }
    }
}