package co.com.neubs.shopneubs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

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
        isOnline();
    }

    // Verifica la conectividad a internet
    public boolean isOnline() {
        final boolean[] internetActive = {false};
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
            internetActive[0] = true;
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage(R.string.msg_no_internet);
            builder.setPositiveButton("Recargar",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    internetActive[0] = isOnline();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if (internetActive[0]){
            AsyncSyncronize syncronizeData = new AsyncSyncronize(this);
            syncronizeData.execute();
        }
        return internetActive[0];
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
