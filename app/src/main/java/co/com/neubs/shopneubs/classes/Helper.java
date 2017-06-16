package co.com.neubs.shopneubs.classes;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.Locale;
import co.com.neubs.shopneubs.classes.models.Usuario;

/**
 * Created by bikerlfh on 6/6/17.
 * Contiene métodos staticos para reutilizados
 */

public class Helper {
    public static int idUserLogin;
    public static String tokenUserLogin;

    /**
     * Retorna un numero en formato moneda sin decimales y con el signo pesos
     * @param number
     * @return
     */
    public static String MoneyFormat(float number){
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
        numberFormat.setMaximumFractionDigits(0);
        return numberFormat.format(number);
    }

    /**
     * Verifica la conexion a internet
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
            return true;
        return false;
    }

    /**
     * Encode un texto a UTF-8
     * @param text
     * @return
     */
    public static String EncodingUTF8(String text){
        try {
            return text!=null? new String(text.getBytes("ISO-8859-1"), "UTF-8"):null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * Se usa en los recycleview
     * @param dp
     * @param context
     * @return
     */
    public static int dpToPx(int dp, Context context) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }
}
