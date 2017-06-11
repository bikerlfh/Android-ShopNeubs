package co.com.neubs.shopneubs.classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.NumberFormat;
import java.util.Locale;

import co.com.neubs.shopneubs.R;
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

    public static boolean validarLogin(Context context){
        Usuario usuario = new Usuario(context);
        if (usuario.getLoginUser()) {
            idUserLogin = usuario.getIdUsuario();
            tokenUserLogin = usuario.getToken();
            return true;
        }
        return false;
    }

    public static boolean guardarLoginUser(Context context,String email, String username,String token){
        Usuario usuario = new Usuario(context);
        // Si ya hay un usuario logeado, se debe desloguear
        if (usuario.getLoginUser()){
            usuario.setToken(null);
            usuario.save();
        }
        // Se valida que el email o el username exista
        if (!email.isEmpty() || !username.isEmpty()) {
            if (!usuario.getByEmail(email)) {
                usuario.getByUserName(username);
            }
            // Si el usuario no esta guardado, se asigna el email y el username
            if (usuario.getIdUsuario() == 0) {
                usuario.setEmail(email);
                usuario.setUsername(username);
            }
            // Se guarda el token y el usuario
            usuario.setToken(token);
            usuario.save();
            return true;
        }
        return false;
    }

}
