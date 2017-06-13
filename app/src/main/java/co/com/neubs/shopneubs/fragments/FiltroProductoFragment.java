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
import java.util.HashMap;
import java.util.Map;

import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.adapters.ProductoAdapter;
import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.APIValidations;
import co.com.neubs.shopneubs.classes.ConsultaPaginada;
import co.com.neubs.shopneubs.classes.GridSpacingItemDecoration;
import co.com.neubs.shopneubs.classes.OnVerticalScrollListener;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FiltroProductoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FiltroProductoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FiltroProductoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PARAM_FILTRO = "PARAM_FILTRO";

    // TODO: Rename and change types of parameters
    private String filtro;

    private RecyclerView recyclerView;
    ProductoAdapter productoAdapter;

    private OnFragmentInteractionListener mListener;

    public FiltroProductoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment FiltroProductoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FiltroProductoFragment newInstance(String param1) {
        FiltroProductoFragment fragment = new FiltroProductoFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_FILTRO, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            filtro = getArguments().getString(PARAM_FILTRO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_filtro_producto, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycle_view_filtro_producto);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(3), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);

        recyclerView.setLayoutManager(mLayoutManager);

        final Snackbar snackbar = Snackbar.make(view, R.string.text_loading, Snackbar.LENGTH_INDEFINITE)
                .setAction("Action", null);
        snackbar.show();

        Map<String, String> parametros = new HashMap<>();
        parametros.put("filtro",filtro);
        APIRest.Async.get(APIRest.URL_FILTRO_PRODUCTO,parametros,null, new IServerCallback() {
            @Override
            public void onSuccess(String json) {
                final ConsultaPaginada consultaPaginada = APIRest.serializeObjectFromJson(json,ConsultaPaginada.class);
                productoAdapter = new ProductoAdapter(getActivity(),consultaPaginada);
                recyclerView.setAdapter(productoAdapter);
                recyclerView.addOnScrollListener(new OnVerticalScrollListener(){
                    @Override
                    public void onScrolledToBottom() {
                        super.onScrolledToBottom();
                        productoAdapter.getNextPage(view);

                    }
                });
                snackbar.dismiss();
            }

            @Override
            public void onError(String message_error, APIValidations apiValidations) {
                if (apiValidations!= null)
                    Snackbar.make(view,"Bad Request:"+apiValidations.getResponse(),Snackbar.LENGTH_INDEFINITE).show();
                else
                    Snackbar.make(view,"Error:"+message_error,Snackbar.LENGTH_INDEFINITE).show();
                snackbar.dismiss();
            }

        });
        return view;
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

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
