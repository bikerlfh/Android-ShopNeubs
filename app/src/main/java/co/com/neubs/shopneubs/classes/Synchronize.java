package co.com.neubs.shopneubs.classes;

import android.content.Context;
import co.com.neubs.shopneubs.classes.models.APISincronizacion;
import co.com.neubs.shopneubs.classes.models.APITabla;
import co.com.neubs.shopneubs.classes.models.Categoria;
import co.com.neubs.shopneubs.classes.models.Marca;
import co.com.neubs.shopneubs.classes.models.Pais;
import co.com.neubs.shopneubs.classes.models.TipoDocumento;

/**
 * Created by bikerlfh on 6/2/17.
 */

public class Synchronize {

    // VARIABLES URLS API
    private final String URL_API_SINCRONIZACION = "sync/api-sincronizacion/";
    private final String URL_API_TABLA = "sync/api-tabla/";
    private final String URL_CATEGORIA = "sync/categoria/";
    private final String URL_MARCA = "sync/marca/";
    private final String URL_TIPO_DOCUMENTO = "sync/tipo-documento/";
    private final String URL_PAIS = "sync/pais/";
    private final String URL_DEPARTAMENTO = "sync/departamento/";
    private final String URL_MUNICIPIO = "sync/municipio/";

    private Context context;

    public String message_error;


    public Synchronize(Context context) {
        this.context = context;
    }

    /**
     * Sincronización inicial (debe ser llamada cuando se abre la aplicación por primera vez)
     * @return
     */
    public int InitialSyncronize(){
        int totalRowSync = 0;
        try {
            // Se sincroniza la API
            totalRowSync += SyncronizeAPI(false);
            // Se sincroniza la APITabla
            totalRowSync += SyncronizeApiTabla();
            // Se sincroniza las categorias
            totalRowSync += SyncronizeCategorias();
            // Se sincroniza las marcas
            totalRowSync += SyncronizeMarcas();
            // Se sincroniza los tipos de documentos
            //totalRowSync += SyncronizeTipoDocumento();
            // Se sincroniza los paises
            //totalRowSync += SyncronizePais();
        }
        catch (Exception ex){
            message_error = ex.getMessage();
            return -1;
        }
        return totalRowSync;
    }


    /**
     * Sincroniza ApiTabla
     * @return un el numero de sincronzaciones guardadas.
     */
    public int SyncronizeApiTabla(){
        int numSincronizacion = 0;

        // Se consulta la api y se obtiene un arreglo tipo APITabla[]
        final APITabla[] listadoAPITabla = APIRest.Sync.getSerializedObjectFromGETRequest(URL_API_TABLA,APITabla[].class);
        if (listadoAPITabla != null && listadoAPITabla.length > 0) {
            for (APITabla tabla : listadoAPITabla) {
                tabla.initDbManager(context);
                // Si la tabla no está creada en la base de datos se guarda
                if (!tabla.exists()) {
                    tabla.save();
                    numSincronizacion++;
                }
            }
        }
        return numSincronizacion;
    }
    /**
     * Sincroniza el registro ApiSincronizacion segun el idApiTabla
     * @param idApiTabla
     * @return
     */
    public int SyncronizeAPI(int idApiTabla){
        final String url = URL_API_SINCRONIZACION + "?tabla="+idApiTabla;

        APISincronizacion apiSincronizacion = APIRest.Sync.getSerializedObjectFromGETRequest(url,APISincronizacion.class);
        apiSincronizacion.initDbManager(context);
        if (apiSincronizacion != null && !apiSincronizacion.exists()){
            apiSincronizacion.save();
            return 1;
        }
        return 0;
    }

    /**
     * Sincroniza toda la tabla ApiSincronizacion
     * @param withTablas True, Sincroniza las tablas que la APISincronizacion especifique. False, solo sincroniza APISincronización
     * @return un el número de sincronzaciones guardadas.
     */
    public int SyncronizeAPI(boolean withTablas) {
        int numSincronizacion = 0;
        try {
            final APISincronizacion[] listApiSincronizacion = APIRest.Sync.getSerializedObjectFromGETRequest(URL_API_SINCRONIZACION, APISincronizacion[].class);
            if (listApiSincronizacion != null && listApiSincronizacion.length > 0) {
                for (APISincronizacion apiSincronizacion : listApiSincronizacion) {
                    apiSincronizacion.initDbManager(context);
                    if (apiSincronizacion != null && !apiSincronizacion.exists()) {
                        apiSincronizacion.save();
                        numSincronizacion++;

                        if (withTablas) {
                            SynchronizeAPITabla(apiSincronizacion.getTabla());
                        }
                    }
                }
            }
        }
        catch (Exception ex){
            message_error = ex.getMessage();
            numSincronizacion = -1;
        }
        return numSincronizacion;
    }

    public int SyncronizeTipoDocumento(){
        int numSincronizacion = 0;
        final TipoDocumento[] listTipoDocumento = APIRest.Sync.getSerializedObjectFromGETRequest(URL_TIPO_DOCUMENTO,TipoDocumento[].class);
        if (listTipoDocumento != null && listTipoDocumento.length > 0) {
            for (TipoDocumento tipoDocumento : listTipoDocumento) {
                tipoDocumento.initDbManager(context);
                if (!tipoDocumento.exists()) {
                    tipoDocumento.save();
                    numSincronizacion++;
                }
            }
        }
        return numSincronizacion;
    }

    public int SyncronizePais(){
        int numSincronizacion = 0;
        final Pais[] listPais = APIRest.Sync.getSerializedObjectFromGETRequest(URL_PAIS,Pais[].class);
        if (listPais != null && listPais.length > 0) {
            for (Pais pais : listPais) {
                pais.initDbManager(context);
                if (!pais.exists()) {
                    pais.save();
                    numSincronizacion++;
                }
            }
        }
        return numSincronizacion;
    }

    /**
     * Realiza la sincronización de las marcas
     * @return
     */
    public int SyncronizeMarcas(){
        int numSincronizacion = 0;
        final Marca[] listMarca = APIRest.Sync.getSerializedObjectFromGETRequest(URL_MARCA,Marca[].class);
        if (listMarca != null && listMarca.length > 0) {
            for (Marca marca : listMarca) {
                marca.initDbManager(context);
                if (!marca.exists()) {
                    marca.save();
                    numSincronizacion++;
                }
            }
        }
        return numSincronizacion;
    }

    /**
     * Realiza la sincronización de las Categorias
     * @return
     */
    public int SyncronizeCategorias(){
        int numSincronizacion = 0;

        final Categoria[] listCategoria = APIRest.Sync.getSerializedObjectFromGETRequest(URL_CATEGORIA,Categoria[].class);
        if (listCategoria != null && listCategoria.length > 0) {
            for (Categoria cat : listCategoria) {
                cat.initDbManager(context);
                if (!cat.exists()) {
                    cat.save();
                    numSincronizacion++;
                }
            }
        }
        return numSincronizacion;
    }

    /**
     * Sincroniza una tabla en especifico. Si la tabla es null, realiza una sincronización inicial
     * @param tabla
     * @return
     */
    private int SynchronizeAPITabla(APITabla tabla){
        int numSincronizacion = 0;
        if (tabla != null) {
            switch (tabla.getCodigo()) {
                // APITablas
                case "01":
                    numSincronizacion = SyncronizeApiTabla();
                    break;
                // Categorias
                case "02":
                    numSincronizacion = SyncronizeCategorias();
                    break;
                // Marcas
                case "03":
                    numSincronizacion = SyncronizeMarcas();
                    break;
                // Productos
                case "04":
                    break;
            }
        }
        else{
            // Se sincroniza todas las tablas
            numSincronizacion = InitialSyncronize();
        }
        return numSincronizacion;
    }

}
