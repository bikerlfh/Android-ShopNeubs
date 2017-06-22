package co.com.neubs.shopneubs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import co.com.neubs.shopneubs.controls.ImageLoaderView;

/**
 * Created by Tatiana on 22/06/2017.
 */

public class AdapterBanner extends BaseAdapter{
    Context context;
    int[] images;
    String[] names;
    LayoutInflater inflater;

    public AdapterBanner(Context applicationContext, String[] names, int[] images){
        this.context = applicationContext;
        this.images= images;
        this.names= names;
        inflater = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.content_item_banner,null);
        TextView NameG= (TextView) view.findViewById(R.id.name);
        ImageLoaderView ImageG= (ImageLoaderView) view.findViewById(R.id.img_banner);
        NameG.setText(names[position]);
        ImageG.setAdapter(images[position]);
        return view;
    }
}
