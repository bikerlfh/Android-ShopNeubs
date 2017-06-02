package co.com.neubs.shopneubs.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by TatisRamos on 13/05/2016.
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

    public Cursor RawQuery(String sql, String[] selectionArgs)
    {
        return db.rawQuery(sql, selectionArgs);
    }
}