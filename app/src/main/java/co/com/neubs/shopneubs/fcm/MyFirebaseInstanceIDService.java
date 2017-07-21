package co.com.neubs.shopneubs.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import co.com.neubs.shopneubs.classes.SessionManager;
import co.com.neubs.shopneubs.classes.models.FireBaseToken;

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
        // Se registra el token en el servidor
        SessionManager.getInstance(getApplicationContext()).registerFCMToken(token);

        FireBaseToken fireBaseToken = new FireBaseToken();
        // se verifica si existe un registro
        if (fireBaseToken.exists()){
            fireBaseToken.setToken(token);
            if(!fireBaseToken.update())
                Log.d(TAG,"Error al guardar el token fcm");
        }
        else{
            fireBaseToken.setToken(token);
            if (!fireBaseToken.save())
                Log.d(TAG,"Error al modificar el token fcm");
        }
    }
}
