package com.waypoints1.Fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.waypoints1.Adapter.AdapterCategory;
import com.waypoints1.Helper.CustomHeaderWithRelative;
import com.waypoints1.Model.CategoryModel;
import com.waypoints1.Model.CategoryWaypoints;
import com.waypoints1.R;
import com.waypoints1.utility.Const;
import com.waypoints1.utility.Utils;
import com.waypoints1.utility.WebInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class Category extends Fragment {

    private DrawerLayout drawer_layout;
    private RelativeLayout rl_header;
    private Context context;
    private FragmentManager fm;
    private View rootView;
    private ProgressDialog mProgressDialog;
    private ArrayList<CategoryModel> categoryList=new ArrayList<>();
    private RecyclerView rv_category;
    private AdapterCategory adapterCategory;
    Realm realmcat,waypointrealm;

    Bitmap bitmap ;
    File mediaImage;
    static int count = -1;
     ProgressDialog progressDoalog;
//    ArrayList<CategoryWaypoints> waypointsdata  = new ArrayList<>();
    RealmList<CategoryWaypoints> waypointsdata  = new RealmList<>();

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int First_time_load_key;
    int HeadID;


    //    RealmConfiguration realmConfiguration1;
    public Category(Context context, DrawerLayout drawer_layout, RelativeLayout rl_header, FragmentManager fm) {
        Log.d("Category","=>Calll=="+rl_header);
        this.drawer_layout = drawer_layout;
        this.rl_header = rl_header;
        this.context = context;
        this.fm = fm;

    }

    public Category(Context context, DrawerLayout drawer_layout, RelativeLayout rl_header, FragmentManager fm, int i) {
        this.drawer_layout = drawer_layout;
        this.rl_header = rl_header;
        this.context = context;
        this.fm = fm;
        this.HeadID = i;
        Log.d("HeaderId","id===>"+HeadID);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RealmConfiguration  realmConfiguration1   = new RealmConfiguration.Builder()
                .name("realmNameCategory")
                .deleteRealmIfMigrationNeeded()
                .build();
        realmcat = Realm.getInstance(realmConfiguration1);

        RealmConfiguration  realmConfiguratio2   = new RealmConfiguration.Builder()
                .name("realmwaypointsCategory")
                .deleteRealmIfMigrationNeeded()
                .build();
        waypointrealm = Realm.getInstance(realmConfiguratio2);
        progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Its loading....");
        progressDoalog.setCancelable(false);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_category, container, false);

        if(HeadID == 1){
            CustomHeaderWithRelative.setInnerToolbar(getActivity(), rl_header, "CATEGORIES");

        }else {
            CustomHeaderWithRelative.setOuter(getActivity(), drawer_layout, rl_header, "CATEGORIES");
            Log.d("Category", "onCreateView===>" + rl_header);
        }
        //TODO ADD VALUES IN PREFRANCE.....

        pref = context.getApplicationContext().getSharedPreferences(getString(R.string.SharedPrefranceKey), Context.MODE_PRIVATE); // 0 - for private mode

        First_time_load_key =   pref.getInt(getString(R.string.key_cat), 0);
        Log.d("Category_pref","Value===0=>"+First_time_load_key);


//        editor.remove("email");
//        editor.commit();
//        editor.clear();
//        editor.commit();
        try{
            if(realmcat.isInTransaction() && waypointrealm.isInTransaction()){

            }else {
                realmcat.beginTransaction();
                waypointrealm.beginTransaction();
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        init();

        //TODO Checking Internet Connection

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            final boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnected();
            if (isConnected) {

                if(First_time_load_key == 1){
                    Log.d("Category_pref","Value==2=>"+First_time_load_key);
                    getOffline();
                }else {
                    Log.d("Category_pref","Value==3=>"+First_time_load_key);
                    if(realmcat != null){
                        realmcat.deleteAll();
                        waypointrealm.deleteAll();
                    }
                    getCategory();
                }

            } else {

                getOffline();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO NETEWORK CHECKING

        return rootView;
    }

    private void getOffline() {

        //TODO Checking Internet Connection

//
                final RealmResults<CategoryModel> results = realmcat.where(CategoryModel.class).findAll();
//                final RealmResults<CategoryWaypoints> allwaypoints = waypointrealm.where(CategoryWaypoints.class).findAll();
//
                Log.d("OFFLINECAT","Offlien_size====>"+results.size());

                for (int i = 0; i <results.size() ; i++) {
                    Log.d("OFFLINECAT","Offline_name"+results.get(i).getName());
                    Log.d("OFFLINECAT","Offline_Image"+results.get(i).getImage());
                    Log.d("OFFLINECAT","Offline_id"+results.get(i).getId());
                    }

//        for (int i = 0; i <allwaypoints.size() ; i++) {
//            if(allwaypoints.size() != 0) {
//                Log.d("OFFLINECAT", "Offline_way_pointId" + allwaypoints.get(i).getWaypoint_id());
//                Log.d("OFFLINECAT", "Offline_way_point_Lat" + allwaypoints.get(i).getLat());
//                Log.d("OFFLINECAT", "Offline_way_point_Longg" + allwaypoints.get(i).getLongg());
//            }
//
//        }

        Log.d("OFFLINECAT","Offlien_size_catagery_list===>"+categoryList.size());


        if(results !=null) {
//                    adapterCategory = new AdapterCategory(context, rl_header, categoryList, fm);
                    adapterCategory = new AdapterCategory(context, drawer_layout,rl_header, results, fm);
                    rv_category.setAdapter(adapterCategory);

            editor = pref.edit();
            editor.putInt(getString(R.string.key_cat), 1); // Storing long
            editor.commit();


                }else {
                    Toast.makeText(context,"You are in offline mode!",Toast.LENGTH_SHORT).show();
                }

        //TODO NETEWORK CHECKING
    }

    private void init() {
        rv_category=rootView.findViewById(R.id.rv_category);
        rv_category.setLayoutManager(new GridLayoutManager(context,3));
    }

    private void getCategory() {

        categoryList=new ArrayList<>();
        waypointsdata = new RealmList<>();

        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);
                mProgressDialog.setMessage("Loading");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                String response = null;
                try {
                    response = WebInterface.getInstance().doGet(Const.CATEGORY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Utils.getInstance().d(" image Final Response : " + response);
                return response;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                Log.d("mytag","Inseide Post Excecute___CAll");
                mProgressDialog.dismiss();
//                waypointsdata.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        Log.d("mytag","Inseide Post Excecute___data");

                        JSONArray data= jsonObject.getJSONArray("data");

                        Log.d("mytag","Inseide Post Excecute___waypoints");

                        for (int i=0;i<data.length();i++) {

                            JSONObject category_data = data.getJSONObject(i);
                            int id = category_data.getInt("id");
                            Log.d("Not_CAt", "get_cat_==id==>"+id);

                            String name = category_data.getString("name");
                            Log.d("Not_CAt", "get_cat_==name==>"+id);

                            String image = category_data.getString("image");
                            Log.d("Not_CAt", "get_cat_==image==>"+image);

//                            JSONArray waypoints = category_data.getJSONArray("waypoints");
//



//                                Log.d("mytag", "Inseide Post Excecute__length===>" + waypoints.length());

//                                for (int j = 0; j < waypoints.length(); j++) {
//
//                                Log.d("mytag", "Inseide Post Excecute___waypoints.length()===?" + waypoints.length());
//                                JSONObject waypoints_data = waypoints.getJSONObject(j);
//                               String waypoint_id = waypoints_data.getString("waypoint_id");
//
//                               String waypoint_name = waypoints_data.getString("name");
//
//                               double lat = waypoints_data.getDouble ("lat");
//
//                               double longg = waypoints_data.getDouble("longg");
//
//                               String category = waypoints_data.getString("category");
//                               Log.d("mytag","Category===>"+category);
//                               int cagry = Integer.parseInt(category);
//                               Log.d("mytag","Way_points_category==>"+cagry);
//                                  if(cagry == id){
//                                  Log.d("mytag","All Id and Category==>"+id+"====>"+category);
//                                      waypointsdata.add(new CategoryWaypoints(waypoint_id, waypoint_name, lat, longg));
//
//                                      CategoryWaypoints obj2 = waypointrealm.createObject(CategoryWaypoints.class);
//
//                                      obj2.setWaypoint_name(waypointsdata.get(j).getWaypoint_name());
//                                      Log.d("mytag","data_after_added===>"+obj2.getWaypoint_name());
//                                      obj2.setWaypoint_id(waypointsdata.get(j).getWaypoint_id());
//                                      obj2.setLat(waypointsdata.get(j).getLat());
//                                      obj2.setLongg(waypointsdata.get(j).getLongg());
//                                  }
//
//                                }
//

                            categoryList.add(new CategoryModel(id,name,image,waypointsdata));

                        }


                        InsertImageWithPath();
                    }
                } catch (Exception e) {

                }

            }
        }.execute();
    }



    private void InsertImageWithPath() {

          count++;
        Log.d("Category","Count++===>"+count);

        if(count == categoryList.size()){

            if(progressDoalog.isShowing()){
                progressDoalog.dismiss();
            }else {
//             /   progressDoalog.dismiss();
            }
            Log.d("Call_InsertData", "Here_we_go");
            realmcat.commitTransaction();
            waypointrealm.commitTransaction();
            getOffline();

        }

        if(count<categoryList.size()) {



            categoryList.get(count).getId();
            final String image;
            final String s;
            Log.d("Not_CAt", "HEre==>" + categoryList.get(count).getImage()+"===ID No===>"+categoryList.get(count).getId());

            image = categoryList.get(count).getImage();
            Log.d("Not_CAt","image+path==>"+image);
            s = categoryList.get(count).getName();
            Log.d("Not_CAt","image+name==>"+s);

//            count++;



//            Log.d("Category", "HEre==>" + categoryList.get(count).getImage());
//

            if(progressDoalog.isShowing()){
                Log.d("Not_CAt","inside on isShowing==>");


            }else {
                progressDoalog.show();
                Log.d("Not_CAt","inside on showProgress==>");

            }

            //TODO ADD IMAGE IN DB
//           final File file;
            if (image != null) {

               final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d("Not_CAt","inside try_call==>");

                        URL url = null;
                        url = new URL(image);
                        URLConnection conn = null;
                        conn = url.openConnection();
                        bitmap = BitmapFactory.decodeStream(conn.getInputStream());
                        Log.e("Not_CAt", "PAth===Image URl=>" + url);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bitmap.setHasAlpha(true);
                                File sdcard = Environment.getExternalStorageDirectory();
                                File folder = new File(sdcard.getAbsoluteFile(), ".smartStop");//the dot makes this directory hidden to the user
                                folder.mkdir();
                                mediaImage = new File(folder.getAbsoluteFile(), s + ".png");
                                Log.d("Not_CAt", "File PAth=====>" + mediaImage.getPath());

                                try {
                                    FileOutputStream out = new FileOutputStream(mediaImage);
                                    bitmap.setHasAlpha(true);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                                    out.flush();
                                    out.close();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Log.d("Call_InsertData", "File PAth===33==>" + mediaImage.getPath());

//                                progressDoalog.dismiss();
                                //TODO SET ADTA INTO REALM
                                Log.d("Call_InserData", "Realm_Calll");
                                if (mediaImage.getPath() != null) {

                                    Log.d("Category", "PAth_not_null");
                                   try {

                                       CategoryModel obj1;
                                       obj1 = realmcat.createObject(CategoryModel.class);

                                       obj1.setId(categoryList.get(count).getId());
                                       Log.d("Call_InsertData", "categoryList id==for=>" + obj1.getId());

                                       obj1.setImage(mediaImage.getPath());
                                       obj1.setName(categoryList.get(count).getName());
                                       Log.d("Call_InsertData", "categoryList name==for=>" + obj1.getName());
                                       Log.d("Call_InsertData", "categoryList Waypointname==for=>" + waypointsdata.size());



//                                        obj2 = waypointrealm.createObject(CategoryWaypoints.class);




//                                  obj1.setWaypointsdata(categoryList.get(count).getWaypointsdata());
//                                   Log.d("Call_InsertData","Array_list==>"+obj1.getWaypointsdata());

//                                       obj.setWaypoint_name(waypointsdata.get(count).getWaypoint_name());


//                                       obj2.setWaypoint_name(waypointsdata.get(count).getWaypoint_name());
//                                       Log.d("Call_InsertData", "categoryList Waypointname==for==W_name=>" + obj2.getWaypoint_name());
//                                       obj2.setWaypoint_id(waypointsdata.get(count).getWaypoint_id());
//                                       Log.d("Call_InsertData", "categoryList Waypointname==for==W_id=>" + obj2.getWaypoint_id());
//                                       obj2.setLat(waypointsdata.get(count).getLat());
//                                       Log.d("Call_InsertData", "categoryList Waypointname==for==W_Lat=>" + obj2.getLat());
//                                       obj2.setLongg(waypointsdata.get(count).getLongg());
//                                       Log.d("Call_InsertData", "categoryList Waypointname==for==W_Longg=>" + obj2.getLongg());

                                   }catch (Exception e){
                                       e.printStackTrace();
                                   }

                                } else {
                                    Log.d("Category", "PAth_is_null");

                                }
                                //TODO SET ADTA INTO REALM

                                InsertImageWithPath();
//                                count++;


                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            });
            thread.start();

        }else {
//                progressDoalog.dismiss();
                InsertImageWithPath();
//                count++;

            }
//            thread.destroy();
              //TODO ADD IMAGE IN DB

//            InsertImageWithPath();
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        count = -1;
//        realmcat.close();
    }

    //TODO Ander WALa For Loop



}
