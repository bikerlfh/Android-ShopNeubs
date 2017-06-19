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

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import co.com.neubs.shopneubs.AppController;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_NOT_MODIFIED;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_CLIENT_TIMEOUT;

/**
 * Created by bikerlfh on 5/23/17.
 */

public class APIRest {

    //public final static String PROTOCOL_URL_API = "https";
    //public final static String URL_API = PROTOCOL_URL_API + "://api.shopneubs.com/";

    public final static String PROTOCOL_URL_API = "http";
    public final static String URL_API = PROTOCOL_URL_API + "://192.168.1.50:8000/api/";

    // URLs de funcionalidad de la API
    public final static String URL_LOGIN = "rest-auth/login/";
    public final static String URL_LOGOUT = "rest-auth/logout/";
    public final static String URL_REGISTER = "register/";
    public final static String URL_FILTRO_PRODUCTO = "search/";
    public final static String URL_PRODUCTO_DETALLE = "producto/";
    public final static String URL_MIS_PEDIDOS = "mis-pedidos/";
    public final static String URL_PERFIL = "profile/";
    public final static String URL_PERFIL_CREATE = "profile/create/";
    public final static String URL_PERFIL_EDIT = "profile/edit/";


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
     * @param url
     * @return
     */
    protected static String constructURL(String url){
        return (!url.contains(URL_API))? URL_API + url : url;
    }

    /**
     * Retorna el valor de una key de un json
     * @param json json
     * @param key del parametro
     * @return
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
     * @param param
     * @return
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
     * @return
     */
    public static Map<String,String> addTokenHeader(Map<String,String> headers){
        SessionManager sessionManager = SessionManager.getInstance();
        if (sessionManager.getToken() != null && sessionManager.getToken().length() > 0)
        {
            if (headers == null)
                headers = new HashMap<String,String>();
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
        /**
         * realiza una peticion a la API retornando un String en formato json
         * @param url
         * @return
         */
        public static String get(String url,Map<String,String> headers){
            headers = addTokenHeader(headers);
            headers = headers !=  null? headers:new HashMap<String, String>();
            request = HttpRequest.get(constructURL(url)).accept("application/json").headers(headers);
            return request.body();
        }

        /**
         * Realiza una petición POST
         * @param url URL Api
         * @param params parametros conformados por K,Y
         * @param headers
         * @return
         */
        public static String post(String url,Map<String,String> params,Map<String,String> headers){
            //headers = headers !=  null? headers:new HashMap<String, String>();
            request = HttpRequest.post(constructURL(url)).accept("application/json");
            // Se envian los parametros si existen
            request = (params != null) ? request.send(makeParams(params)):request;

            headers = addTokenHeader(headers);
            if (headers != null)
                request = request.headers(headers);
            return request.body();
        }

        /**
         * Realiza una peticion a la API retornando un objeto serializado
         * @param url
         * @param classOfT Tipo de objeto a devolver (ejemplo Producto.class, Producto[].class)
         * @return
         */
        public static <T> T getSerializedObjectFromGETRequest(String url,Class<T> classOfT){
            String response = get(url,null);
            return (response == null)? null : serializeObjectFromJson(response,classOfT);
        }



        public static boolean ok() {
            return request.ok();
        }

        public static boolean badRequest() {
            return request.badRequest();
        }

        public static boolean unAuthorized() { return request.code() == HTTP_UNAUTHORIZED; }

        public static boolean timeOut() {
            return request.code() == HTTP_CLIENT_TIMEOUT;
        }

        public static boolean serverError() {
            return request.serverError();
        }

        public static boolean notFound() {
            return request.notFound();
        }
    }

    /**
     * Clase de metodos Asincronos
     *
     */
    public static class Async {
        private static int RESPONSE_STATUS_CODE;
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
         * @param url
         * @param params
         * @param headers
         * @param callback
         */
        public static void get(String url,final Map<String,String> params,final Map<String,String> headers,final IServerCallback callback){
            // Se formatean los parametros si es necesario
            url = constructURL(url).concat((params != null)? ("?" + makeParams(params)) : "");
            StringRequest(Request.Method.GET,url,null,headers,callback);
        }

        /**
         * nueva peticion POST con parametros
         * @param url
         * @param params
         * @param callback
         */
        public static void post(String url,Map<String,String> params,final IServerCallback callback){
            StringRequest(Request.Method.POST,constructURL(url),params,null,callback);
        }

        /**
         * nueva peticion POST con parametros y headers
         * @param url
         * @param params
         * @param headers
         * @param callback
         */
        public static void post(String url,Map<String,String> params,Map<String,String> headers,final IServerCallback callback){
            StringRequest(Request.Method.POST,constructURL(url),params,headers,callback);
        }

        /**
         * Creates a new request.
         * @param method GET, POST, PUT ....
         * @param url URL
         * @param params parametros
         * @param callback
         */
        private static void StringRequest(int method,String url,final Map<String,String> params,final Map<String,String> headers, final IServerCallback callback){
            // TimeOut
            final RetryPolicy policy = new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

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
                            if (error.getClass() == TimeoutError.class) {
                                RESPONSE_STATUS_CODE = HTTP_CLIENT_TIMEOUT;
                            }
                            else if (error.networkResponse != null)
                                RESPONSE_STATUS_CODE = error.networkResponse.statusCode;
                            // Si es un badRequest se llena el apiValidations
                            if (badRequest() || unAuthorized()){
                                apiValidations = serializeObjectFromJson(new String(error.networkResponse.data),APIValidations.class);
                                apiValidations.setResponse(new String(error.networkResponse.data));
                            }
                            callback.onError(error.getMessage(),apiValidations);
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parms1 = params == null? new HashMap<String, String>() : params;
                    return parms1;
                    //return (params == null? super.getParams() : params);
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> headers1 = addTokenHeader(headers);
                    return (headers1 == null? super.getHeaders() : headers1);
                }
            }.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(postRequest);
        }
        public static boolean ok() {
            return RESPONSE_STATUS_CODE == HTTP_OK;
        }

        public static boolean created() { return  RESPONSE_STATUS_CODE == HTTP_CREATED; }

        public static boolean badRequest() {
            return RESPONSE_STATUS_CODE == HTTP_BAD_REQUEST;
        }

        public static boolean unAuthorized() { return RESPONSE_STATUS_CODE == HTTP_UNAUTHORIZED; }

        public static boolean timeOut() {
            return RESPONSE_STATUS_CODE == HTTP_CLIENT_TIMEOUT;
        }

        public static boolean serverError() {
            return RESPONSE_STATUS_CODE == HTTP_INTERNAL_ERROR;
        }

        public static boolean notFound() {
            return RESPONSE_STATUS_CODE == HTTP_NOT_FOUND;
        }
    }
}
