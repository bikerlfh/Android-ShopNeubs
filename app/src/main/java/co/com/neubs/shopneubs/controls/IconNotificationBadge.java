package co.com.neubs.shopneubs.controls;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by bikerlfh on 6/28/17.
 * Copyright 2017 Neubs SAS
 *
 * Crea un icono con notificaciones numerica tipo  Icono ShopCart
 *
 * Modo de uso como icono del ActionBar
 * * En el menu agregar al item (app:actionViewClass="co.com.neubs.shopneubs.controls.IconNotificationBadge")
 * * En el onCreateOptionsMenu de la actividad obtener el ActionView del item (R.id.action_cart = id del MenuItem)

    final MenuItem itemMenuCart = menu.findItem(R.id.action_cart);
    final ActionBarIconNotificationBadge itemCart = (ActionBarIconNotificationBadge)itemMenuCart.getActionView();
    if (itemCart != null) {
        // Se asigna el icono
        itemCart.setIcon(R.drawable.ic_menu_shop_cart);
        // Se habilita la animación
        itemCart.setAnimationEnabled(true);
        // OJO no sirve el onOptionsItemSelected con este control, se debe agregar el setOnClickListener
        itemCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(itemMenuCart);
            }
        });
    }
 *
 *
 */

public class IconNotificationBadge extends RelativeLayout {
    /**
     * Icono del menu
     */
    private ImageView mImage;
    /**
     * Badge (donde se visualizará los numeros)
     */
    private TextView mTextBadge;

    @ColorInt
    private static final int DEFAULT_BACKGROUND_BADGE_COLOR = Color.RED;

    private static final int DEFAULT_ANIMATION_DURATION = 500;
    /**
     * Icono por defecto
     */
    private static final int DEFAULT_ICON_DRAWABLE = android.R.drawable.ic_menu_gallery;
    /**
     * numero maximo de tamaño del texto
     */
    private static final int DEFAULT_MAX_TEXT_LENGTH = 2;
    /**
     * Cuando el texto o el número excede el tamaño maximo, se visualiza el contenido de ésta propiedad
     */
    private static final String DEFAULT_TEXT_MAX_LENGTH_REACHED = "...";

    @ColorInt
    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;

    @Dimension
    private static final int DEFAULT_TEXT_SIZE_SP = 10;

    private static final int DEFAULT_GRAVITY_TEXT_BADGE = Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL;

    @Dimension
    private static final int DEFAULT_WIDTH_BADGE_DP = 18;
    @Dimension
    private static final int DEFAULT_HEIGTH_BADGE_DP = 18;

    private int widthBadge = DEFAULT_WIDTH_BADGE_DP;
    private int heightBadge = DEFAULT_HEIGTH_BADGE_DP;

    // Margenes del Badge en DP
    private static final int DEFAULT_MARGIN_LEFT_DP = -8;
    private static final int DEFAULT_MARGIN_TOP_DP = -8;
    private static final int DEFAULT_MARGIN_RIGHT_DP = 0;
    private static final int DEFAULT_MARGIN_BOTTOM_DP = 0;

    private int badgeMarginLeft = DEFAULT_MARGIN_LEFT_DP;
    private int badgeMarginTop = DEFAULT_MARGIN_TOP_DP;
    private int badgeMarginRight = DEFAULT_MARGIN_RIGHT_DP;
    private int badgeMarginBottom = DEFAULT_MARGIN_BOTTOM_DP;

    /**
     * Duración de la animación
     */
    private int animationDuration = DEFAULT_ANIMATION_DURATION;
    /**
     * indica si las animaciones estan activas o no
     */
    private boolean isAnimationEnabled = true;

    /**
     * Animaciones
     */
    private Animation animationUpdate;
    private Animation animationShow;
    private Animation animationHide;

    /**
     * indica la longitud maxima del texto (número)
     */
    private int maxTextLength = DEFAULT_MAX_TEXT_LENGTH;
    /**
     * indica si se está mostrando el texto Badge
     */
    private boolean isBadgeShow = false;
    /**
     * Donde se guarda el texto cuando se invoca el método setText
     */
    private String textoBadge = "0";

    @NonNull
    private final ShapeDrawable shapeOval = new ShapeDrawable(new OvalShape());


    public IconNotificationBadge(Context context) {
        super(context,null,android.R.attr.actionButtonStyle,android.R.attr.actionBarItemBackground);
        init(context,null);
    }

    public IconNotificationBadge(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public IconNotificationBadge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public IconNotificationBadge(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs){
        setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        setClickable(true);

        // Se crea la imagen
        mImage = new ImageView(getContext());
        // se asigna un id para poderlo relacionar con el textCount
        mImage.setId(View.generateViewId());
        mImage.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        mImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        // Se asigna el icono por defecto
        setIcon(DEFAULT_ICON_DRAWABLE);

        // se crea el texto count
        mTextBadge = new TextView(getContext());
        setLayoutParamsBadge();

        // se asigna el color del fondo del Badge
        setBackgroundBadgeColor(DEFAULT_BACKGROUND_BADGE_COLOR);

        setVisibilityBadge(false);

        setTextColor(DEFAULT_TEXT_COLOR);
        setTextSize(DEFAULT_TEXT_SIZE_SP);
        setGravityBadge(DEFAULT_GRAVITY_TEXT_BADGE);

        addView(mImage);
        addView(mTextBadge);

        if (isAnimationEnabled)
            initAnimation();
    }

    public void setAnimationDuration(int duration){
        animationDuration = duration;
    }

    public void setIcon(Drawable drawable){
        mImage.setImageDrawable(drawable);
    }

    public void setIcon(int idDrawable){
        mImage.setImageDrawable(getContext().getApplicationContext().getDrawable(idDrawable));
    }

    public void setColorIcon(int color){
        mImage.setColorFilter(getContext().getResources().getColor(color));
    }

    public void setTextColor(int id){
        mTextBadge.setTextColor(id);
    }
    public void setTextSize(float size){
        mTextBadge.setTextSize(size);
    }

    public void setBackgroundBadge(Drawable background){
        mTextBadge.setBackground(background);
    }

    public void setBackgroundBadgeColor(int color){
        shapeOval.setColorFilter(color, PorterDuff.Mode.SCREEN);
        setBackgroundBadge(shapeOval);
    }

    public void setGravityBadge(int gravityBadge){
        mTextBadge.setGravity(gravityBadge);
    }

    public void setMaxTextLength(int maxTextLength){
        this.maxTextLength = maxTextLength;
    }

    public void setSizeBadge(int width,int height){
        widthBadge = width;
        heightBadge = height;
        setLayoutParamsBadge();
    }

    public void setMarginBadge(int left,int top,int right,int bottom){
        badgeMarginLeft = left;
        badgeMarginTop = top;
        badgeMarginRight = right;
        badgeMarginBottom = bottom;
        setLayoutParamsBadge();
    }

    public void setAnimationEnabled(boolean animationEnabled){
        isAnimationEnabled = animationEnabled;
        if (isAnimationEnabled && (animationShow == null || animationUpdate == null || animationHide == null ))
            initAnimation();
        else if(!isAnimationEnabled) {
            animationShow = null;
            animationUpdate = null;
            animationHide = null;
        }
    }

    private void setVisibilityBadge(boolean visible){
        mTextBadge.setVisibility(visible?VISIBLE:INVISIBLE);
    }

    /**
     * Asigna los LayoutParams del txtBadge
     * el tamaño width y heigth
     * el margin de las propiedades badgeMarginLeft,badgeMarginTop,badgeMarginRight,badgeMarginBottom
     * lo alinea a la imagen.
     */
    private void setLayoutParamsBadge(){
        RelativeLayout.LayoutParams paramsText = new RelativeLayout.LayoutParams(convertDpToPixels(widthBadge),convertDpToPixels(heightBadge));
        paramsText.setMargins(convertDpToPixels(badgeMarginLeft),convertDpToPixels(badgeMarginTop),convertDpToPixels(badgeMarginRight),convertDpToPixels(badgeMarginBottom));
        paramsText.addRule(RelativeLayout.ALIGN_TOP,mImage.getId());
        paramsText.addRule(RelativeLayout.END_OF,mImage.getId());
        mTextBadge.setLayoutParams(paramsText);
    }

    private int convertDpToPixels(float dpi){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpi,getResources().getDisplayMetrics());
    }

    public void clear(){
        if (isAnimationEnabled && isBadgeShow)
            mTextBadge.setAnimation(animationHide);
        else
            setVisibilityBadge(false);

        isBadgeShow = false;
    }

    public void show(int number){
        if (!String.valueOf(number).equals(textoBadge)) {
            textoBadge = String.valueOf(number);
            // si el número tiene una longitud mayor a la proporcionada
            // se visualiza el texto por default alcanzado
            if (textoBadge.length() > maxTextLength)
                textoBadge = DEFAULT_TEXT_MAX_LENGTH_REACHED;

            if (isAnimationEnabled && number > 0) {
                Animation animation = animationShow;
                // si el badger ya se esta visualizado
                // se cambia la animación por la Update
                if (isBadgeShow) {
                    animation = animationUpdate;
                }
                mTextBadge.startAnimation(animation);
            } else if (number > 0) {
                mTextBadge.setText(textoBadge);
                setVisibilityBadge(true);
            } else {
                clear();
                isBadgeShow = false;
                return;
            }
            isBadgeShow = true;
        }
    }

    /**
     * Inicializa las animaciones de show, update y hide
     */
    private void initAnimation() {
        // se crea la animacion de Show
        animationShow = new ScaleAnimation(0, 1, 0, 1,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationShow.setDuration(animationDuration / 2);
        animationShow.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setVisibilityBadge(true);
                mTextBadge.setText(textoBadge);
            }
            @Override
            public void onAnimationEnd(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        // Se crea la animación Update
        animationUpdate = new ScaleAnimation(1, 1.2f, 1, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationUpdate.setDuration(animationDuration / 2);
        animationUpdate.setRepeatMode(Animation.REVERSE);
        animationUpdate.setRepeatCount(1);
        animationUpdate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mTextBadge.setText(textoBadge);
            }
            @Override
            public void onAnimationEnd(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        // Se crea la animación de Hide
        animationHide = new ScaleAnimation(1, 0, 1, 0,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationHide.setDuration(animationDuration / 2);
        animationHide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibilityBadge(false);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }
}
