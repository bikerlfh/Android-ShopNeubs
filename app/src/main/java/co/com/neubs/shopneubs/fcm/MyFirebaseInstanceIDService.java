package co.com.neubs.shopneubs.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.APIValidations;
import co.com.neubs.shopneubs.classes.SessionManager;
import co.com.neubs.shopneubs.classes.models.FireBaseToken;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

/**
 * Created by bikerlfh on 7/19/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "FBInstanceIDService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,refreshedToken);
        saveToken(refreshedToken);
    }

    // Guarda el token en la DB
    public void saveToken(String token){
        FireBaseToken fireBaseToken = new FireBaseToken();
        // se verifica si existe un registro
        if (fireBaseToken.exists()){
            fireBaseToken.setToken(token);
            if(!fireBaseToken.update()){
                Log.d(TAG,"Error al guardar el token fcm");
                return;
            }
        }
        else{
            fireBaseToken.setToken(token);
            if (!fireBaseToken.save()){
                Log.d(TAG,"Error al modificar el token fcm");
                return;
            }
        }

        SessionManager sessionManager = SessionManager.getInstance(getApplicationContext());

        sessionManager.registerFCMToken(token);
    }
}
