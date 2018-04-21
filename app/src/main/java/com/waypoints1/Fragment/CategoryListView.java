package com.waypoints1.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waypoints1.Adapter.CategoriesListingAdapter;
import com.waypoints1.Helper.CustomHeaderWithRelative;
import com.waypoints1.Model.CategoryWayPointsListModel;
import com.waypoints1.NavigationMap.GpsTrackrer.GPSTracker;
import com.waypoints1.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
@SuppressLint("ValidFragment")
public class CategoryListView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Context context;
    RelativeLayout rl_header;
    FragmentManager  fm;
    List<CategoryWayPointsListModel> addAllDataInsed;
    CategoriesListingAdapter categoriesListingAdapter;
    DrawerLayout drawer_layout;
    TextView category_list_no_such_data_found;
    RecyclerView categories_listing_recycle_view;
    double lat,longg;
    // TODO: Rename and change types of parameters
    View rootView;
    int count = 0;
    String image;

    public CategoryListView(Context context, DrawerLayout drawer_layout, RelativeLayout rl_header, FragmentManager fm, List<CategoryWayPointsListModel> addAllDataInsed,String image,int i) {
        this.context=context;
        this.rl_header=rl_header;
        this.fm=fm;
        this.addAllDataInsed=addAllDataInsed;
        Log.d("Cateffff0","------>"+addAllDataInsed);
        this.drawer_layout=drawer_layout;
        this.count=i;
        this.image =image;
    }

    public CategoryListView(Context context, DrawerLayout drawer_layout, RelativeLayout rl_header, FragmentManager fm, int i) {
        this.context=context;
        this.rl_header=rl_header;
        this.fm=fm;
        this.count=i;
        Log.d("Cateffff0","------>"+count);
        this.drawer_layout=drawer_layout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_category_list_view, container, false);
        CustomHeaderWithRelative.setInnerToolbar(getActivity(), rl_header, "CATEGORIES");

        category_list_no_such_data_found= (TextView)rootView.findViewById(R.id.category_list_no_such_data_found);
        categories_listing_recycle_view = (RecyclerView) rootView.findViewById(R.id.categories_listing_recycle_view);

        if(count==1){
            category_list_no_such_data_found.setVisibility(View.VISIBLE);
            categories_listing_recycle_view.setVisibility(View.GONE);
        }else {

            //TODO GPS TRACKER

            category_list_no_such_data_found.setVisibility(View.GONE);
            categories_listing_recycle_view.setVisibility(View.VISIBLE);

            final GPSTracker gpsTracker = new GPSTracker(context);
            if (gpsTracker.canGetLocation()) {

                Log.d("CategoryList", "in gps enabled");
                lat = gpsTracker.getLatitude();
                longg = gpsTracker.getLongitude();
            } else {
                gpsTracker.showSettingsAlert();
            }

            Log.d("CategoryList", "lat -" + lat + " lng -" + longg);


            float[] resultsdb = new float[1];
            for (int i = 0; i < addAllDataInsed.size(); i++) {
                Location.distanceBetween(addAllDataInsed.get(i).getLat(), addAllDataInsed.get(i).getLongg(), lat, longg, resultsdb);
            }
            float distanceInMeters = resultsdb[0];
            float KmDB = distanceInMeters / 1000;

            Log.d("mytag", "Database Distance" + KmDB);
            for (int i = 0; i < addAllDataInsed.size(); i++) {
                Log.d("ViewCat", "===>DAta" + addAllDataInsed.get(i).getLat());
                Log.d("ViewCat", "===>DAta" + addAllDataInsed.get(i).getWaypoint_name());
            }
            if (KmDB <= 5) {

                //TODO GPS TRACKER

                for (int i = 0; i < addAllDataInsed.size(); i++) {
                    Log.d("ViewCat", "With  in 5 Km===>DAta" + addAllDataInsed.get(i).getLat());
                    Log.d("ViewCat", "With in 5 Kn===>DAta" + addAllDataInsed.get(i).getWaypoint_name());

                }


                Log.d("mytag", "not null cat detail");
                categories_listing_recycle_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                categories_listing_recycle_view.setHasFixedSize(true);
                categories_listing_recycle_view.setVisibility(View.VISIBLE);
                category_list_no_such_data_found.setVisibility(View.GONE);
                categoriesListingAdapter = new CategoriesListingAdapter(context, drawer_layout, addAllDataInsed, fm, rl_header,image);
                categories_listing_recycle_view.setAdapter(categoriesListingAdapter);
            }else {
                category_list_no_such_data_found.setVisibility(View.VISIBLE);
                categories_listing_recycle_view.setVisibility(View.GONE);
            }

        }



        return rootView;
    }


}
