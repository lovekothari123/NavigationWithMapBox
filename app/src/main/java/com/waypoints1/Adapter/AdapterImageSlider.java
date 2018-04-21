package com.waypoints1.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.waypoints1.Model.More2;
import com.waypoints1.R;
import com.waypoints1.utility.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


import java.util.ArrayList;

/**
 * Created by Android18 on cd-11-2016.
 */
public class AdapterImageSlider extends PagerAdapter {

    private ArrayList<View> views = new ArrayList<View>();
    private ArrayList<More2> imageArraylist;
    private Context context;
    private DisplayImageOptions options;

    public AdapterImageSlider(ArrayList<More2> imageArraylist, Context context) {
        this.context = context;
        this.imageArraylist = imageArraylist;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.splech_scree_updated_logo)
                .showImageForEmptyUri(R.drawable.splech_scree_updated_logo)
                .showImageOnFail(R.drawable.splech_scree_updated_logo)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public int getCount() {
        return imageArraylist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);



        View rootView1 = inflater.inflate(R.layout.row_image_slider_home, container, false);
        TextView tv_title = (TextView) rootView1.findViewById(R.id.tv_title);
//        TextView tv_producer= (TextView) rootView1.findViewById(R.id.tv_producer);
        ImageView iv_image = (ImageView) rootView1.findViewById(R.id.iv_image);
        More2 item = imageArraylist.get(position);
//        tv_title.setText(item.getTitle());

        Utils.getInstance().d("slider image adapter - "+item.getImage());

        ImageLoader.getInstance().displayImage(item.getImage(),iv_image,options);

//        loadGlideForSlider(context, iv_image, item.getImage());


//        ImageLoader.getInstance().displayImage(item.getImage(),iv_image);
        container.addView(rootView1);
        return rootView1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        int index = views.indexOf(object);
        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }

    public void loadGlideForSlider(Context context, ImageView iv, String url) {
        try {
            Glide.with(context)
                    .load(url)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .centerCrop()
                    .placeholder(R.drawable.drawer_back)
//                    .crossFade()
                    .into(iv);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}