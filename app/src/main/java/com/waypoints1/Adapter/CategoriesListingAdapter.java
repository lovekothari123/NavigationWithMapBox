package com.waypoints1.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waypoints1.Fragment.SmartStops;
import com.waypoints1.Model.CategoryWayPointsListModel;
import com.waypoints1.R;

import java.util.List;

/**
 * Created by love on 27-Mar-18.
 */

public class CategoriesListingAdapter extends RecyclerView.Adapter<CategoriesListingAdapter.ViewHolder>{

Context context;
    List<CategoryWayPointsListModel> addAllDataInsed;
    FragmentManager fm;
    RelativeLayout rl_header;
    private DrawerLayout drawer_layout;
    int one = 1;
    String imaeg;


    public CategoriesListingAdapter(Context context, DrawerLayout drawer_layout, List<CategoryWayPointsListModel> addAllDataInsed, FragmentManager fm, RelativeLayout rl_header,String image) {
        this.context=context;
        this.addAllDataInsed=addAllDataInsed;
        this.fm=fm;
        this.rl_header=rl_header;
        this.drawer_layout=drawer_layout;
        this.imaeg=image;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ctegory_listing_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {




        holder.textTwo.setText(addAllDataInsed.get(position).getWaypoint_name());
        holder.textView.setText(addAllDataInsed.get(position).getWaypoint_id());
            holder.textTwo.setSelected(true);
            holder.textView.setSelected(true);

        Glide.with(context).load(imaeg).placeholder(R.drawable.drawer_logo).into(holder.imageView);
//            holder.imageView.setImageResource(addAllDataInsed.get(position).getImage());

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pushFragment(new SmartStops(context,fm,drawer_layout, rl_header,addAllDataInsed.get(position).getWaypoint_id(),addAllDataInsed.get(position).getWaypoint_name(),addAllDataInsed.get(position).getLat(),addAllDataInsed.get(position).getLongg(),imaeg,one),"home", false);
//                    pushFragment(new SmartStops(context, drawer_layout, rl_header, fm), "home", false);

                }
            });

//            Utils.getInstance().loadGlide(context, holder.imageView, image);

    }
    private void pushFragment(Fragment fragment, String tag, boolean addToBackStack) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, fragment, tag);
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);


        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        ft.commit();
        ft.commitAllowingStateLoss();
    }

    @Override
    public int getItemCount() {
        return addAllDataInsed.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView,textTwo;
        Button button;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.category_listing_view_image);
            textView = (TextView)itemView.findViewById(R.id.category_listing_view_text);
            textTwo = (TextView)itemView.findViewById(R.id.category_listing_view_text_two);
            button = (Button)itemView.findViewById(R.id.ctegory_listing_get_direction);

        }
    }
}
