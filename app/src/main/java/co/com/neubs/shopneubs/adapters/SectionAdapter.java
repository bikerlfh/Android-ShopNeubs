package co.com.neubs.shopneubs.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import co.com.neubs.shopneubs.PrincipalActivity;
import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.APIValidations;
import co.com.neubs.shopneubs.classes.ConsultaPaginada;
import co.com.neubs.shopneubs.classes.models.APISection;
import co.com.neubs.shopneubs.fragments.ProductosCategoriaFragment;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

/**
 * Created by Tatiana on 21/06/2017.
 */

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.ViewHolder> {
    public List<APISection> listAPISections;

    public SectionAdapter(List<APISection> listAPISections){
        this.listAPISections = listAPISections;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.section_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        APISection APISection = listAPISections.get(position);
        holder.bind(APISection);
    }

    @Override
    public int getItemCount() {
        return listAPISections !=null? listAPISections.size():0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mLblTitle;
        private TextView mLblSubTitle;
        private RecyclerView mRecyclerViewItem;
        private Button  mBtnMore;

        private View view;

        public ViewHolder(View itemView){
            super(itemView);
            this.mLblTitle = (TextView)itemView.findViewById(R.id.txtTitle);
            this.mLblSubTitle = (TextView) itemView.findViewById(R.id.txtSubTitle);
            this.mRecyclerViewItem = (RecyclerView) itemView.findViewById(R.id.rvItem);
            this.mBtnMore = (Button) itemView.findViewById(R.id.btnMore);

            // se configura el RecycleView
            mRecyclerViewItem.setHasFixedSize(true);
            mRecyclerViewItem.setLayoutManager(new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.HORIZONTAL,false));
            view = itemView;
        }
        public void bind(final APISection apiSection){

            mLblTitle.setText(apiSection.getTitle());

            if (apiSection.getSubTitle() != null && apiSection.getSubTitle().length() > 0) {
                mLblSubTitle.setText(apiSection.getSubTitle());
            }else{
                mLblSubTitle.setVisibility(View.INVISIBLE);
            }

            // Se realiza la petición de los productos a la API
            APIRest.Async.get(apiSection.getUrlRequestProductos(), new IServerCallback() {
                @Override
                public void onSuccess(String json) {
                    final ConsultaPaginada consultaPaginada = APIRest.serializeObjectFromJson(json,ConsultaPaginada.class);
                    final ProductoAdapter productoAdapter = new ProductoAdapter(view.getContext(),consultaPaginada,R.layout.cardview_section_item);
                    mRecyclerViewItem.setAdapter(productoAdapter);
                }

                @Override
                public void onError(String message_error, APIValidations apiValidations) {

                }
            });
            // funcionalidad boton más
            final String urlMas = apiSection.getUrlRequestMas();
            if (urlMas != null && urlMas.length() > 0) {
                mBtnMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args = new Bundle();
                        args.putString(ProductosCategoriaFragment.PARAM_URL_REQUEST, urlMas);
                        Fragment fragment = new ProductosCategoriaFragment();
                        fragment.setArguments(args);
                        ((FragmentActivity)v.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, fragment, PrincipalActivity.TAG_FRAGMENT).commit();
                    }
                });
            }
            else{
                // se esconde el boton
                mBtnMore.setVisibility(View.INVISIBLE);
            }
        }
    }
}
