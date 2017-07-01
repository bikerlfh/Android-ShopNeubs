package co.com.neubs.shopneubs.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import co.com.neubs.shopneubs.models.APISectionModel;
import co.com.neubs.shopneubs.models.APISincronizacionModel;
import co.com.neubs.shopneubs.models.APITablaModel;
import co.com.neubs.shopneubs.models.APIBannerModel;
import co.com.neubs.shopneubs.models.CategoriaModel;
import co.com.neubs.shopneubs.models.DepartamentoModel;
import co.com.neubs.shopneubs.models.ItemCarModel;
import co.com.neubs.shopneubs.models.MarcaModel;
import co.com.neubs.shopneubs.models.MunicipioModel;
import co.com.neubs.shopneubs.models.PaisModel;
import co.com.neubs.shopneubs.models.ProductoModel;
import co.com.neubs.shopneubs.models.SaldoInventarioModel;
import co.com.neubs.shopneubs.models.SugerenciaBusquedaModel;
import co.com.neubs.shopneubs.models.TipoDocumentoModel;
import co.com.neubs.shopneubs.models.UsuarioModel;

/**
 * Created by TatisRamos on 13/05/2016.
 * Update by bikerlfh on 01/06/2017 (Se agrega los metodos de Select sin todos los parametros y SelectAll)
 */

public class DbManager
{
    // instancia
    private static DbManager instance;

    private DbHelper dbHelper;
    private SQLiteDatabase db;
    public  Context context;

    //public static final String QueryInsertFormatos = "insert into formato(codigo)";

    private long identity;

    /**
     * Obtiene el pk de la inserción
     * @return
     */
    public long getIdentity() {
        return identity;
    }

    /**
     * Inicializa la instancia
     * @param context
     */
    public static synchronized void initInstance(Context context){
        if (instance == null)
            instance = new DbManager(context.getApplicationContext());
    }

    /**
     * Obtiene la instancia. Si no esta inistanciada retorna una excepción
     * @return instancia de DbManager
     */
    public static synchronized DbManager getInstance(){
        if (instance == null)
            throw new IllegalStateException(DbManager.class .getSimpleName() +
                    " no esta inicializada, usted debe invocar primero el metodo initInstance");
        return instance;
    }

    /**
     * El constructor debe ser privado para evitar la instanciación directa
     * @param context
     */
    private DbManager(Context context) {
        this.context = context;
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public boolean Insert(String table,ContentValues values){
        identity = db.insert(table, null, values);
        return (identity > 0);
    }

    public boolean Update(String table, ContentValues values, String whereClause, String[] whereArgs){
        //String table, ContentValues values, String whereClause, String[] whereArgs
        return (db.update(table, values, whereClause, whereArgs)>0);
    }

    public boolean Update(String table, ContentValues values, String columnPk, int pk){
        return (db.update(table, values, columnPk + "=?", new String[] {String.valueOf(pk)})>0);
    }

    public boolean Delete(String table, String whereClause, String[] whereArgs){
        return (db.delete(table, whereClause, whereArgs) > 0);
    }

    public boolean Delete(String table, String columnPk, int pk){
        return (db.delete(table, columnPk + "=?", new String[] {String.valueOf(pk)}) > 0);
    }

    public Cursor Select(String table, String[] columns, String selection, String[] selectionArgs,
                         String groupBy, String having, String orderBy, String limit){
        return db.query(table, columns, selection, selectionArgs, groupBy,having, orderBy,null);
    }

    public Cursor Select(String table, String[] columns, String selection, String[] selectionArgs){
        return db.query(table, columns, selection, selectionArgs, null,null, null,null);
    }

    public Cursor Select(String table, String[] columns){
        return db.query(table, columns, null, null,null, null,null);
    }

    public Cursor SelectAll(String table){
        return db.query(table, new String[]{"*"}, null, null,null, null,null);
    }


    public Cursor RawQuery(String sql, String[] selectionArgs){
        return db.rawQuery(sql, selectionArgs);
    }

    /**
     * Verifica si un registro existe en la base de datos
     * @param nameTable Nombre de la tabla
     * @param columnPk Nombre de la columna pk
     * @param pk valor del PK
     * @return
     */
    public boolean exists(String nameTable,String columnPk,int pk){
        Cursor cursor = Select(nameTable, new String[] { "*" },columnPk + "=?",new String[] {String.valueOf(pk)});
        return  (cursor.moveToFirst());
    }
    public boolean exists(String nameTable,String columnPk,long pk){
        Cursor cursor = Select(nameTable, new String[] { "*" },columnPk + "=?",new String[] {String.valueOf(pk)});
        return  (cursor.moveToFirst());
    }

    public void close(){
        if (db != null && db.isOpen())
            db.close();
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
            db.execSQL(TipoDocumentoModel.CREATE_TABLE);
            db.execSQL(PaisModel.CREATE_TABLE);
            db.execSQL(DepartamentoModel.CREATE_TABLE);
            db.execSQL(MunicipioModel.CREATE_TABLE);
            db.execSQL(SugerenciaBusquedaModel.CREATE_TABLE);
            db.execSQL(APIBannerModel.CREATE_TABLE);
            db.execSQL(APISectionModel.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion < newVersion){

            }
        }
    }
}