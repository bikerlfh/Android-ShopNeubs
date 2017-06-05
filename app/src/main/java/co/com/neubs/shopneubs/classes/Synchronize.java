package co.com.neubs.shopneubs.classes;

import android.content.Context;

import android.util.Log;

import co.com.neubs.shopneubs.classes.models.APISincronizacion;
import co.com.neubs.shopneubs.classes.models.APITabla;
import co.com.neubs.shopneubs.classes.models.Categoria;
import co.com.neubs.shopneubs.classes.models.Marca;

/**
 * Created by bikerlfh on 6/2/17.
 */

public class Synchronize {

    private Context context;
    public static String message_error;

    public Synchronize(Context context) {
        this.context = context;
    }

    /**
     * Sincronización inicial (debe ser llamada cuando se abre la aplicación por primera vez)
     * @return
     */
    public boolean InitialSyncronize(){
        if (SyncronizeApiTabla()){
            if (SyncronizeCategorias()){
                if (SyncronizeMarcas()){
                    if (SyncronizeApiSincronizacion())
                        return true;
                }
            }
        }
        return false;
    }


    /**
     * Sincroniza el registro ApiSincronizacion segun el idApiTabla
     * @param idApiTabla
     * @return
     */
    public boolean SyncronizeApiSincronizacion(int idApiTabla){
        final String url = "api-sincronizacion/?tabla="+idApiTabla;

        APISincronizacion apiSincronizacion = APIRest.Sync.get(url,APISincronizacion.class);
        apiSincronizacion.initDbManager(context);
        if (apiSincronizacion != null && !apiSincronizacion.exists(apiSincronizacion.getIdApiSincronizacion())){
            apiSincronizacion.save();
            return true;

        }
        message_error = "ERROR al sincronizar api";
        return false;
    }

    /**
     * Sincroniza toda la tabla ApiSincronizcion
     * @return
     */
    public boolean SyncronizeApiSincronizacion(){
        final APISincronizacion[] listApiSincronizacion = APIRest.Sync.get("api-sincronizacion/",APISincronizacion[].class);
        if (listApiSincronizacion != null && listApiSincronizacion.length > 0){
            for (APISincronizacion apiSincronizacion: listApiSincronizacion) {
                apiSincronizacion.initDbManager(context);
                if (apiSincronizacion != null && !apiSincronizacion.exists(apiSincronizacion.getIdApiSincronizacion())) {
                    apiSincronizacion.save();
                }
            }
            return  true;
        }
        message_error = "ERROR al sincronizar api";
        return false;
    }

    /**
     * Sincroniza ApiTabla
     * @return
     */
    public boolean SyncronizeApiTabla(){
        try {
                        // Se consulta la api y se obtiene un arreglo tipo APITabla[]
            final APITabla[] listadoAPITabla = APIRest.Sync.get("api-tabla/",APITabla[].class);
            if (listadoAPITabla != null && listadoAPITabla.length > 0) {
                for (APITabla tabla : listadoAPITabla) {
                    tabla.initDbManager(context);
                    // Si la tabla no está creada en la base de datos se guarda
                    if (!tabla.exists()) {
                        tabla.save();
                    }
                }
            }
            return true;
        }
        catch (Exception ex){
            Log.d("SyncronizeApiTabla",ex.getMessage());
            message_error = ex.getMessage();
        }
        return false;
    }

    /**
     * Realiza la sincronización de las marcas
     * @return
     */
    public boolean SyncronizeMarcas(){
        try {
            final Marca[] listMarca = APIRest.Sync.get("marca/",Marca[].class);
            if (listMarca != null && listMarca.length > 0) {
                for (Marca marca : listMarca) {
                    marca.initDbManager(context);
                    if (!marca.exists()) {
                        marca.save();
                    }
                }
            }
            return true;
        }
        catch (Exception ex){
            Log.d("SincronizacionMarcas",ex.getMessage());
            message_error = ex.getMessage();
        }
        return false;
    }

    /**
     * Realiza la sincronización de las Categorias
     * @return
     */
    public boolean SyncronizeCategorias(){
        try{
            final Categoria[] listCategoria = APIRest.Sync.get("categoria/",Categoria[].class);
            if (listCategoria != null && listCategoria.length > 0) {
                for (Categoria cat : listCategoria) {
                    cat.initDbManager(context);
                    if (!cat.exists()) {
                        cat.save();
                    }
                }
            }
            return true;
        }
        catch (Exception ex){
            Log.d("SyncronizeCategorias",ex.getMessage());
            message_error = ex.getMessage();
        }
        return false;
    }

}
