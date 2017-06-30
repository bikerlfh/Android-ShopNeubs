package co.com.neubs.shopneubs.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import co.com.neubs.shopneubs.adapters.SectionAdapter;
import co.com.neubs.shopneubs.R;
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

        ArrayList<String> images = new ArrayList<String>();
        //images.add("http://192.168.1.50:8000/media/filer_public_thumbnails/filer_public/bb/0e/bb0eb875-1963-4416-a787-6c4c3e6f3a7e/img-carousel-1.jpg__1800x500_q85_crop_subsampling-2_upscale.jpg");
        images.add("https://www.caprabo.com/export/shared/.galleries/cajas/ofertas-y-promociones-caprabo.png_796588431.png");
        images.add("http://d37enp55yzymsd.cloudfront.net/images/Las-mejores-13-ofertas-en-cancun-para-nadar-con-delfines-554x247.jpg");
        images.add("http://www.sofacols.com/wp-content/uploads/2016/07/ofertas.png");
        mViewPagerBanner = (ViewPagerNeubs) view.findViewById(R.id.adpaterViewFlipper_banner);
        if (images.size() > 0) {
            mViewPagerBanner.showGalleryImages(images);
            mViewPagerBanner.setVisibility(View.VISIBLE);
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
