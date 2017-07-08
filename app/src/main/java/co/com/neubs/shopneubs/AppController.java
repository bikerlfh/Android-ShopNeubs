package co.com.neubs.shopneubs;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by bikerlfh on 5/22/17.
 */

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //addShortcut();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {

            mRequestQueue = Volley.newRequestQueue(getApplicationContext());

            /*Cache cache = new DiskBasedCache(getCacheDir(), 10 * 1024 * 1024*364);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new  RequestQueue(cache, network);
            mRequestQueue.start();*/
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    private void addShortcut() {
        //Creamos el Intent y apuntamos a nuestra classe principal
        //al hacer click al acceso directo
        //En este caso de ejemplo se llama "Principal"
        Intent shortcutIntent = new Intent(getApplicationContext(), SplashActivity.class);
        //Añadimos accion
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        //Recogemos el texto des de nuestros Values
        CharSequence contentTitle = getString(R.string.app_name);
        //Creamos intent para crear acceso directo
        Intent addIntent = new Intent();
        //Añadimos los Extras necesarios como nombre del icono y icono
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, contentTitle.toString());
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(),R.mipmap.ic_launcher));
        //IMPORTATE: si el icono ya esta creado que no cree otro
        addIntent.putExtra("duplicate", false);
        //Llamamos a la acción
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        //Enviamos petición
        getApplicationContext().sendBroadcast(addIntent);
    }
}