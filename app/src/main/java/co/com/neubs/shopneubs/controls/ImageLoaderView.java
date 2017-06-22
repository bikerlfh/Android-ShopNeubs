package co.com.neubs.shopneubs.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import co.com.neubs.shopneubs.R;

/**
 * Created by bikerlfh on 6/9/17.
 * Control personalizado, carga mediante Glide una imagen en un ImageView, visualizando un progressBar antes de finalizar la carga
 * El drawable (rotate) del progressBar personalizado se pasa por la propiedad indeterminateDrawable
 * Ejemplo
 * xmlns:neubs="http://schemas.android.com/apk/res-auto"
 *<co.com.neubs.shopneubs.controls.ImageLoaderView
     android:id="@+id/img_producto"
     android:layout_width="match_parent"
     android:layout_height="150dp"
     neubs:indeterminateDrawable="@drawable/circular_progress_bar"
 />
 */

public class ImageLoaderView extends LinearLayout
{
    private ImageView imageView;
    private ProgressBar progressBar;
    private int adapter;

    public ImageLoaderView(Context context){
        super(context);
        init(context, null);
    }

    public ImageLoaderView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context, attrs);
    }

    public ImageLoaderView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public ImageLoaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * inicializa el control
     * @param context
     * @param attrs
     */
    public void init(Context context, AttributeSet attrs){
        setGravity(Gravity.CENTER);
        // Se instancia la clase ImageView
        imageView = new ImageView(context);
        LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        //imageView.setBackgroundColor(getColor(R.color.colorBackgroundCardView));

        // Se crea el progressBar con stylo progressBarStyleLarge
        progressBar = new ProgressBar(context,null,android.R.attr.progressBarStyleLarge);
        //Se obtienen los atributos del control
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ImageLoaderView);

        try {
            // Se obtiene el drawable de la propiedad (indeterminateDrawable)
            Drawable drawable = a.getDrawable(R.styleable.ImageLoaderView_indeterminateDrawable);
            // Si el drawable existe se agrega al ProgressBar
            if (drawable != null)
                progressBar.setIndeterminateDrawable(drawable);
        }
        finally {
            a.recycle();
        }
        progressBar.setVisibility(VISIBLE);

        addView(imageView);
        addView(progressBar);
    }

    public ImageView getImageView()
    {
        return imageView;
    }

    /**
     * Visualiza o esconde el ProgressBar y el ImageView
     * @param loading
     */
    private void loadingImage(boolean loading){
        if (loading){
            // Se visualiza el progressBar
            imageView.setVisibility(GONE);
            progressBar.setVisibility(VISIBLE);
        }else{
            // Se visualiza el ImagenView
            progressBar.setVisibility(GONE);
            imageView.setVisibility(VISIBLE);
        }
    }

    /**
     * Carga la imagen de la url cambiandole el tamaños
     * @param url de la imagen
     * @param height Alto de la imagen
     * @param width Ancho de la imagen
     */
    public void setImageURLresize(String url, int height, int width)
    {
        // Se visualiza el progressBar
        loadingImage(true);
        // Se carga la imagen mediante Glide
        Glide.with(getContext())
                .load(url)
                .override(height, width)
                //.placeholder(R.drawable.circular_progress_bar)
                .listener(new RequestListener<String, GlideDrawable>(){
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target, boolean isFirstResource){
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource){
                        loadingImage(false);
                        return false;
                    }
                })
                .into(imageView);
    }

    /**
     * Carga la imagen de la url en el ImageView
     * @param url de la imagen
     */
    public void setImageURL(String url){
        // Se visualiza el progressBar
        loadingImage(true);
        // Se carga la imagen mediante Glide
        // Se convierte en un bitMap.
        /* el into es un SimpleTarget, esto con el fin de evitar errores de carga
         * debido a la visibilidad del imageView (si se hace con el .listen no funciona)
         */
        Glide.with(getContext())
                .load(url)
                .asBitmap()
                //.placeholder(R.drawable.circular_progress_bar)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        // do something with the bitmap
                        // for demonstration purposes, let's just set it to an ImageView
                        imageView.setImageBitmap( bitmap );
                        loadingImage(false);
                    }
                });
    }


    public void setAdapter(int adapter) {
        this.adapter = adapter;
    }

    public int getAdapter() {
        return adapter;
    }
}