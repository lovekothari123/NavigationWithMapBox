package com.waypoints1.NavigationMap.SearchFilter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.waypoints1.NavigationMap.MapModal;
import com.waypoints1.R;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {


    private List<MapModal> names = null;
//    private List<SearchwayModel> names = null;

    Realm realm;
    double lat,lng;


    public CustomAdapter(List<SearchwayModel> names, Realm realm, Double lat, Double lng) {

        Log.d("mytag_adapter", "Current ==a===>" + lat+"=====>"+lng);

//        this.names=names;
        this.realm=realm;
        this.lat=lat;
        this.lng=lng;
    }

    public CustomAdapter(RealmResults<MapModal> names, Realm realm, Double lat, Double lng) {
        this.names=names;
        this.realm=realm;
        this.lat=lat;
        this.lng=lng;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d("mytag_adapter", "Current ==a===>" + names.size());

        for (int i = 0; i <names.size() ; i++) {
            Log.d("mytag_adapter", "Current ==a===>" + names.get(i).getWaypoint_id());

        }
            if ( names.get(position).getWaypoint_id().equals("N/A")) {
                Log.d("mytag_adapter", "Current=AlllDa===>" + names.get(position).getWaypoint_id());
                holder.textViewName.setText(names.get(position).getName());

                holder.getTextViewstate.setText("smartStop");


            }else {
                holder.textViewName.setText(names.get(position).getName());
                holder.getTextViewstate.setText(names.get(position).getWaypoint_id());
            }




    }

    @Override
    public int getItemCount() {
        return names.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName,getTextViewstate;

        ViewHolder(View itemView) {
            super(itemView);
             textViewName = (TextView) itemView.findViewById(R.id.textViewName);
             getTextViewstate = (TextView)itemView.findViewById(R.id.textViewState);


        }
    }
}