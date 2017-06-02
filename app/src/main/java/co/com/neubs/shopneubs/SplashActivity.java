package co.com.neubs.shopneubs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.widget.Toast;

import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.AsyncTaskBuiler;
import co.com.neubs.shopneubs.classes.Synchronize;

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
        },TIME_SPLASH);
        */
        AsyncSyncronize syncronize = new AsyncSyncronize(this);
        syncronize.execute();
    }

    private void initSyncronize(){

        Synchronize.SyncronizeMarcas(this);
    }

    // Verifica la conectividad a internet
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
            return true;
        return false;
    }

    public class AsyncSyncronize  extends AsyncTask<Void, Void, Boolean> {

        final Context context;
        public AsyncSyncronize(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            return Synchronize.SyncronizeMarcas(context);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result){
                Toast.makeText(context,"TERMINO PERFECTO",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SplashActivity.this,PrincipalActivity.class);
                startActivity(intent);
                finish();
            }
            else
                Toast.makeText(context,"NO TERMINO",Toast.LENGTH_LONG).show();

        }
    }

}
