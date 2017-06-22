package co.com.neubs.shopneubs;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Tatiana on 21/06/2017.
 */

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ViewHolder> {
    private ArrayList<Item> items;

    AdapterItem(ArrayList<Item>items){
        this.items=items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item=items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items!=null?items.size():0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName;
        private ImageView imgItem;

        public ViewHolder(View itemView){
            super(itemView);
            this.txtName=(TextView)itemView.findViewById(R.id.txtName);
            this.imgItem=(ImageView) itemView.findViewById(R.id.imgItem);
        }
        public  void bind(final Item item){

            txtName.setText(item.getName());
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(),item.getName()+""+item.getId(),Toast.LENGTH_LONG).show();
                }
            });

            //Glide.with(itemView.getContext()).load(R.drawable.icon).into(imgItem);
        }

    }
}
