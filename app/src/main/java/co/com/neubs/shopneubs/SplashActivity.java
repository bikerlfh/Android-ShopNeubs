package co.com.neubs.shopneubs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.StringPrepParseException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import co.com.neubs.shopneubs.classes.AsyncTaskBuiler;
import co.com.neubs.shopneubs.classes.DbManager;
import co.com.neubs.shopneubs.classes.Synchronize;
import co.com.neubs.shopneubs.interfaces.IAsyncTask;

public class SplashActivity extends Activity {
    private final int TIME_SPLASH = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,PrincipalActivity.class);
                startActivity(intent);
                finish();
            }
        },TIME_SPLASH);*/

        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isfirstrun", true);

        if (isFirstRun)
        {
            //DO SOMETHING
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit().putBoolean("isfirstrun", false).commit();
        }
        else {
            // Si no existe ninguna base de datos
            if(this.databaseList().length == 0){
                isFirstRun = true;
            }
        }

        initApp(isFirstRun);
    }


    /**
     * Inicializa la APP
     */
    public void initApp(final boolean isFirstRun) {
        if (!isConnected()){
            dialogNoInternetConnection(this,isFirstRun).show();
        }
        else {
            AsyncSyncronize syncronizeData = new AsyncSyncronize(this,isFirstRun);
            syncronizeData.execute();
        }
    }

    /**
     * AlertDialog de no internet connection
     * @param c
     * @return
     */
    public AlertDialog.Builder dialogNoInternetConnection(Context c, final boolean isFirstRun) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setCancelable(false);
        builder.setTitle(R.string.title_no_internet);
        builder.setMessage(R.string.msg_no_internet);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                initApp(isFirstRun);
            }
        });
        return builder;
    }

    /**
     * Verifica la conexion a internet
     * @return
     */
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
            return true;
        return false;
    }

    public class AsyncSyncronize  extends AsyncTask<Void, Void, Boolean> {

        private Synchronize synchronize;
        final Context context;
        private boolean isFirstRun;

        public AsyncSyncronize(Context context,boolean isFirstRun){
            this.context = context;
            this.isFirstRun = isFirstRun;
        }

        @Override
        protected void onPreExecute() {
            synchronize = new Synchronize(SplashActivity.this);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // Se sincroniza toda la api
            if (isFirstRun){
                if(!synchronize.InitialSyncronize())
                    return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                Intent intent = new Intent(SplashActivity.this,PrincipalActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(SplashActivity.this, synchronize.message_error, Toast.LENGTH_LONG).show();
                // Se elimina la base de datos
                context.deleteDatabase(DbManager.DbHelper.DB_NAME);
            }
        }
    }


}
