package co.com.neubs.shopneubs.classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
}
