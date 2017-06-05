package co.com.neubs.shopneubs.interfaces;

import android.database.Cursor;

/**
 * Created by bikerlfh on 6/4/17.
 */

public interface ICrud {
    boolean save();
    /**
     * Verifica si existe el registro en la base de datos
     * @return
     */
    boolean exists();
    boolean getById(int id);
}
