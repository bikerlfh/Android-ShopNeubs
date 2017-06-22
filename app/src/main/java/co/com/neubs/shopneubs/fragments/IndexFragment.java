package co.com.neubs.shopneubs.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;

import java.util.ArrayList;

import co.com.neubs.shopneubs.adapters.AdapterSection;
import co.com.neubs.shopneubs.AdapterBanner;
import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.classes.models.Section;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IndexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndexFragment extends Fragment {

    ArrayList<Section> sections;
    AdapterSection adapterSection;
    RecyclerView rvSection;
    AdapterViewFlipper mAdapterViewFlipper;

    int[] images={R.drawable.carousel3, R.drawable.carousel4};
    String[] names={"Imagen 1","Imagen2"};

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
        //LoginFragment fragment = new LoginFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
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
        mAdapterViewFlipper = (AdapterViewFlipper) view.findViewById(R.id.adpaterViewFlipper_banner);
        if (images.length > 0) {
            AdapterBanner customAdapter = new AdapterBanner(getContext(), names, images);
            mAdapterViewFlipper.setAdapter(customAdapter);
            //mAdapterViewFlipper.setFlipInterval(2000);
            //mAdapterViewFlipper.setAutoStart(true);
            mAdapterViewFlipper.setVisibility(View.VISIBLE);
        }


        rvSection=(RecyclerView) view.findViewById(R.id.rvSection);
        rvSection.setHasFixedSize(true);

        sections=Section.getAllSections();

        if(sections!=null) {
            adapterSection=new AdapterSection(sections);
            rvSection.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            rvSection.setAdapter(adapterSection);
        }
        return view;

    }

}
