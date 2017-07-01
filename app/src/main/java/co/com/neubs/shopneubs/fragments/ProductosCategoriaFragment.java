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

import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.adapters.ProductoAdapter;
import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.APIValidations;
import co.com.neubs.shopneubs.classes.ConsultaPaginada;
import co.com.neubs.shopneubs.classes.GridSpacingItemDecoration;
import co.com.neubs.shopneubs.classes.Helper;
import co.com.neubs.shopneubs.classes.OnVerticalScrollListener;
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


    private RecyclerView recyclerView;
    ProductoAdapter productoAdapter;

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
            urlRequest = getArguments().getString(PARAM_URL_REQUEST,null);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_productos_categoria, container, false);
        // Inflate the layout for this fragment

        recyclerView = (RecyclerView)view.findViewById(R.id.myRecycleView);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, Helper.dpToPx(3,getContext()), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);

        recyclerView.setLayoutManager(mLayoutManager);

        // se visualiza el spinner de loading
        final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.progress_bar);
        spinner.setVisibility(View.VISIBLE);

        // Si la urlRequest es nula quiere decir que fue invocado el fragment desde una peticion filtro por categoria o marca
        if (codigoCategoria != null) {
            addParametroUrlRequest("categoria",codigoCategoria);
        }
        if (codigoMarca != null){
            addParametroUrlRequest("marca",codigoMarca);
        }

        APIRest.Async.get(urlRequest, new IServerCallback() {
            @Override
            public void onSuccess(String json) {
                final ConsultaPaginada consultaPaginada = APIRest.serializeObjectFromJson(json,ConsultaPaginada.class);
                productoAdapter = new ProductoAdapter(getActivity(),consultaPaginada,R.layout.cardview_producto);
                recyclerView.setAdapter(productoAdapter);
                recyclerView.addOnScrollListener(new OnVerticalScrollListener(){
                    @Override
                    public void onScrolledToBottom() {
                        super.onScrolledToBottom();
                        productoAdapter.getNextPage(view);
                    }
                });
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onError(String message_error, APIValidations apiValidations) {
                Snackbar.make(view,"Error:"+message_error,Snackbar.LENGTH_INDEFINITE).show();
                spinner.setVisibility(View.GONE);
            }
        });
        return view;
    }

    /**
     * Agrega un parametro GET a la urlRequest
     * @param key nombre del parametro
     * @param value valor del parametro
     */
    private  void addParametroUrlRequest(String key,String value){
        if (urlRequest == null)
            urlRequest = "";
        // Si la url ya contiene el signo ? se agrega la adición de un nuevo parametro (&)
        // de lo contrario agrega el signo ?
        urlRequest += (urlRequest.contains("?"))? "&" : "?";
        // Se adiciona el parametro get
        urlRequest += key + "=" + value;
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
