package co.com.neubs.shopneubs.classes;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.NumberFormat;
import java.util.Locale;

import co.com.neubs.shopneubs.R;

/**
 * Created by bikerlfh on 6/6/17.
 * Contiene métodos staticos para reutilizados
 */

public class Helper {

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
     * Obtiene de una imagen de una url y la carga en un objeto ImageView
     * @param context
     * @param urlImage URL's image
     * @param imageView the object ImageView
     */
    public static void GetImageCached(Context context, String urlImage, ImageView imageView){
        Glide.with(context).load(urlImage).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.circular_progress_bar).into(imageView);
    }
}
