package co.com.neubs.shopneubs.classes;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import co.com.neubs.shopneubs.AppController;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

import static java.net.HttpURLConnection.HTTP_CLIENT_TIMEOUT;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_SERVER_ERROR;
import static java.net.HttpURLConnection.HTTP_UNAVAILABLE;

/**
 * Created by bikerlfh on 5/23/17.
 */

public class APIRest {

    public final static String PROTOCOL_URL_API = "https";
    public final static String URL_API = PROTOCOL_URL_API + "://api.shopneubs.com/";

    //public final static String PROTOCOL_URL_API = "http";
    //public final static String URL_API = PROTOCOL_URL_API + "://192.168.1.50:8000/api/";

    // URLs de funcionalidad de la API
    public final static String URL_LOGIN = "rest-auth/login/";
    public final static String URL_LOGOUT = "rest-auth/logout/";
    public final static String URL_REGISTER = "register/";
    public final static String URL_FILTRO_PRODUCTO = "search/";
    public final static String URL_PRODUCTO_DETALLE = "producto/";
    public final static String URL_MIS_PEDIDOS = "mis-pedidos/";
    public final static String URL_PEDIDO_DETALLE = "pedido/";
    public final static String URL_PERFIL = "profile/";
    public final static String URL_PERFIL_CREATE = "profile/create/";
    public final static String URL_PERFIL_EDIT = "profile/edit/";
    public final static String URL_SOLICITUD_PEDIDO = "ventas/solicitud/";
    public final static String URL_PRODUCTO_SIMPLE = "producto-simple/";


    /**
     * tiempo de espera maximo por petición
     */
    private final static int TIME_OUT = 30000;

    /**
     * Serialize a object from String(json format)
     * @param json The string json format
     * @param classOfT class to convert
     */
    public static <T> T serializeObjectFromJson(String json, final Class<T> classOfT){
        Gson gson = new GsonBuilder().create();
        return (T) gson.fromJson(json, (Type)classOfT);
    }

    /**
     * Retorna la url valida
     * @param url de la peticion
     * @return url valida
     */
    protected static String constructURL(String url){
        return (!url.contains(URL_API))? URL_API + url : url;
    }

    /**
     * Retorna el valor de una key de un json
     * @param json json
     * @param key del parametro
     * @return Object
     */
    public static Object getObjectFromJson(String json, String key){
        try{
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.get(key);
        }
        catch (Exception ex){
            return null;
        }
    }

    /**
     * Convierte los parametros Map<String,String> a un String format: p1=value1&p2=value2
     * @param param parametros Map para convertirlos parametros GET
     * @return String parametros formato param1=valor1&param2=valor2...
     */
    private static String makeParams(Map<String,String> param){
        try {
            String parms = null;
            Set<String> keys = param.keySet();
            for (String key : keys) {
                parms = (parms == null)? key + "=" + URLEncoder.encode(String.valueOf(param.get(key)), "UTF-8"): parms + "&" + key + "=" + URLEncoder.encode(String.valueOf(param.get(key)), "UTF-8");
            }
            return parms;
        }
        catch (Exception ex){
            return null;
        }
    }

    /**
     * Retorna el header con el Token.
     * @return parametros con el token si existe
     */
    public static Map<String,String> addTokenHeader(Map<String,String> headers){
        SessionManager sessionManager = SessionManager.getInstance(null);
        if (sessionManager.getToken() != null && sessionManager.getToken().length() > 0)
        {
            if (headers == null)
                headers = new HashMap<>();
            headers.put("Authorization","Token " + sessionManager.getToken());
        }
        return headers;
    }

    /**
     * Clase de metodos sincronos
     * los métodos de esta clase deben ser usados dentro de un AsyncTask, de lo contrario se Excepcionará
     */
    public static class Sync {
        private static HttpRequest request;
        public static APIValidations apiValidations;

        /**
         * realiza una peticion a la API retornando un String en formato json
         * @param url de la peticion
         * @return response o null
         */
        public static String get(String url){
            try {
                Map<String, String> headers = addTokenHeader(null);
                headers = headers != null ? headers : new HashMap<String, String>();
                request = HttpRequest.get(constructURL(url)).accept("application/json").headers(headers).connectTimeout(TIME_OUT);
                // Se valida si el request tiene error
                return validarRequest()? request.body() : null;
            }
            catch (HttpRequest.HttpRequestException e){
                makeException(e.getCause());
                return null;
            }
        }

        /**
         * realiza una peticion a la API retornando un String en formato json
         * @param url de la peticion
         * @param headers de la peticion
         * @return response o null
         */
        public static String get(String url,Map<String,String> headers){
            try {
                headers = addTokenHeader(headers);
                headers = headers != null ? headers : new HashMap<String, String>();
                request = HttpRequest.get(constructURL(url)).accept("application/json").headers(headers).connectTimeout(TIME_OUT);
                // Se valida si el request tiene error
                return validarRequest()? request.body() : null;
            }
            catch (HttpRequest.HttpRequestException e){
                makeException(e.getCause());
                return null;
            }
        }

        /**
         * Realiza una petición POST
         * @param url URL Api
         * @param params parametros conformados por K,Y
         * @param headers de la peticion
         * @return response o null
         */
        public static String post(String url,Map<String,String> params,Map<String,String> headers){
            try {
                //headers = headers !=  null? headers:new HashMap<String, String>();
                request = HttpRequest.post(constructURL(url)).accept("application/json").connectTimeout(TIME_OUT);
                // Se envian los parametros si existen
                request = (params != null) ? request.send(makeParams(params)) : request;

                headers = addTokenHeader(headers);
                if (headers != null)
                    request = request.headers(headers);
                // Se valida si el request tiene error
                return validarRequest()? request.body() : null;
            }
            catch (HttpRequest.HttpRequestException e){
                makeException(e.getCause());
                return null;
            }
        }

        /**
         * Valida la respuesta de la petición
         * si esta no es correcta, se crea el objeto apiValidations
         */
        private static boolean validarRequest(){
            apiValidations = null;
            // Si la respuesta del request no es ok, se carga el objeto apiValidations
            if (request != null && !request.ok() && !request.created()) {
                if (!request.isBodyEmpty())
                    apiValidations = serializeObjectFromJson(request.body(), APIValidations.class);
                else
                    apiValidations = new APIValidations();
                apiValidations.setResponseCode(request.code());
                return false;
            }
            return true;
        }

        /**
         * Crea el objeto apiValidations dependiendo de la excepción generada
         * @param ex excepción
         */
        private static void makeException(IOException ex){
            apiValidations = new APIValidations();
            // Si se excepcionó connectException
            if (ex.getClass() == ConnectException.class)
                apiValidations.setResponseCode(HTTP_UNAVAILABLE);
            else if(ex.getClass() == SocketTimeoutException.class)
                apiValidations.setResponseCode(HTTP_CLIENT_TIMEOUT);

            apiValidations.setDetail(ex.getMessage());
        }

        public static boolean ok() {
            return request.ok();
        }

        /*
        public static boolean badRequest() {
            return request.badRequest();
        }

        public static boolean unAuthorized() { return request.code() == HTTP_UNAUTHORIZED; }

        public boolean timeOut() { return request.code() == HTTP_CLIENT_TIMEOUT || request.code() == HTTP_UNAVAILABLE; }

        public static boolean serverError() {
            return request.serverError();
        }

        public static boolean notFound() {
            return request.notFound();
        }
        */
    }

    /**
     * Clase de metodos Asincronos
     *
     */
    public static class Async {
        private static int RESPONSE_CODE;
        /**
         * Crea un nuevo request GET
         * @param url URl to fetch the JSON from
         * @param callback Interface Implement (IServerCallback)
         */
        public static void get(final String url,final IServerCallback callback){
            StringRequest(Request.Method.GET,constructURL(url),null,null,callback);
        }

        /**
         * Crea un nuevo request GET con parametros y headers
         * @param url de la petición
         * @param params de la peticion
         * @param callback intefaz para interactuar
         */
        public static void get(String url,final Map<String,String> params,final IServerCallback callback){
            // Se formatean los parametros si es necesario
            url = constructURL(url).concat((params != null)? ("?" + makeParams(params)) : "");
            StringRequest(Request.Method.GET,url,null,null,callback);
        }
        /**
         * Crea un nuevo request GET con parametros y headers
         * @param url de la petición
         * @param params de la peticion
         * @param headers de la peticion
         * @param callback intefaz para interactuar
         */
        public static void get(String url,final Map<String,String> params,final Map<String,String> headers,final IServerCallback callback){
            // Se formatean los parametros si es necesario
            url = constructURL(url).concat((params != null)? ("?" + makeParams(params)) : "");
            StringRequest(Request.Method.GET,url,null,headers,callback);
        }

        /**
         * nueva peticion POST con parametros
         * @param url de la petición
         * @param params de la petición
         * @param callback intefaz para interactuar
         */
        public static void post(String url,Map<String,String> params,final IServerCallback callback){
            StringRequest(Request.Method.POST,constructURL(url),params,null,callback);
        }

        /**
         * nueva peticion POST con parametros y headers
         * @param url de la petición
         * @param params de la petición
         * @param headers de la petición
         * @param callback intefaz para interactuar
         */
        public static void post(String url,Map<String,String> params,Map<String,String> headers,final IServerCallback callback){
            StringRequest(Request.Method.POST,constructURL(url),params,headers,callback);
        }

        /**
         * Creates a new request.
         * @param method GET, POST, PUT ....
         * @param url URL
         * @param params parametros
         * @param callback intefaz para interactuar
         */
        private static void StringRequest(int method,String url,final Map<String,String> params,final Map<String,String> headers, final IServerCallback callback){
            // TimeOut
            final RetryPolicy policy = new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

            final StringRequest postRequest = (StringRequest) new StringRequest(method, constructURL(url),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            callback.onSuccess(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            APIValidations apiValidations = null;
                            if (error.networkResponse != null) {
                                RESPONSE_CODE = error.networkResponse.statusCode;
                                // Si no fue un error de no found o un server error
                                // se serializan los datos
                                if (RESPONSE_CODE != HTTP_NOT_FOUND  && RESPONSE_CODE != HTTP_INTERNAL_ERROR) {
                                    apiValidations = serializeObjectFromJson(new String(error.networkResponse.data), APIValidations.class);
                                    apiValidations.setResponse(new String(error.networkResponse.data));
                                }
                                else
                                    apiValidations = new APIValidations();
                                apiValidations.setResponseCode(error.networkResponse.statusCode);
                            }
                            else{
                                apiValidations = new APIValidations();
                                if (error.getCause().getClass() == ConnectException.class) {
                                    RESPONSE_CODE = HTTP_UNAVAILABLE;
                                }
                                else if(error.getClass() == TimeoutError.class || error.getCause().getClass() == SocketTimeoutException.class) {
                                    RESPONSE_CODE = HTTP_CLIENT_TIMEOUT;
                                }
                                apiValidations.setResponseCode(RESPONSE_CODE);
                            }
                            callback.onError(error.getMessage(),apiValidations);
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Map<String,String> parms1 = params == null? new HashMap<String, String>() : params;
                    return params == null? new HashMap<String, String>() : params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> headers1 = addTokenHeader(headers);
                    //headers1.put("Content-Type","application/json");
                    return (headers1 == null? super.getHeaders() : headers1);
                }
            }.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(postRequest);
        }
        /*
        public static boolean ok() {
            return RESPONSE_CODE == HTTP_OK;
        }

        public static boolean created() { return  RESPONSE_CODE == HTTP_CREATED; }

        public static boolean badRequest() {
            return RESPONSE_CODE == HTTP_BAD_REQUEST;
        }

        public static boolean unAuthorized() { return RESPONSE_CODE == HTTP_UNAUTHORIZED; }

        public boolean timeOut() { return responseCode == HTTP_CLIENT_TIMEOUT || responseCode == HTTP_UNAVAILABLE; }

        public static boolean serverError() {
            return RESPONSE_CODE == HTTP_INTERNAL_ERROR;
        }

        public static boolean notFound() {
            return RESPONSE_CODE == HTTP_NOT_FOUND;
        }*/
    }
}
