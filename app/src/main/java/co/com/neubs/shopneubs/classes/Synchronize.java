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

    /**
     * Sincronización inicial
     * @param context
     * @return
     */
    public static boolean InitialSyncronize(Context context){
        APITabla apiTabla = new APITabla(context);
        if (SyncronizeApiTabla(context)){
           if (SyncronizeCategorias(context)){
                if (SyncronizeMarcas(context)){
                    return true;
                }
           }
        }
        return false;
    }


    public static boolean SyncronizeApiSincronizacion(Context context, int idApiTabla){
        final String url = "api-sincronizacion/" + ((idApiTabla > 0)? "?tabla="+idApiTabla : "");

        if (idApiTabla > 0){
            APISincronizacion apiSincronizacion = APIRest.Sync.get(url,APISincronizacion.class);
            if (apiSincronizacion != null && !apiSincronizacion.exists(apiSincronizacion.getIdApiSincronizacion())){
                apiSincronizacion.save();
                //SyncronizeApiSincronizacion(context,null);
            }
        }
        return false;
    }
    /**
     * Sincroniza ApiTabla
     * @param context
     * @return
     */
    public static boolean SyncronizeApiTabla(Context context){
        try {
            APITabla apiTabla = new APITabla(context);

            // Se consulta la api y se obtiene un arreglo tipo APITabla[]
            final APITabla[] listadoAPITabla = APIRest.Sync.get("api-tabla/",APITabla[].class);
            if (listadoAPITabla != null && listadoAPITabla.length > 0) {
                for (APITabla tabla : listadoAPITabla) {
                    // Si la tabla no está creada en la base de datos se guarda
                    if (!apiTabla.getAPITablaByid(tabla.getIdApiTabla())) {
                        tabla.initDbManager(context);
                        tabla.save();
                    }
                }
            }
            return true;
        }
        catch (Exception ex){
            Log.d("SyncronizeApiTabla",ex.getMessage());
        }
        return false;
    }

    /**
     * Realiza la sincronización de las marcas
     * @param context
     * @return
     */
    public static boolean SyncronizeMarcas(Context context){
        try {
            final Marca[] marcas = APIRest.Sync.get("marca/",Marca[].class);
            if (marcas != null && marcas.length > 0) {
                Marca marca = new Marca(context);
                for (Marca m : marcas) {
                    if (!marca.getMarcaByid(m.getIdMarca())) {
                        m.initDbManager(context);
                        m.save();
                    }
                }
            }
            return true;
        }
        catch (Exception ex){
            Log.d("SincronizacionMarcas",ex.getMessage());
        }
        return false;
    }

    /**
     * Realiza la sincronización de las Categorias
     * @param context
     * @return
     */
    public static boolean SyncronizeCategorias(Context context){
        try{
            final Categoria[] categorias = APIRest.Sync.get("",Categoria[].class);
            if (categorias != null && categorias.length > 0) {
                Categoria categoria = new Categoria(context);
                for (Categoria cat : categorias) {
                    if (!categoria.getCategoriaByid(cat.getIdCategoria())) {
                        cat.initDbManager(context);
                        cat.save();
                    }
                }
            }
            return true;
        }
        catch (Exception ex){
            Log.d("SyncronizeCategorias",ex.getMessage());
        }
        return false;
    }

}
