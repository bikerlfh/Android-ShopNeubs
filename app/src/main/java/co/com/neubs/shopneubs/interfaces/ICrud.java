package co.com.neubs.shopneubs.interfaces;

/**
 * Created by bikerlfh on 6/4/17.
 */

public interface ICrud {
    boolean save();
    /**
     * Verifica si existe el registro en la base de datos
     * @return
     */
    boolean update();
    boolean delete();
    boolean exists();
    boolean getById(int id);

}
