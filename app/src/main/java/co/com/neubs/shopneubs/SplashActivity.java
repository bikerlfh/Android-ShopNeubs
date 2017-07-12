package co.com.neubs.shopneubs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.APIValidations;
import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.classes.SessionManager;
import co.com.neubs.shopneubs.classes.Synchronize;
import co.com.neubs.shopneubs.classes.models.SugerenciaBusqueda;

public class SplashActivity extends Activity {
    private AsyncSyncronize asyncSyncronizeData;
    private boolean isRunning=false;
    boolean isFirstRun;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        ImageView imgSplash = (ImageView) findViewById(R.id.img_splash);
        isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isfirstrun", true);

        if (isFirstRun)
        {
            //DO SOMETHING
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit().putBoolean("isfirstrun", false).apply();
        }
        else {
            // Si no existe ninguna base de datos
            if(this.databaseList().length == 0){
                isFirstRun = true;
            }
        }

        // Se obtiene la instancia del sessionManager
        // para que consulte el usuario y el carro
        sessionManager = SessionManager.getInstance(this);
        initApp(isFirstRun);

        imgSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning){
                    initApp(isFirstRun);
                }
            }
        });
    }


    /**
     * Inicializa la APP
     */
    public void initApp(final boolean isFirstRun) {
        if (!Helper.isConnected(this)){
            dialogNoInternetConnection(isFirstRun).show();
        }
        else {
            isRunning = true;
            asyncSyncronizeData = new AsyncSyncronize(this,isFirstRun);
            asyncSyncronizeData.execute();
        }
    }

    /**
     * AlertDialog de no internet connection
     * @return AlertDialog.Builder
     */
    public AlertDialog dialogNoInternetConnection(final boolean isFirstRun) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.title_no_internet);
        builder.setMessage(R.string.msg_no_internet);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                initApp(isFirstRun);
            }
        });
        return builder.create();
    }



    private class AsyncSyncronize  extends AsyncTask<Void, Void, Boolean> {

        private Synchronize synchronize;
        final Context context;
        private boolean isFirstRun;
        private APIValidations apiValidations;

        public AsyncSyncronize(Context context,boolean isFirstRun){
            this.context = context;
            this.isFirstRun = isFirstRun;
        }

        @Override
        protected void onPreExecute() {
            synchronize = new Synchronize();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean result = true;
            // Se sincroniza toda la api
            if (isFirstRun){
                if(synchronize.InitialSynchronize() == -1)
                    result = false;
            }
            else{
                if(synchronize.SynchronizeAPI(true)==-1)
                    result = false;
            }
            // Si el resultado false, se obtiene el apiValidations
            if (!result){
                apiValidations = APIRest.Sync.apiValidations;
            }
            // Se agregan las sugerencias a la session
            SugerenciaBusqueda sugerencia = new SugerenciaBusqueda();
            sessionManager.setSugerencias(sugerencia.getAllSugerencias());

            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            isRunning = false;
            if(result) {
                Intent intent = new Intent(SplashActivity.this,PrincipalActivity.class);
                startActivity(intent);
                // Se sincronizan los precios del carro
                if (sessionManager.getShopCar().size() > 0){
                    sessionManager.sincronizarPreciosShopCar();
                }
                finish();
            }
            else
            {
                if (apiValidations != null){
                    // Si el token es invalido o no esta authorizado y esta autenticado
                    // se cierra la sesión y se vuelve a lanzar el asincrono
                    if (apiValidations.isTokenInvalid() || (apiValidations.unAuthorized() && sessionManager.isAuthenticated())){
                        Toast.makeText(SplashActivity.this, getString(R.string.msg_session_expired), Toast.LENGTH_LONG).show();
                        sessionManager.closeUserSession();
                        isRunning = true;
                        asyncSyncronizeData = new AsyncSyncronize(SplashActivity.this,isFirstRun);
                        asyncSyncronizeData.execute();
                    }
                    else {
                        Toast.makeText(SplashActivity.this, getString(R.string.error_connection_server), Toast.LENGTH_LONG).show();
                        Log.d("AsyncSyncronize",apiValidations.getDetail());
                    }
                }
                else
                    Toast.makeText(SplashActivity.this, getString(R.string.error_connection_server), Toast.LENGTH_LONG).show();

                if (isFirstRun) {
                    // Se elimina la base de datos
                    context.deleteDatabase(DbManager.DbHelper.DB_NAME);
                }
            }
        }
    }
}
