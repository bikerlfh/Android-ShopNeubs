package co.com.neubs.shopneubs.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.Map;

import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.adapters.ProductoAdapter;
import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.APIValidations;
import co.com.neubs.shopneubs.classes.ConsultaPaginada;
import co.com.neubs.shopneubs.classes.GridSpacingItemDecoration;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.classes.OnVerticalScrollListener;
import co.com.neubs.shopneubs.controls.NavigationViewFiltro;
import co.com.neubs.shopneubs.controls.VistaFiltroPrincipal;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductosCategoriaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductosCategoriaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductosCategoriaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String PARAM_CODIGO_CATEGORIA = "CODIGO_CATEGORIA";
    public static final String PARAM_CODIGO_MARCA = "CODIGO_MARCA";
    public static final String PARAM_URL_REQUEST = "URL_REQUEST";

    // TODO: Rename and change types of parameters
    private String codigoCategoria;
    private String codigoMarca;
    /**
     * Almacena la url al cual se desea se realize la petición a la API
     */
    private String urlRequest;

    /**
     * vistaFiltroPrincipal
     */
    private VistaFiltroPrincipal vistaFiltroPrincipal;


    private OnFragmentInteractionListener mListener;

    public ProductosCategoriaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductosCategoriaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductosCategoriaFragment newInstance(String param1, String param2) {
        ProductosCategoriaFragment fragment = new ProductosCategoriaFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CODIGO_CATEGORIA, param1);
        args.putString(PARAM_CODIGO_MARCA, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            codigoCategoria = getArguments().getString(PARAM_CODIGO_CATEGORIA,null);
            codigoMarca = getArguments().getString(PARAM_CODIGO_MARCA,null);
            urlRequest = getArguments().getString(PARAM_URL_REQUEST,"");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_productos_categoria, container, false);
        // Inflate the layout for this fragment

        vistaFiltroPrincipal = (VistaFiltroPrincipal) view.findViewById(R.id.vista_filtro_principal);

        final Map<String,String> params = new HashMap<>();
        if (codigoCategoria != null) {
            params.put("categoria",codigoCategoria);
        }
        if (codigoMarca != null){
            params.put("marca",codigoMarca);
        }


        // Se asigna el evento click del boton aplicarFiltro
        vistaFiltroPrincipal.setOnClickListenerAplicarFiltro(new NavigationViewFiltro.OnClickListenerAplicarFiltro() {
            @Override
            public void onClick(View v, Map<String, String> filtro) {
                // Se le agrega al filtro los parametros
                if (filtro != null)
                    params.putAll(filtro);
                consultarPeticionAPI(urlRequest,params);
                vistaFiltroPrincipal.closeDrawer();
            }
        });

        consultarPeticionAPI(urlRequest,params);

        return view;
    }

    private void consultarPeticionAPI(String url,Map<String,String> params){
        vistaFiltroPrincipal.showLoadingProgressBar(true);
        APIRest.Async.get(url,params, new IServerCallback() {
            @Override
            public void onSuccess(String json) {
                final ConsultaPaginada consultaPaginada = APIRest.serializeObjectFromJson(json,ConsultaPaginada.class);
                // Se visualizan los productos en el recycleView de la vistaFiltroPrincipal
                vistaFiltroPrincipal.showProductos(consultaPaginada);
            }

            @Override
            public void onError(String message_error, APIValidations apiValidations) {
                vistaFiltroPrincipal.showLoadingProgressBar(false);
                //Snackbar.make(view,"Error:"+message_error,Snackbar.LENGTH_INDEFINITE).show();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
