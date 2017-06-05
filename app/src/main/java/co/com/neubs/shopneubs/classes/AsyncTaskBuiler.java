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

    private String messageProgressDialog;

     public AsyncTaskBuiler(Context context, final IAsyncTask iTask){
        this. context = context;
        this.iTask = iTask;
     }

    public void setMessageProgressDialog(String message){
        this.messageProgressDialog = message;
    }

    public void setProgressDialog(ProgressDialog progressDialog){
        this.progressDialog = progressDialog;
    }

     private void createProgressDialog(){
         if (progressDialog == null) {
             progressDialog = new ProgressDialog(this.context);
             progressDialog.setCancelable(false);
             progressDialog.setMessage(messageProgressDialog);
             progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
             progressDialog.setProgress(0);
             progressDialog.setMax(100);

             progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                 @Override
                 public void onDismiss(DialogInterface dialog) {
                     AsyncTaskBuiler.this.cancel(true);
                 }
             });
         }
     }

    @Override
    protected void onPreExecute() {
        createProgressDialog();

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
