package co.com.neubs.shopneubs.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.ProductoDetalleActivity;
import co.com.neubs.shopneubs.adapters.SectionAdapter;
import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.classes.models.APIBanner;
import co.com.neubs.shopneubs.classes.models.Section;
import co.com.neubs.shopneubs.controls.ViewPagerNeubs;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IndexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndexFragment extends Fragment {

    ArrayList<Section> sections;
    SectionAdapter sectionAdapter;
    RecyclerView rvSection;
    ViewPagerNeubs mViewPagerBanner;

    private APIBanner apiBanner;


    public IndexFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IndexFragment newInstance() {
        return new IndexFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_index, container, false);
        apiBanner = new APIBanner();
        final List<APIBanner> listadoApiBanner = apiBanner.getAllApiBanner(true);

        if (listadoApiBanner.size() > 0) {
            final ArrayList<String> images = new ArrayList<>();
            for(APIBanner banner:listadoApiBanner){
                images.add(banner.getUrlImagen());
            }
            mViewPagerBanner = (ViewPagerNeubs) view.findViewById(R.id.adpaterViewFlipper_banner);
            if (images.size() > 0) {
                mViewPagerBanner.showGalleryImages(images, new ViewPagerNeubs.OnClickListenerImage() {
                    @Override
                    public void onClick(View v, int position) {
                        final APIBanner apiBanner = listadoApiBanner.get(position);
                        if (apiBanner.isClickable()){
                            // si viene el idSaldoInventario, se abre el ProductoDetalleActivity
                            if (apiBanner.getIdSaldoInventario() > 0){
                                Intent intent = new Intent(v.getContext(), ProductoDetalleActivity.class);
                                intent.putExtra(ProductoDetalleActivity.PARAM_ID_SALDO_INVENTARIO,apiBanner.getIdSaldoInventario());
                                v.getContext().startActivity(intent);
                            }
                            // Si viene el urlResultado, se envía al fragment ProductosCategoria
                            else if(apiBanner.getUrlResultado() != null && apiBanner.getUrlResultado().length() > 0){

                            }
                        }
                    }
                });
                mViewPagerBanner.setVisibility(View.VISIBLE);
            }
        }


        rvSection=(RecyclerView) view.findViewById(R.id.rvSection);
        rvSection.setHasFixedSize(true);

        sections=Section.getAllSections();

        if(sections!=null) {
            sectionAdapter =new SectionAdapter(sections);
            rvSection.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            rvSection.setAdapter(sectionAdapter);
        }
        return view;

    }

}