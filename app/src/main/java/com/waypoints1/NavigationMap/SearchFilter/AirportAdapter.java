//package com.demosmartstop.NavigationMap.SearchFilter;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Filterable;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.demosmartstop.NavigationMap.MapModal;
//import com.demosmartstop.R;
//import com.mapbox.mapboxsdk.style.layers.Filter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import io.realm.Case;
//import io.realm.Realm;
//import io.realm.RealmQuery;
//
//public class AirportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
//    Realm realm;
//    Context context;
//    private List<MapModal> names = null;
//    private ArrayList<MapModal> arraylist;
//
//    public AirportAdapter(Context context, Realm realm) {
//        this.realm = realm;
//    }
//
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.airport_show, parent, false);
//        AirportClass holder = new AirportClass(view);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        AirportR airportR = getData().get(position);
//
//        AirportClass mHolder = (AirportClass) holder;
//
//        mHolder.country.setText(airportR.getIsoCountry());
//        mHolder.name.setText(airportR.getName());
//    }
//
//    @Override
//    public int getItemCount() {
//        return names.size();
//    }
//
//    public void filterResults(String text) {
//        text = text == null ? null : text.toLowerCase().trim();
//        RealmQuery<MapModal> query = realm.where(MapModal.class);
//        if (!(text == null || "".equals(text))) {
//            query.contains("fieldToQueryBy", text, Case.INSENSITIVE);
//            // TODO: change field
//        }
//        updateData(query.findAll());
//    }
//
//
//    }
//
//    public class AirportClass extends RecyclerView.ViewHolder {
//        TextView name, country;
//        ImageView image;
//
////        public AirportClass(View itemView) {
//            super(itemView);
//
//            name = (TextView) itemView.findViewById(R.id.name);
//            country = (TextView) itemView.findViewById(R.id.country);
//            image = (ImageView) itemView.findViewById(R.id.imageView);
//        }
//    }
//}
