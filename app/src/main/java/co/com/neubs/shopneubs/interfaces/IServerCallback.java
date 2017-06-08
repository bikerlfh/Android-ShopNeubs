package co.com.neubs.shopneubs.interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IServerCallback {

    void onSuccess(String json);

    void onError(String message_error,String response);

}