package co.com.neubs.shopneubs.classes;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import co.com.neubs.shopneubs.AppController;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_NOT_MODIFIED;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_SERVER_ERROR;

/**
 * Created by bikerlfh on 5/23/17.
 */

public class APIRest {

    //public final static String PROTOCOL_URL_API = "https";
    //public final static String URL_API = PROTOCOL_URL_API + "://api.shopneubs.com/";

    public final static String PROTOCOL_URL_API = "http";
    public final static String URL_API = PROTOCOL_URL_API + "://192.168.1.50:8000/api/";

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
        public static  String post(String url,Map<String,String> params,Map<String,String> headers){
            headers = headers !=  null? headers:new HashMap<String, String>();
            request = HttpRequest.post(constructURL(url),params,false).accept("application/json").headers(headers);
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
         * Create a new request get
         * @param url URl to fetch the JSON from
         * @param callback Interface Implement (IServerCallback)
         */
        public static void get(String url,final IServerCallback callback){
            StringRequest(Request.Method.GET,constructURL(url),null,callback);
        }

        public static void post(String url,Map<String,String> params,final IServerCallback callback){
            StringRequest(Request.Method.POST,constructURL(url),params,callback);
        }

        /**
         * Creates a new request.
         * @param method GET, POST, PUT ....
         * @param url URL
         * @param params parametros
         * @param callback
         */
        private static void StringRequest(int method,String url,final Map<String,String> params, final IServerCallback callback){
            StringRequest postRequest = new StringRequest(method, constructURL(url),
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            callback.onSuccess(response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            RESPONSE_STATUS_CODE = error.networkResponse.statusCode;
                            callback.onError(error.getMessage(),new String(error.networkResponse.data));
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String,String> params1 = params == null? new HashMap<String, String>():params;
                    return params1;
                }
            };
            AppController.getInstance().addToRequestQueue(postRequest);
        }

        public static boolean ok() {
            return RESPONSE_STATUS_CODE == HTTP_OK;
        }

        public static boolean badRequest() {
            return RESPONSE_STATUS_CODE == HTTP_BAD_REQUEST;
        }

        public static boolean serverError() {
            return RESPONSE_STATUS_CODE == HTTP_INTERNAL_ERROR;
        }

        public static boolean notFound() {
            return RESPONSE_STATUS_CODE == HTTP_NOT_FOUND;
        }
    }


}
