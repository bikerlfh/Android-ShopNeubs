package co.com.neubs.shopneubs.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.APIValidations;
import co.com.neubs.shopneubs.classes.ConsultaPaginada;
import co.com.neubs.shopneubs.classes.models.Section;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.section_layout,parent,false);

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

        private View view;

        public ViewHolder(View itemView){
            super(itemView);
            this.txtTitle=(TextView)itemView.findViewById(R.id.txtTitle);
            this.txtSubTitle=(TextView) itemView.findViewById(R.id.txtSubTitle);
            this.rvItem=(RecyclerView) itemView.findViewById(R.id.rvItem);
            this.btnMore=(Button) itemView.findViewById(R.id.btnMore);
            view = itemView;
        }
        public void bind(final Section section){

            txtTitle.setText(section.getTitle());
            txtSubTitle.setText(section.getSubTitle());

            rvItem.setHasFixedSize(true);
            rvItem.setLayoutManager(new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.HORIZONTAL,false));

            APIRest.Async.get(section.getUrlProductos(), new IServerCallback() {
                @Override
                public void onSuccess(String json) {
                    final ConsultaPaginada consultaPaginada = APIRest.serializeObjectFromJson(json,ConsultaPaginada.class);
                    ProductoSectionItemAdapter productoAdapter = new ProductoSectionItemAdapter(view.getContext(),consultaPaginada);
                    rvItem.setAdapter(productoAdapter);
                }

                @Override
                public void onError(String message_error, APIValidations apiValidations) {

                }
            });

            btnMore.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                }
            });

            //Glide.with(itemView.getContext()).load(R.drawable.icon).into(imgItem);
        }

    }
}
