package co.com.neubs.shopneubs.classes;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import co.com.neubs.shopneubs.interfaces.IAsyncTask;

/**
 * Created by bikerlfh on 5/23/17.
 */

public class AsyncTaskBuiler  extends AsyncTask<Void, Void, Boolean> {

    private IAsyncTask iTask;
    private ProgressDialog progressDialog;
    private Context context;

     public AsyncTaskBuiler(Context context, final IAsyncTask iTask)
     {
        this. context = context;
        this.iTask = iTask;
     }


    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(this.context);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Consultando ...");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);

        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            AsyncTaskBuiler.this.cancel(true);
            }
        });

        progressDialog.show();
        iTask.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return iTask.doInBackground();
    }

    @Override
    protected void onPostExecute(Boolean result) {

        progressDialog.dismiss();
        iTask.onPostExecute(result);
    }
}
