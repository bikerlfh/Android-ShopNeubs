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
import co.com.neubs.shopneubs.classes.ConsultaPaginada;
import co.com.neubs.shopneubs.classes.GridSpacingItemDecoration;
import co.com.neubs.shopneubs.classes.OnVerticalScrollListener;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OfertasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OfertasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OfertasFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private OnFragmentInteractionListener mListener;
    private ProductoAdapter productoAdapter;

    public OfertasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OfertasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OfertasFragment newInstance(String param1, String param2) {
        OfertasFragment fragment = new OfertasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_ofertas, container, false);



        // Inflate the layout for this fragment
        recyclerView = (RecyclerView)view.findViewById(R.id.recycle_view_oferta);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(3), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // get list saldo inventario from de Api
        final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.progress_bar);
        spinner.setVisibility(View.VISIBLE);
        APIRest.Async.get("ofertas/", new IServerCallback() {
            @Override
            public void onSuccess(String json) {
                ConsultaPaginada consultaPaginada = APIRest.serializeObjectFromJson(json,ConsultaPaginada.class);
                productoAdapter = new ProductoAdapter(getActivity(),consultaPaginada);
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
            public void onError(String message_error, String response) {
                Snackbar.make(view,"Error:"+message_error,Snackbar.LENGTH_INDEFINITE).show();
                spinner.setVisibility(View.GONE);
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
