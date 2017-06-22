package co.com.neubs.shopneubs;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Tatiana on 21/06/2017.
 */

public class AdapterSection extends RecyclerView.Adapter<AdapterSection.ViewHolder> {
    public ArrayList<Section> sections;

    public AdapterSection(ArrayList<Section> sections){
        this.sections=sections;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_section,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Section section=sections.get(position);
        holder.bind(section);
    }

    @Override
    public int getItemCount() {
        return sections!=null?sections.size():0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTitle;
        private TextView txtSubTitle;
        private RecyclerView rvItem;
        private Button  btnMore;

        public ViewHolder(View itemView){
            super(itemView);
            this.txtTitle=(TextView)itemView.findViewById(R.id.txtTitle);
            this.txtSubTitle=(TextView) itemView.findViewById(R.id.txtSubTitle);
            this.rvItem=(RecyclerView) itemView.findViewById(R.id.rvItem);
            this.btnMore=(Button) itemView.findViewById(R.id.btnMore);
        }
        public  void bind(final Section section){

            txtTitle.setText(section.getTitle());
            txtSubTitle.setText(section.getSubTitle());
            AdapterItem adapterItem=new AdapterItem(section.getItems());
            rvItem.setHasFixedSize(true);
            rvItem.setLayoutManager(new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.HORIZONTAL,false));
            rvItem.setAdapter(adapterItem);
            btnMore.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(),"id" +section.getId(),Toast.LENGTH_LONG).show();
                }
            });

            //Glide.with(itemView.getContext()).load(R.drawable.icon).into(imgItem);
        }

    }
}
