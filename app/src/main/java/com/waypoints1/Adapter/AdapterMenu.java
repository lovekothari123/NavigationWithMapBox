package com.waypoints1.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.waypoints1.Model.MENU;
import com.waypoints1.R;


import java.util.ArrayList;

/**
 * Created by jack94 on 05-09-2017.
 */

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.MyViewHolder>{
    private Context context;
    private ArrayList<MENU> arrayList;
    private int selectedItem = -1;
    int pos;

    private Typeface custom_font;
    private Typeface custom_font_regular;

    public AdapterMenu(Context context, ArrayList<MENU> list) {
        this.context = context;
        this.arrayList = list;
    }

    @Override
    public AdapterMenu.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_menu, parent, false);
        custom_font = Typeface.createFromAsset(context.getAssets(),"fonts/OpenSans-Bold.ttf");
        custom_font_regular = Typeface.createFromAsset(context.getAssets(),"fonts/OpenSans-Regular.ttf");
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AdapterMenu.MyViewHolder holder, final int position) {

        MENU item = arrayList.get(position);
        final String title = item.getTitle();
        final int icon = item.getIcon();
        holder.tv_title.setText(title);
        holder.iv_icon.setImageResource(icon);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    public void updateItem(int index, MENU item) {
        arrayList.set(index, item);
        this.notifyItemChanged(index);
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        ImageView iv_icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);

            tv_title.setTypeface(custom_font_regular);
        }
    }
}
