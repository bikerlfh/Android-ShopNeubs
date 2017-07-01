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

import co.com.neubs.shopneubs.PrincipalActivity;
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

    RecyclerView mRecyclerViewSection;
    ViewPagerNeubs mViewPagerBanner;

    ArrayList<Section> sections;
    SectionAdapter sectionAdapter;

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

        /* region Funcionalidad del banner */

        // Se consultan todos los banner activos
        final List<APIBanner> listadoApiBanner = new APIBanner().getAll(true);
        if (listadoApiBanner.size() > 0) {

            mViewPagerBanner = (ViewPagerNeubs) view.findViewById(R.id.viewpager_banner);
            // Se sacan las imagenes de los banners
            final ArrayList<String> images = new ArrayList<>();
            for(APIBanner banner:listadoApiBanner){
                images.add(banner.getUrlImagen());
            }
            // Se visualiza el viewPager como Galeria de imagenes y se
            // asigna el evento del click por imagen, para abrir el producto(idSaldoInventario) o el fragment ProductoCategoria(urlRequest)
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
                        // Si viene el urlRequest, se envía al fragment ProductosCategoria
                        else if(apiBanner.getUrlRequest() != null && apiBanner.getUrlRequest().length() > 0){
                            Bundle args = new Bundle();
                            args.putString(ProductosCategoriaFragment.PARAM_URL_REQUEST, apiBanner.getUrlRequest());
                            Fragment fragment = new ProductosCategoriaFragment();
                            fragment.setArguments(args);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, fragment, PrincipalActivity.TAG_FRAGMENT).commit();
                            // Se detiene el autoScroll
                            mViewPagerBanner.stopAutoScroll();
                        }
                    }
                }
            });
            // Se visualiza el viewPagerBanner
            mViewPagerBanner.setVisibility(View.VISIBLE);
        }
        /*  endregion */

        mRecyclerViewSection=(RecyclerView) view.findViewById(R.id.recycle_view_index);
        mRecyclerViewSection.setHasFixedSize(true);

        sections = Section.getAllSections();

        if(sections!=null) {
            sectionAdapter =new SectionAdapter(sections);
            mRecyclerViewSection.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mRecyclerViewSection.setAdapter(sectionAdapter);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Se reinicializa el autoScroll
        mViewPagerBanner.reinitializeAutoScroll();
    }

    @Override
    public void onStop() {
        super.onStop();
        mViewPagerBanner.stopAutoScroll();
    }
}