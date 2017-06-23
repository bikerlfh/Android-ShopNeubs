package co.com.neubs.shopneubs.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.controls.ImageLoaderView;

/**
 * Created by Tatiana on 22/06/2017.
 */

public class BannerAdapter extends BaseAdapter{
    Context context;
    ArrayList<String> listImages;

    public BannerAdapter(Context context, ArrayList<String> images){
        this.context = context;
        this.listImages = images;
    }
    @Override
    public int getCount() {
        return listImages.size();
    }

    @Override
    public Object getItem(int position) {
        return listImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.content_item_banner,null);
        ImageLoaderView imagen = (ImageLoaderView) view.findViewById(R.id.img_banner);
        imagen.setImageURL(listImages.get(position));
        return view;
    }
}
