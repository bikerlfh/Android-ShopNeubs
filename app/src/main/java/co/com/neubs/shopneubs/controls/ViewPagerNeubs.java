package co.com.neubs.shopneubs.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.R;

/**
 * Created by bikerlfh on 6/27/17.
 */

public class ViewPagerNeubs extends LinearLayout implements  ViewPager.OnPageChangeListener {

    // region viewPager
    @Nullable
    private ViewPager mViewPager;

    @Dimension
    private int DEFAULT_VIEW_PAGER_HEIGHT_DPI = 200;

    @Nullable
    private WeakReference<PagerAdapter> pagerAdapterReference;
    /**
     * representa la posicion seleccionada
     */
    private int oldPositionSelected;
    //endregion

    // region LayoutDots
    private LinearLayout mLinealLayoutDots;
    private List<IndicatorView> listIndicatorView;

    @Dimension
    static final int DEFAULT_INDICATOR_PADDING_DIP = 9;
    @Px
    private int indicatorPadding;
    @Px
    private int indicatorRadius;
    @ColorInt
    private int unselectedIndicatorColor;
    @ColorInt
    private int selectedIndicatorColor;
    //endregion


    // region Constructor
    public ViewPagerNeubs(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public ViewPagerNeubs(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public ViewPagerNeubs(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public ViewPagerNeubs(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }
    //endregion

    /**
     * Inicializa el control
     */
    private void init(@NonNull Context context,@Nullable AttributeSet attrs,int defStyleAttr,int defStyleRes){

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerNeubs, defStyleAttr, defStyleRes);

        indicatorPadding = attributes.getDimensionPixelSize(R.styleable.ViewPagerNeubs_indicatorPadding, applyDimensionPixel(DEFAULT_INDICATOR_PADDING_DIP));
        indicatorRadius = attributes.getDimensionPixelSize(R.styleable.ViewPagerNeubs_indicatorRadius,applyDimensionPixel(IndicatorView.DEFAULT_RADIUS_DIP));

        unselectedIndicatorColor = attributes.getColor(R.styleable.ViewPagerNeubs_defaultIndicatorColor,IndicatorView.DEFAULT_COLOR);
        selectedIndicatorColor = attributes.getColor(R.styleable.ViewPagerNeubs_selectedIndicatorColor,IndicatorView.DEFAULT_SELECTED_COLOR);

        final int heightViewPager = attributes.getDimensionPixelSize(R.styleable.ViewPagerNeubs_viewPagerHeight, applyDimensionPixel(DEFAULT_VIEW_PAGER_HEIGHT_DPI));

        mViewPager = new ViewPager(context);
        mViewPager.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightViewPager));

        attributes.recycle();

        setOrientation(VERTICAL);

        mLinealLayoutDots = new LinearLayout(context);
        LinearLayout.LayoutParams paramsLayoutDots = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsLayoutDots.setMargins(0,10,0,5);
        mLinealLayoutDots.setLayoutParams(paramsLayoutDots);
        mLinealLayoutDots.setOrientation(HORIZONTAL);
        mLinealLayoutDots.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        mViewPager.addOnPageChangeListener(this);

        addView(mViewPager);
    }

    public PagerAdapter getAdapter() {
        return pagerAdapterReference != null? pagerAdapterReference.get() : null;
    }

    public void setAdapter(PagerAdapter adapter) {
        this.pagerAdapterReference = new WeakReference<>(adapter);
        mViewPager.setAdapter(adapter);
        createDots();
    }


    /**
     * Carga en el ViewPager imagenes apartir de urls.
     * Cuando se invoca este método, se crea el adaptador (ViewPagerAdapter) de forma autormatica
     * no se debe llamar despues el setAdapter
     * @param listUrlImages listado de urls de imagenes
     */
    public void showGalleryImages(List<String> listUrlImages){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getContext(),listUrlImages,null);
        this.setAdapter(adapter);
    }

    /**
     * Carga en el ViewPager imagenes apartir de los idResource
     * Cuando se invoca este método, se crea el adaptador (ViewPagerAdapter) de forma autormatica
     * no se debe llamar despues el setAdapter
     * @param listResourceImages listado de idResources de las imagenes
     */
    public void showGalleryImagesResources(List<Integer> listResourceImages){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getContext(),null,listResourceImages);
        this.setAdapter(adapter);
    }

    /**
     * Return the number of views available.
     */
    public int getCount(){
        return pagerAdapterReference != null ? pagerAdapterReference.get().getCount() : 0;
    }

    /**
     * Crea los indicadores segun la cantidad de paginas que tenga el adaptador
     */
    private void createDots() {
        LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //params.setMargins(8,0,8,0);
        int countDots = getCount();
        listIndicatorView = new ArrayList<>();
        for (int i = 0; i < countDots; i++) {
            IndicatorView indicatorView = new IndicatorView(getContext());
            indicatorView.setRadius(indicatorRadius);
            indicatorView.setPadding(indicatorPadding, 0, indicatorPadding, 0);
            indicatorView.setColor((i == 0) ? selectedIndicatorColor : unselectedIndicatorColor);
            indicatorView.setOnClickListener(onClickListenerIndicator(i));
            listIndicatorView.add(i, indicatorView);
            mLinealLayoutDots.addView(indicatorView, params);
        }
        oldPositionSelected = 0;
        addView(mLinealLayoutDots);
    }

    private OnClickListener onClickListenerIndicator(final int position){
        return new OnClickListener(){

            @Override
            public void onClick(View v) {
                if (position != oldPositionSelected)
                    mViewPager.setCurrentItem(position);
            }
        };
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        // Se cambia el Dot de la posicion anteriormente seleccionada
        if (oldPositionSelected >= 0){
            listIndicatorView.get(oldPositionSelected).setColor(unselectedIndicatorColor);
        }
        // Se cambia el Dot (selectedDot) a la nueva posicion seleccionada
        listIndicatorView.get(position).setColor(selectedIndicatorColor);
        oldPositionSelected = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * Convierte una dimensión DPI en Pixel
     * @param dpi
     * @return
     */
    private int applyDimensionPixel(int dpi){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpi,getResources().getDisplayMetrics());
    }


    /**
     * IndicatorView extiende de un ImageView
     * representa el Indicador
     */
    private class IndicatorView extends ImageView{

        @Dimension
        static final int DEFAULT_RADIUS_DIP = 3;

        @ColorInt
        static final int DEFAULT_COLOR = Color.BLACK;
        @ColorInt
        static final int DEFAULT_SELECTED_COLOR = Color.RED;

        @NonNull
        private final ShapeDrawable dot = new ShapeDrawable(new OvalShape());

        @Px
        private int dotRadius;

        public IndicatorView(Context context) {
            super(context);
            init(context,null,0,0);
        }

        public IndicatorView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init(context,attrs,0,0);
        }

        public IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(context,attrs,defStyleAttr,0);
        }

        public IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            init(context,attrs,defStyleAttr,defStyleRes);
        }

        private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            setImageDrawable(dot);
        }

        @Px
        int getRadius() {
            return dotRadius;
        }

        void setRadius(@Px int newRadius) {
            dotRadius = newRadius;

            final int diameter = newRadius * 2;
            dot.setIntrinsicWidth(diameter);
            dot.setIntrinsicHeight(diameter);

            invalidate();
        }

        @ColorInt
        int getColor() {
            return dot.getPaint().getColor();
        }

        void setColor(@ColorInt int color) {
            dot.getPaint().setColor(color);
            invalidate();
        }
    }


    /**
     * Adaptador para visualizar imagenes ya sea por URL o Resource
     */
    private class ViewPagerAdapter extends PagerAdapter {

        private Context context;
        private List<String> urlImages;
        private List<Integer> resourceImages;



        public ViewPagerAdapter(Context context, List<String> urlImages, List<Integer> resourceImages) {
            this.context = context;
            this.urlImages = urlImages;
            this.resourceImages = resourceImages;
        }

        @Override
        public int getCount() {
            return urlImages!=null? urlImages.size() : resourceImages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageLoaderView image = new ImageLoaderView(context);

            if (urlImages != null)
                image.setImageURL(urlImages.get(position));
            else
                image.setImagenResource(resourceImages.get(position));
            container.addView(image);
            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}

