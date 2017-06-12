package co.com.neubs.shopneubs.interfaces;

public interface IServerCallback {

    void onSuccess(String json);

    void onError(String message_error,String response);

}