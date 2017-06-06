package co.com.neubs.shopneubs.classes;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.reflect.Type;

import co.com.neubs.shopneubs.AppController;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

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
     * Clase de metodos sincronos
     * los métodos de esta clase deben ser usados dentro de un AsyncTask, de lo contrario se Excepcionará
     */
    public static class Sync {

        /**
         * realiza una peticion a la API retornando un String en formato json
         * @param url
         * @return
         */
        public static String get(String url){
            if(!url.contains(URL_API))
                url= URL_API + url;
            return HttpRequest.get(url).accept("application/json").body();
        }

        /**
         * Realiza una peticion a la API retornando un objeto serializado
         * @param url
         * @param classOfT Tipo de objeto a devolver (ejemplo Producto.class, Producto[].class)
         * @return
         */
        public static <T> T get(String url,Class<T> classOfT){
            return serializeObjectFromJson(get(url),classOfT);
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
            if(!url.contains(URL_API))
                url= URL_API + url;
            requestJsonObject(Request.Method.GET,url,null,callback);
        }

        public static void getArray(String url,final IServerCallback callback){
            if(!url.contains(URL_API))
                url= URL_API + url;
            requestJsonArray(url,callback);
        }

        public static void post(String url,JSONObject params,final IServerCallback callback){
            requestJsonObject(Request.Method.POST,url,params,callback);
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
                            callback.onError(error.getMessage());
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
                    callback.onError(message_error);
                }
            });
            AppController.getInstance().addToRequestQueue(req);
        }
    }
}
