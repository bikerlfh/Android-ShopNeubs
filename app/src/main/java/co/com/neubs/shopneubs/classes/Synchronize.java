package co.com.neubs.shopneubs.classes;

import co.com.neubs.shopneubs.classes.models.APIBanner;
import co.com.neubs.shopneubs.classes.models.APISection;
import co.com.neubs.shopneubs.classes.models.APISincronizacion;
import co.com.neubs.shopneubs.classes.models.APITabla;
import co.com.neubs.shopneubs.classes.models.Categoria;
import co.com.neubs.shopneubs.classes.models.Departamento;
import co.com.neubs.shopneubs.classes.models.Marca;
import co.com.neubs.shopneubs.classes.models.Municipio;
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
    private final String URL_BANNER = "sync/banner/";
    private final String URL_SECTION = "sync/section/";


    public String message_error;


    public Synchronize() {

    }

    /**
     * Sincronización inicial (debe ser llamada cuando se abre la aplicación por primera vez)
     * @return numero de filas sincronizadas
     */
    public int InitialSynchronize(){
        int totalRowSync = 0;
        try {
            // Se sincroniza la API
            totalRowSync += SynchronizeAPI(false);
            // Se sincroniza la APITabla
            totalRowSync += SynchronizeApiTabla();
            // Se sincroniza las categorias
            totalRowSync += SynchronizeCategorias();
            // Se sincroniza las marcas
            totalRowSync += SynchronizeMarcas();
            // Se sincroniza los tipos de documentos
            totalRowSync += SynchronizeTipoDocumento();
            // Se sincroniza los paises
            totalRowSync += SynchronizePais();
            // Se sincroniza el ApiBanner
            totalRowSync += SynchronizeAPIBanner();
            // Se sincroniza la ApiSection
            totalRowSync += SynchronizeAPISection();
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
    private int SynchronizeApiTabla(){
        int numSincronizacion = 0;

        // Se consulta la api y se obtiene un arreglo tipo APITabla[]
        final String response = APIRest.Sync.get(URL_API_TABLA);
        if (APIRest.Sync.ok()){
            final APITabla[] listadoAPITabla = APIRest.serializeObjectFromJson(response,APITabla[].class);
            if (listadoAPITabla != null && listadoAPITabla.length > 0) {
                for (APITabla tabla : listadoAPITabla) {
                    // Si la tabla no está creada en la base de datos se guarda
                    if (!tabla.exists()) {
                        tabla.save();
                        numSincronizacion++;
                    }
                }
            }
        }
        else
            numSincronizacion = -1;
        return numSincronizacion;
    }
    /**
     * Sincroniza el registro ApiSincronizacion segun el idApiTabla
     * @param idApiTabla de la tabla
     * @return numero de sincronizaciones guardadas
     */
    public int SynchronizeAPI(int idApiTabla){
        final String url = URL_API_SINCRONIZACION + "?tabla="+idApiTabla;

        final String response = APIRest.Sync.get(url);
        if(response != null && APIRest.Sync.ok()) {
            APISincronizacion apiSincronizacion = APIRest.serializeObjectFromJson(response, APISincronizacion.class);
            if (!apiSincronizacion.exists()) {
                apiSincronizacion.save();
                return 1;
            }
        }
        else
            return -1;
        return 0;
    }

    /**
     * Sincroniza toda la tabla ApiSincronizacion
     * @param withTablas True, Sincroniza las tablas que la APISincronizacion especifique. False, solo sincroniza APISincronización
     * @return un el número de sincronzaciones guardadas.
     */
    public int SynchronizeAPI(boolean withTablas) {
        int numSincronizacion = 0;

        final String response = APIRest.Sync.get(URL_API_SINCRONIZACION);
        if (response != null && APIRest.Sync.ok()) {
            final APISincronizacion[] listApiSincronizacion = APIRest.serializeObjectFromJson(response, APISincronizacion[].class);
            if (listApiSincronizacion != null && listApiSincronizacion.length > 0) {
                for (APISincronizacion apiSincronizacion : listApiSincronizacion) {
                    if (!apiSincronizacion.exists()) {

                        /* Se limpia el cache de Volley si existe alguna sincronización pendiente.
                        * solo se ejecuta la primera vez que se guarda la sincronización
                        */
                        if (numSincronizacion == 0)
                            Helper.clearCacheVolley();

                        apiSincronizacion.save();
                        numSincronizacion++;
                        // Sincroniza la tabla que se guarda
                        if (withTablas) {
                            SynchronizeAPITabla(apiSincronizacion.getTabla());
                        }
                    }
                }
            }
        }
        else
            numSincronizacion = -1;
        return numSincronizacion;
    }

    /**
     * Sincroniza la tabla TipoDocumento
     * @return numero de sincronizaciones guardadas
     */
    private int SynchronizeTipoDocumento(){
        int numSincronizacion = 0;
        final String response = APIRest.Sync.get(URL_TIPO_DOCUMENTO);
        if (response != null && APIRest.Sync.ok()) {
            final TipoDocumento[] listTipoDocumento = APIRest.serializeObjectFromJson(response, TipoDocumento[].class);
            if (listTipoDocumento != null && listTipoDocumento.length > 0) {
                for (TipoDocumento tipoDocumento : listTipoDocumento) {
                    if (!tipoDocumento.exists()) {
                        tipoDocumento.save();
                        numSincronizacion++;
                    }
                }
            }
        }
        else
            numSincronizacion = -1;
        return numSincronizacion;
    }

    /**
     * Sincroniza la tabla PAIS
     * @return numero de sincronizaciones guardadas
     */
    private int SynchronizePais(){
        int numSincronizacion = 0;
        final String response = APIRest.Sync.get(URL_PAIS);
        if (response != null && APIRest.Sync.ok()) {
            final Pais[] listPais = APIRest.serializeObjectFromJson(response, Pais[].class);
            if (listPais != null && listPais.length > 0) {
                for (Pais pais : listPais) {
                    if (!pais.exists()) {
                        pais.save();
                        numSincronizacion++;
                    }
                }
            }
        }
        else
            numSincronizacion = -1;
        return numSincronizacion;
    }

    /**
     * sincroniza los departamentos.
     * Este método puede sincronizar todos los departamentos pasando como parametro idPais=0
     * No debe sincronizarse todos los departamentos
     * @param idPais del pais
     * @return numero de sincronizaciones guardadas
     */
    public int SynchronizeDepartamento(int idPais){
        int numSincronizacion = 0;
        String url = idPais > 0? URL_DEPARTAMENTO.concat("?idPais"+String.valueOf(idPais)):URL_DEPARTAMENTO;
        final String response = APIRest.Sync.get(url);
        if (response != null && APIRest.Sync.ok()) {
            final Departamento[] listDepartamento = APIRest.serializeObjectFromJson(response, Departamento[].class);
            if (listDepartamento != null && listDepartamento.length > 0) {
                for (Departamento departamento : listDepartamento) {
                    if (!departamento.exists()) {
                        departamento.save();
                        numSincronizacion++;
                    }
                }
            }
        }
        else
            numSincronizacion = -1;
        return numSincronizacion;
    }


    /**
     * sincroniza los municipio.
     * Este método puede sincronizar todos los municipio pasando como parametro idDepartamento=0
     * No debe sincronizarse todos los municipio
     * @param idDepartamento del departamento
     * @return numero de sincronizaciones guardadas
     */
    public int SynchronizeMunicipio(int idDepartamento){
        int numSincronizacion = 0;
        String url = idDepartamento > 0? URL_MUNICIPIO.concat("?idPais"+String.valueOf(idDepartamento)):URL_MUNICIPIO;
        final String response = APIRest.Sync.get(url);
        if (response != null && APIRest.Sync.ok()) {
            final Municipio[] listMunicipio = APIRest.serializeObjectFromJson(response, Municipio[].class);
            if (listMunicipio != null && listMunicipio.length > 0) {
                for (Municipio municipio : listMunicipio) {
                    if (!municipio.exists()) {
                        municipio.save();
                        numSincronizacion++;
                    }
                }
            }
        }
        else
            numSincronizacion = -1;
        return numSincronizacion;
    }

    /**
     * Realiza la sincronización de las marcas
     * @return numero de sincronizaciones guardadas
     */
    private int SynchronizeMarcas(){
        int numSincronizacion = 0;
        final String response = APIRest.Sync.get(URL_MARCA);
        if (response != null && APIRest.Sync.ok()) {
            final Marca[] listMarca = APIRest.serializeObjectFromJson(response, Marca[].class);
            if (listMarca != null && listMarca.length > 0) {
                for (Marca marca : listMarca) {
                    if (!marca.exists()) {
                        marca.save();
                        numSincronizacion++;
                    }
                }
            }
        }
        else
            numSincronizacion = -1;
        return numSincronizacion;
    }

    /**
     * Realiza la sincronización de las Categorias
     * @return numero de sincronizaciones guardadas
     */
    private int SynchronizeCategorias(){
        int numSincronizacion = 0;
        final String response = APIRest.Sync.get(URL_CATEGORIA);
        if (response != null && APIRest.Sync.ok()) {
            final Categoria[] listCategoria = APIRest.serializeObjectFromJson(response, Categoria[].class);
            if (listCategoria != null && listCategoria.length > 0) {
                for (Categoria cat : listCategoria) {
                    if (!cat.exists()) {
                        cat.save();
                        numSincronizacion++;
                    }
                }
            }
        }
        else
            numSincronizacion = -1;
        return numSincronizacion;
    }

    /**
     * Realiza la sincronización del ApiBanner
     * @return numero de sincronizaciones guardadas
     */
    private int SynchronizeAPIBanner(){
        int numSincronizacion = 0;
        final String response = APIRest.Sync.get(URL_BANNER);
        if (response != null && APIRest.Sync.ok()) {
            final APIBanner[] listApiBanner = APIRest.serializeObjectFromJson(response, APIBanner[].class);
            if (listApiBanner != null && listApiBanner.length > 0) {
                for (APIBanner banner : listApiBanner) {
                    // Si el banner no existe y está activo
                    if (!banner.exists()) {
                        if (banner.getEstado()) {
                            banner.save();
                            numSincronizacion++;
                        }
                    }
                    // Si el banner existe pero esta con estado false
                    // se elimina de la base de datos
                    else if(!banner.getEstado()){
                        banner.delete();
                        numSincronizacion++;
                    }
                }
            }
        }
        else
            numSincronizacion = -1;
        return numSincronizacion;
    }

    /**
     * Realiza la sincronización del ApiSection
     * @return numero de sincronizaciones guardadas
     */
    private int SynchronizeAPISection(){
        int numSincronizacion = 0;
        final String response = APIRest.Sync.get(URL_SECTION);
        if (response != null && APIRest.Sync.ok()) {
            final APISection[] listApiSection = APIRest.serializeObjectFromJson(response, APISection[].class);
            if (listApiSection != null && listApiSection.length > 0) {
                for (APISection section : listApiSection) {
                    // Si la section no existe y está activa
                    if (!section.exists()) {
                        if (section.getEstado()) {
                            section.save();
                            numSincronizacion++;
                        }
                    }
                    // Si la section existe pero esta con estado false
                    // se elimina de la base de datos
                    else if(!section.getEstado()){
                        section.delete();
                        numSincronizacion++;
                    }
                    else{
                        // se modifica por si el orden ah cambiado
                        section.update();
                    }
                }
            }
        }
        else
            numSincronizacion = -1;
        return numSincronizacion;
    }

    /**
     * Sincroniza una tabla en especifico. Si la tabla es null, realiza una sincronización inicial
     * @param tabla APITabla a sincronizar
     * @return numero de sincronizaciones guardadas
     */
    private int SynchronizeAPITabla(APITabla tabla){
        int numSincronizacion = 0;
        if (tabla != null) {
            switch (tabla.getCodigo()) {
                // APITablas
                case "01":
                    numSincronizacion = SynchronizeApiTabla();
                    break;
                // Categorias
                case "02":
                    numSincronizacion = SynchronizeCategorias();
                    break;
                // Marcas
                case "03":
                    numSincronizacion = SynchronizeMarcas();
                    break;
                // ApiBanner
                case "04":
                    numSincronizacion = SynchronizeAPIBanner();
                    break;
                case "05":
                    numSincronizacion = SynchronizeAPISection();
            }
        }
        else {
            // Se sincroniza todas las tablas
            numSincronizacion = InitialSynchronize();
        }
        return numSincronizacion;
    }
}
