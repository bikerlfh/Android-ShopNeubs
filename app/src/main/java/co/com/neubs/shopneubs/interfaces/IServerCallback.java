package co.com.neubs.shopneubs.interfaces;

import co.com.neubs.shopneubs.classes.APIValidations;

public interface IServerCallback {

    void onSuccess(String json);

    void onError(String message_error, APIValidations apiValidations);

}