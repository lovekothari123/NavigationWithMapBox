package com.waypoints1.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.waypoints1.Adapter.AdapterMenu;
import com.waypoints1.Fragment.Category;
import com.waypoints1.Fragment.FAQ;
import com.waypoints1.Fragment.SmartStops;
import com.waypoints1.Fragment.TellAFriend;
import com.waypoints1.Fragment.TermsAndCondition;
import com.waypoints1.Fragment.Weather;
import com.waypoints1.NavigationMap.GpsTrackrer.GPSTracker;
import com.waypoints1.NavigationMap.SearchFilter.RecyclerItemClickListener;
import com.waypoints1.Model.MENU;

import com.waypoints1.R;
import com.waypoints1.utility.Const;
import com.waypoints1.utility.Prefs;
import com.waypoints1.utility.Utils;
import com.waypoints1.utility.WebInterface;
import com.whinc.widget.ratingbar.RatingBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeActivity extends AppCompatActivity implements DrawerLayout.DrawerListener, View.OnClickListener {

    private ArrayList<MENU> menuArrayList;
    private AdapterMenu adapterMenu;
    private RecyclerView rv_menu;
    private DrawerLayout drawer_layout;
    private ActionBarDrawerToggle toggle;
    private RelativeLayout main_content, rl_header;
    private FrameLayout content;
    private Button btn_call;
    boolean homePressed = true, doubleBackToExitPressedOnce = false;
    private PopupWindow pw;
    private String ratings;
    private int pos = -1;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private String android_id;
    private ProgressDialog mProgressDialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editorrating;
    public GPSTracker gpsTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        Prefs.getPrefInstance().setValue(this, Const.USER_ID, android_id);
        pref = getApplicationContext().getSharedPreferences(getString(R.string.SharedPrefranceKey), Context.MODE_PRIVATE);

        gpsTracker = new GPSTracker(HomeActivity.this);


        init();
        listner();

        checkcondition();

    }

    private void checkcondition() {
        if (gpsTracker.canGetLocation()) {
        } else {
            gpsTracker.showSettingsAlert();

        }
    }



    private void init() {

        if (Build.VERSION.SDK_INT >= 19)
        {
            CheckPermission();
        }

        rl_header = (RelativeLayout) findViewById(R.id.rl_header);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        main_content = (RelativeLayout) findViewById(R.id.main_content);
        content = (FrameLayout) findViewById(R.id.content);

        btn_call = rl_header.findViewById(R.id.btn_call);

        toggle = new ActionBarDrawerToggle(this, drawer_layout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(this);
        toggle.syncState();

        rv_menu = (RecyclerView) findViewById(R.id.rv_menu);
        rv_menu.setLayoutManager(new LinearLayoutManager(HomeActivity.this));

        String[] menuTitle = new String[0];
        int[] menuIcon = new int[0];
        menuTitle = new String[]{"HOME", "CATEGORIES SEARCH", "WEATHER", "FAQ", "RATE","SHARE MY APP"};
        menuIcon = new int[]{R.drawable.ic_home,R.drawable.ic_category,R.drawable.ic_weather,
                R.drawable.ic_faq,R.drawable.ic_rate,R.drawable.ic_share_my_app};

        menuArrayList = new ArrayList<>();
        for (int i = 0; i < menuTitle.length; i++) {
            menuArrayList.add(new MENU(menuTitle[i], menuIcon[i]));
        }
        adapterMenu = new AdapterMenu(this, menuArrayList);
        rv_menu.setAdapter(adapterMenu);

        if (Prefs.getPrefInstance().getValue(HomeActivity.this, Const.WARNING_STATUS,"").equals("1"))
        {
            pushFragment(new SmartStops(HomeActivity.this, drawer_layout, rl_header, getSupportFragmentManager()), "smart stop", false);
        }
        else
        {
            pushFragment(new TermsAndCondition(HomeActivity.this, drawer_layout, rl_header, getSupportFragmentManager()), "terms fragment", false);
        }
    }

    private void listner() {

        btn_call.setOnClickListener(this);

        rv_menu.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                adapterMenu.setSelectedItem(position);
                pos = position;



                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        MENU item = menuArrayList.get(position);
                        String title = item.getTitle();


                        switch (title) {

                            case "HOME":

                                pushFragment(new SmartStops(HomeActivity.this, drawer_layout, rl_header, getSupportFragmentManager()), "home", false);

//                                Fragment f = getSupportFragmentManager().findFragmentById(R.id.content);
//                                if (f instanceof SmartStops) {
//                                       SmartStops.layoutVisbility();
////                                    drawer_layout.closeDrawer(GravityCompat.START);
//                                }else {
//                                    SmartStops.layoutVisbility();
//
//                                    pushFragment(new SmartStops(HomeActivity.this, drawer_layout, rl_header, getSupportFragmentManager()), "home", false);
////                                    drawer_layout.closeDrawer(GravityCompat.START);
//
//                                }
                                break;

//                            case "SMART STOP":
////                                Fragment f1 = getSupportFragmentManager().findFragmentById(R.id.content);
////                                if (f1 instanceof SmartStops) {
////                                    SmartStops.layoutVisbility();
////
////                                    drawer_layout.closeDrawer(GravityCompat.START);
////                                }else {
////
////                                    SmartStops.layoutVisbility();
////
////                                    pushFragment(new SmartStops(HomeActivity.this, drawer_layout, rl_header, getSupportFragmentManager()), "home", false);
////                                    drawer_layout.closeDrawer(GravityCompat.START);
////
////                                }
//                                pushFragment(new SmartStops(HomeActivity.this, drawer_layout, rl_header, getSupportFragmentManager()), "smart stop", false);
//                                break;

//                            case "USER MANUAL":
//                                pushFragment(new UserManual(HomeActivity.this, drawer_layout, rl_header, getSupportFragmentManager()), "user manual", false);
//                                drawer_layout.closeDrawer(GravityCompat.START);
//
//                                break;

                            case "CATEGORIES SEARCH":
                                pushFragment(new Category(HomeActivity.this, drawer_layout, rl_header, getSupportFragmentManager()), "category", false);
                                drawer_layout.closeDrawer(GravityCompat.START);

                                break;

                            case "WEATHER":
                                pushFragment(new Weather(HomeActivity.this, drawer_layout, rl_header, getSupportFragmentManager()), "weather", false);
                                drawer_layout.closeDrawer(GravityCompat.START);

                                break;

                            case "SHARE MY APP":
                                pushFragment(new TellAFriend(HomeActivity.this, drawer_layout, rl_header, getSupportFragmentManager()), "tell frnd", false);
                                drawer_layout.closeDrawer(GravityCompat.START);

                                break;

                            case "FAQ":
                                pushFragment(new FAQ(HomeActivity.this, drawer_layout, rl_header, getSupportFragmentManager()), "faq", false);
                                drawer_layout.closeDrawer(GravityCompat.START);
                                break;

                            case "RATE":
                                openRateUsPopUp();
                                break;

                        }
                        drawer_layout.closeDrawer(GravityCompat.START);
                    }
                },500);

            }
        }));

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void CheckPermission() {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("Fine Location");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("Coarse Location");
        if (!addPermission(permissionsList, Manifest.permission.CALL_PHONE))
            permissionsNeeded.add("Phone Call");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(HomeActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (HomeActivity.this.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//                    loadLocation();
                } else {
                    Toast.makeText(HomeActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;

        }
    }

    private void openRateUsPopUp() {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) HomeActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.popup,
                    (ViewGroup) findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT, true);
            // display the popup in the center
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);


            RatingBar rating_bar = (RatingBar) layout.findViewById(R.id.rating_bar);
            rating_bar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
                @Override
                public void onChange(RatingBar ratingBar, int rating, int i1) {
                    ratings = String.valueOf(ratingBar.getCount());
                }
            });
            final EditText Reating_name = (EditText)layout.findViewById(R.id.rating_name);
            final EditText Rating_email = (EditText)layout.findViewById(R.id.rating_email);
            final EditText Rating_phone = (EditText)layout.findViewById(R.id.rating_phone_no);
            final  EditText Rating_Feedback = (EditText)layout.findViewById(R.id.rating_feedback);

            Button btn_submit = (Button) layout.findViewById(R.id.btn_submit);
            Button btn_later = (Button)layout.findViewById(R.id.rating_btn_later);

            btn_later.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pw.dismiss();


                }
            });


                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String Rname,Remail,Rphone,Rfeedback;
                        Rname = Reating_name.getText().toString();
                        Remail = Rating_email.getText().toString();
                        Rphone = Rating_phone.getText().toString();
                        Rfeedback = Rating_Feedback.getText().toString();

                        Log.d("Rating_tag", "Name==>" + Reating_name.getText().toString());
                        Log.d("Rating_tag", "Email==>" + Rating_email.getText().toString());
                        Log.d("Rating_tag", "Phone==>" + Rating_phone.getText().toString());
                        Log.d("Rating_tag", "Rating_Feedback==>" + Rating_Feedback.getText().toString());


                        if(!Reating_name.getText().toString().isEmpty() && !Rating_email.getText().toString().isEmpty() && !Rating_phone.getText().toString().isEmpty() ) {




                            //TODO Checking Internet Connection

                            try {
                                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                                final boolean isConnected = activeNetwork != null &&
                                        activeNetwork.isConnected();
                                if (isConnected) {

                                    rateApp(Rname,Remail,Rphone,Rfeedback,ratings);
                                    pw.dismiss();



                                    SharedPreferences  pref = HomeActivity.this.getSharedPreferences(getString(R.string.SharedPrefranceKey), Context.MODE_PRIVATE);
                                    final String rating_name = pref.getString(getString(R.string.rating_name), null);
                                    final String rating_email = pref.getString(getString(R.string.rating_email), null);
                                    final String rating_phoneNo = pref.getString(getString(R.string.rating_phoneNo), null);
                                    final String rating_feedback = pref.getString(getString(R.string.rating_feedback), null);
                                    final String rating_ratestar = pref.getString(getString(R.string.rating_ratestar), null);


                                    Log.d("Rating", "aaa==data=>" + rating_name+""+rating_email+""+rating_phoneNo+""+rating_feedback+""+rating_ratestar);


                                    if(rating_name != null && rating_email !=null && rating_phoneNo != null && rating_feedback != null && rating_ratestar!=null){

                                        rateApp(rating_name,rating_email,rating_phoneNo,rating_feedback,rating_ratestar);

                                        editorrating.remove(rating_name);
                                        editorrating.remove(rating_email);
                                        editorrating.remove(rating_phoneNo);
                                        editorrating.remove(rating_feedback);
                                        editorrating.remove(rating_ratestar);



                                    }



                                } else {


                                    SharedPreferences pref = HomeActivity.this.getSharedPreferences(HomeActivity.this.getString(R.string.SharedPrefranceKey), Context.MODE_PRIVATE);
                                    editorrating = pref.edit();
                                    editorrating.putString(HomeActivity.this.getString(R.string.rating_name), Rname);
                                    editorrating.putString(HomeActivity.this.getString(R.string.rating_email), Remail);
                                    editorrating.putString(HomeActivity.this.getString(R.string.rating_phoneNo), Rphone);
                                    editorrating.putString(HomeActivity.this.getString(R.string.rating_feedback), Rfeedback);
                                    editorrating.putString(HomeActivity.this.getString(R.string.rating_ratestar), ratings);
                                    editorrating.commit();
                                    Toast.makeText(HomeActivity.this,"Thank you for rating us! Your Feedback is very valuable for us.",Toast.LENGTH_SHORT).show();





                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //TODO NETEWORK CHECKING



                            pw.dismiss();



                        }else {

                            if(Reating_name.getText().toString().isEmpty()){
                         Log.d("ErrorRating","NAme is empty");

                              Toast.makeText(getApplicationContext(),"Please Enter Name",Toast.LENGTH_SHORT).show();

                                //                                Reating_name.setError("Please Enter name");
//                                Reating_name.requestFocus();

                            }else if(Rating_email.getText().toString().isEmpty()){
                                Log.d("ErrorRating","emai is empty");
                                Toast.makeText(getApplicationContext(),"Please Enter Emai Id",Toast.LENGTH_SHORT).show();


                                //                                Rating_email.setError("Please Enter Email");
//                                Rating_email.requestFocus();

                            }

                        }





//                    Toast.makeText(HomeActivity.this, "Ratings - " + ratings, Toast.LENGTH_SHORT).show();
                    }
                });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void rateApp(final String Rname, final String Remail, final String Rphone, final String Rfeedback,final String ratings) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog = new ProgressDialog(HomeActivity.this,R.style.MyAlertDialogStyle);
                mProgressDialog.setMessage("Loading");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(true);
                mProgressDialog.show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                String response = null;
                try {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("user_id", Prefs.getPrefInstance().getValue(HomeActivity.this,Const.USER_ID,""));
                    jsonObject.put("rate", ratings);
                    jsonObject.put("name",Rname);
                    jsonObject.put("email",Remail);
                    jsonObject.put("phone",Rphone);
                    jsonObject.put("feedback",Rfeedback);

                    Utils.getInstance().d("json object"+ jsonObject.toString());
                    Log.d("Rating_tag","Json"+jsonObject);

                    response = WebInterface.getInstance().doPostRequest(Const.RATINGS,jsonObject.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Utils.getInstance().d("ratings Final Response : " + response);

                Log.d("Rating_tag","Json"+response);

                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                mProgressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        Toast.makeText(HomeActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                        //TODO CALL HOME SCREEE HERE .....
                        Fragment f = getSupportFragmentManager().findFragmentById(R.id.content);
                        if (f instanceof SmartStops) {
                            SmartStops.hoempageopen();

                            drawer_layout.closeDrawer(GravityCompat.START);
                        }else {


                            pushFragment(new SmartStops(HomeActivity.this, drawer_layout, rl_header, getSupportFragmentManager()), "home", false);
                            SmartStops.hoempageopen();
                            drawer_layout.closeDrawer(GravityCompat.START);


                        }

                        //TODO CALL HOME SCREEE HERE .....



                    }
                } catch (Exception e) {

                }
            }
        }.execute();
    }


    private void pushFragment(Fragment fragment, String tag, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
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
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(main_content.getWindowToken(), 0);

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call:
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 911));
                startActivity(i);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        int values=  pref.getInt(getString(R.string.key_cat), 0);
        int values1=  pref.getInt(getString(R.string.Key_waypoints), 0);
        Log.d("Category_pref","Hone==1=OnDestory=>"+values+"==SmartStopValue=="+values1);
        editor = pref.edit();
        editor.clear();
        editor.putInt(getString(R.string.key_cat),0);
        editor.putInt(getString(R.string.Key_waypoints),0);
        int values33=  pref.getInt(getString(R.string.key_cat), 0);
        int values133=  pref.getInt(getString(R.string.Key_waypoints), 0);
        Log.d("Category_pref","Honme===2=OnDestory=>"+values33+"==SmartStopValue=="+values133);
        Log.d("Category_pref","Prefs=OnDestory=====>");
        editor.commit();
        finish();
//        try {
//            finalize();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }

    }

    @Override
    public void onDetachedFromWindow() {
        Log.d("Category_pref","Home OnDetec");
        super.onDetachedFromWindow();
    }


    @Override
    public void onBackPressed() {
        if (rl_header.getTag().equals("Outer")) {


            //ToDo Network Error
            //ToDo Network Error
            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("SmartStop");
            builder.setMessage("Are you sure you want to exit?");
            builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    finish();
                   finishAffinity();


                }
            });
            android.app.AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
            //ToDo Till HERE
            //ToDo Till HERE




//            if (doubleBackToExitPressedOnce) {
//                finish();
//                finishAffinity();
//
//                return;
//            }
//            this.doubleBackToExitPressedOnce = true;
////            Utils.getInstance().toast(this, "Please click BACK again to exit");
//            Toast.makeText(HomeActivity.this, "Are you sure you want to exit?", Toast.LENGTH_SHORT).show();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    doubleBackToExitPressedOnce = false;
//                }
//            }, 2000);
        } else if (rl_header.getTag().equals("Inner")) {
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }


}
