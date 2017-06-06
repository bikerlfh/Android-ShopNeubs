package co.com.neubs.shopneubs.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.List;

import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.classes.Helper;

/**
 * Created by bikerlfh on 6/6/17.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Activity activity;
    private List<String> images;
    private LayoutInflater inflater;

    public ViewPagerAdapter(Activity activity, List<String> images) {
        this.activity = activity;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater)activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item_producto_detalle,container,false);

        ImageView image = (ImageView)itemView.findViewById(R.id.img_producto_detalle);
       /* DisplayMetrics dis = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dis);
        int height = dis.heightPixels;
        int width = dis.widthPixels;
        image.setMinimumHeight(height);
        image.setMinimumWidth(width);*/

        Helper.GetImageCached(activity,images.get(position),image);
        //Glide.with(activity).load(images.get(position)).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.circular_progress_bar).into(image);

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
