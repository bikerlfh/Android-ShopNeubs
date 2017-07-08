package co.com.neubs.shopneubs.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.com.neubs.shopneubs.PrincipalActivity;
import co.com.neubs.shopneubs.ProductoDetalleActivity;
import co.com.neubs.shopneubs.adapters.SectionAdapter;
import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.classes.models.APIBanner;
import co.com.neubs.shopneubs.classes.models.APISection;
import co.com.neubs.shopneubs.controls.ViewPagerNeubs;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IndexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndexFragment extends Fragment {

    private View mMainView;
    private View mProgressView;

    RecyclerView mRecyclerViewSection;
    ViewPagerNeubs mViewPagerBanner;

    List<APISection> listAPISections;
    SectionAdapter sectionAdapter;

    private boolean showBanner = false;

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

        mMainView = view.findViewById(R.id.main_view_index);
        mProgressView = view.findViewById(R.id.loading_progress_bar);


        mViewPagerBanner = (ViewPagerNeubs) view.findViewById(R.id.viewpager_banner);
        mRecyclerViewSection=(RecyclerView) view.findViewById(R.id.recycle_view_index);

        showLoadingView(true);
        AsyncInitial  asyncInitial = new AsyncInitial();
        asyncInitial.execute();
        return view;
    }

    class AsyncInitial extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            // Se cargan los banner
            cargarAPIBanner();
            // Se consultan las secciones activas
            listAPISections =new APISection().getAll(true);

            if(listAPISections !=null)
                sectionAdapter = new SectionAdapter(listAPISections);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            showLoadingView(false);
            mRecyclerViewSection.setHasFixedSize(true);
            if (listAPISections != null){
                mRecyclerViewSection.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                mRecyclerViewSection.setAdapter(sectionAdapter);
            }
            if (showBanner)
                // Se visualiza el viewPagerBanner
                mViewPagerBanner.setVisibility(View.VISIBLE);
        }
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
        // Se reinicializa pausa
        mViewPagerBanner.stopAutoScroll();
    }


    /**
     * Carga los ApiBanner
     */
    private void cargarAPIBanner(){
        // Se consultan todos los banner activos
        final List<APIBanner> listadoApiBanner = new APIBanner().getAll(true);

        if (listadoApiBanner.size() > 0) {
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
            showBanner = true;
        }
    }

    /**
     * Shows the progress UI and hides the main view
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showLoadingView(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getActivity().getResources().getInteger(android.R.integer.config_shortAnimTime);

            mMainView.setVisibility(show ? View.GONE : View.VISIBLE);
            mMainView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mMainView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mMainView.setVisibility(show ? View.GONE : View.VISIBLE);
        }

    }
}