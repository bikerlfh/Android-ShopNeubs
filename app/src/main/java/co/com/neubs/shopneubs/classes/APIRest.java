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

/**
 * Created by bikerlfh on 5/23/17.
 */

public class APIRest {
    public static String message_error;
    public static JSONObject jsonObject;
    public static JSONArray jsonArray;

    public final static String PROTOCOL_URL_API = "https";
    public final static String URL_API = PROTOCOL_URL_API + "://api.shopneubs.com/";

    //public final static String PROTOCOL_URL_API = "http";
    //public final static String URL_API = PROTOCOL_URL_API + "://192.168.1.50:8000/api/";

    public static int RESPONSE_CODE;

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
    private static String constructURL(String url){
        return (!url.contains(URL_API))? URL_API + url : url;
    }

    /**
     * Clase de metodos sincronos
     * los métodos de esta clase deben ser usados dentro de un AsyncTask, de lo contrario se Excepcionará
     */
    public static class Sync {
        /**
         * realiza una peticion a la API retornando un String en formato json
         * @param url
         * @return
         */
        public static String get(String url,Map<String,String> headers){
            headers = headers !=  null? headers:new HashMap<String, String>();
            HttpRequest request = HttpRequest.get(constructURL(url)).accept("application/json").headers(headers);
            if (!validateResponseCode(request)){
                return null;
            }
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
            HttpRequest request = HttpRequest.post(constructURL(url),params,false).accept("application/json").headers(headers);
            validateResponseCode(request);
            /*if (!){
                return null;
            }*/
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
            if (response == null)
                return null;
            return serializeObjectFromJson(response,classOfT);
        }



        private static boolean validateResponseCode(HttpRequest request){
            try {
                RESPONSE_CODE = request.getConnection().getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (request.badRequest() || request.notFound() || request.serverError())
                return false;
            return true;
        }
    }

    /**
     * Clase de metodos Asincronos
     *
     */
    public static class Async {

        /**
         * Create a new request get
         * @param url URl to fetch the JSON from
         * @param callback Interface Implement (IServerCallback)
         */
        public static void get(String url,final IServerCallback callback){
            requestJsonObject(Request.Method.GET,constructURL(url),null,callback);
        }

        public static void getArray(String url,final IServerCallback callback){
            requestJsonArray(constructURL(url),callback);
        }

        public static void post(String url,Map<String,String> params,final IServerCallback callback){

            //requestJsonObject(Request.Method.POST,constructURL(url),params,callback);
            StringRequest(Request.Method.POST,constructURL(url),params,callback);
        }

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
                            //RESPONSE_CODE = error.networkResponse.statusCode;
                            final String data = new String(error.networkResponse.data);


                            if (error.networkResponse.statusCode == HTTP_BAD_REQUEST){
                                //callback.onBadRequest400(data);
                            }
                            //else
                              //  callback.onError(error.getMessage(),error.networkResponse.statusCode,data);
                            /*if (error.getMessage() != null)
                                callback.onError(error.getMessage());
                            else
                                callback.onError(new String(error.networkResponse.data));*/

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
        /**
         * Creates a new request.
         * @param method the HTTP method to use (com.android.volley.Request.Method.GET)
         * @param url URL to fetch the JSON from
         * @param jsonRequest A {@link JSONObject} to post with the request. Null is allowed and
         *   indicates no parameters will be posted along with request.
         */
        private static void requestJsonObject(int method,String url,JSONObject jsonRequest, final IServerCallback callback){
            jsonObject = null;
            message_error = null;
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(method, url, jsonRequest,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            callback.onSuccess(response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            message_error = error.getMessage();
                            //callback.onError(error.getMessage(),error.networkResponse.statusCode,new String(error.networkResponse.data));
                        }
                    });
            AppController.getInstance().addToRequestQueue(jsObjRequest);
        }
        /**
         * Creates a new request.
         * @param url URL to fetch the JSON from
         */
        private static void requestJsonArray(String url, final IServerCallback callback){
            jsonArray = null;
            message_error = null;
            JsonArrayRequest req = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            callback.onSuccess(response.toString());
                            jsonArray = response;
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    message_error = error.getMessage();
                    //callback.onError(error.getMessage(),error.networkResponse.statusCode,new String(error.networkResponse.data));
                }
            });
            AppController.getInstance().addToRequestQueue(req);
        }
    }
}
