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

public class AdapterBanner extends BaseAdapter{
    Context context;
    ArrayList<String> listImages;
    LayoutInflater inflater;

    public AdapterBanner(Context context, ArrayList<String> images){
        this.context = context;
        this.listImages = images;
        inflater = (LayoutInflater.from(context));
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
        view = inflater.inflate(R.layout.content_item_banner,null);
        ImageLoaderView imagen = (ImageLoaderView) view.findViewById(R.id.img_banner);
        imagen.setImageURL(listImages.get(position));
        return view;
    }
}
