package co.com.neubs.shopneubs.interfaces;

/**
 * Created by bikerlfh on 5/23/17.
 */

public interface IAsyncTask {

    void onPreExecute();

    boolean doInBackground();

    void onPostExecute(Boolean result);

}
