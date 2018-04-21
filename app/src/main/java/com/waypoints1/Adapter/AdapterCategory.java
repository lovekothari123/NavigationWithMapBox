package com.waypoints1.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waypoints1.Fragment.CategoryListView;
import com.waypoints1.Model.CategoryModel;
import com.waypoints1.Model.CategoryWayPointsListModel;
import com.waypoints1.NavigationMap.MapModal;
import com.waypoints1.R;
import com.waypoints1.utility.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.MyViewHolder>{

    private Context context;
    RealmResults<CategoryModel> catListRealm;
    private int selectedItem = -1;
    private RelativeLayout rl_header;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Typeface custom_font;
    private Typeface custom_font_regular;
     RealmResults<CategoryModel> resultsone;
    private DisplayImageOptions options;
    private PopupWindow pw;
    DrawerLayout drawer_layout;
    List<CategoryWayPointsListModel> addAllDataInsed;
    Realm realm;
     RealmResults<MapModal> mapModals;




//    public AdapterCategory(Context context, RelativeLayout rl_header, ArrayList<CategoryModel> categoryList, FragmentManager fm) {
//        this.context = context;
//        this.categoryList = categoryList;
//        this.rl_header=rl_header;
//        this.fm=fm;
//    }

    public AdapterCategory(Context context, DrawerLayout drawer_layout, RelativeLayout rl_header, RealmResults<CategoryModel> results, FragmentManager fm) {
        this.context = context;
        this.catListRealm = results;

        this.rl_header=rl_header;
        this.fm=fm;
        this.drawer_layout=drawer_layout;


        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.drawer_logo)
                .showImageForEmptyUri(R.drawable.drawer_logo)
                .showImageOnFail(R.drawable.drawer_logo)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();


    }




    @Override
    public AdapterCategory.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cust_category, parent, false);
//        custom_font = Typeface.createFromAsset(context.getAssets(),"fonts/Montserrat-Bold.ttf");


        Realm.init(context.getApplicationContext());
        realm = Realm.getDefaultInstance();
        if(realm.isInTransaction()){

        }else {
            realm.beginTransaction();
        }
         mapModals = realm.where(MapModal.class).findAll();
         mapModals.load();
        custom_font_regular = Typeface.createFromAsset(context.getAssets(),"fonts/OpenSans-Semibold.ttf");
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterCategory.MyViewHolder holder, final int position) {



//        final CategoryModel item = categoryList.get(position);
        final CategoryModel item = catListRealm.get(position);

        holder.tv_category_name.setText(item.getName());

        holder.tv_category_name.setTypeface(custom_font_regular);

        Utils.getInstance().loadGlide(context,holder.iv_category_image,item.getImage());
//        ImageLoader.getInstance().displayImage(item.getImage(),holder.iv_category_image,options);



       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Log.d("AdapterCategory","Click_hua");
              int category_id =  catListRealm.get(position).getId();
              String image =  catListRealm.get(position).getImage();
              Log.d("AdapterCategory","Category_name==>"+category_id);
               addAllDataInsed = new ArrayList<>();

               for (int i = 0; i < mapModals.size(); i++) {
                   Log.d("AdapterCategory","mapModel_name==>"+mapModals.get(i).getName());
                   int id  =   mapModals.get(i).getCategory_id();


                   if(category_id==id){
                       Log.d("AdapterCategory","mapModel_name=inside_condition=>"+id+"===>"+mapModals.get(i).getWaypoint_id());
                       addAllDataInsed.add(new CategoryWayPointsListModel(mapModals.get(i).getName(), mapModals.get(i).getWaypoint_id(), mapModals.get(i).getLat(), mapModals.get(i).getLng()));

//                       pushInnerFragment(new CategoryListView(context, drawer_layout, rl_header, fm, addAllDataInsed,2), "AdapterCategory", true);
                   }else {

//                       pushFragment(new CategoryListView(context,drawer_layout,rl_header,fm,1),"Adapter",true);
                   }

               }

               if(addAllDataInsed.size()==0){

                   pushInnerFragment(new CategoryListView(context,drawer_layout,rl_header,fm,1),"Adapter",true);

               }else {
                   pushInnerFragment(new CategoryListView(context, drawer_layout, rl_header, fm, addAllDataInsed,image,2), "AdapterCategory", true);
               }




//
//
           }
       });
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

        return catListRealm.size();
    }

    public int getSelectedItem() {
        return selectedItem;
    }

//    public void updateItem(int index, CategoryModel item) {
//        categoryList.set(index, item);
//        this.notifyItemChanged(index);
//    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_category_name;
        ImageView iv_category_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_category_name = (TextView) itemView.findViewById(R.id.tv_category_name);
            iv_category_image = (ImageView) itemView.findViewById(R.id.iv_category_image);

//            iv_category_image.setClipToOutline(true);


        }
    }

    private void pushInnerFragment(Fragment fragment, String tag, boolean addToBackStack) {
        ft = fm.beginTransaction();
        ft.replace(R.id.content, fragment, tag);
//        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.commit();
    }
}