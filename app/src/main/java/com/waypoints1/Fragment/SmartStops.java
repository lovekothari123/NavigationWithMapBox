package com.waypoints1.Fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenuView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mapbox.services.android.telemetry.location.GoogleLocationEngine;
import com.waypoints1.Activities.HomeActivity;
import com.waypoints1.Activities.NavigationActivity;
import com.waypoints1.Helper.CustomHeaderWithRelative;
import com.waypoints1.NavigationMap.GpsTrackrer.GPSTracker;
import com.waypoints1.NavigationMap.ImageModel;
import com.waypoints1.NavigationMap.OfflineImagePathModel;
import com.waypoints1.NavigationMap.RoutesMap.DirectionsJSONParser;
import com.waypoints1.NavigationMap.SearchFilter.RecyclerItemClickListener;
import com.waypoints1.NavigationMap.MapModal;
import com.waypoints1.NavigationMap.SearchFilter.CustomAdapter;
import com.waypoints1.NavigationMap.SearchFilter.SearchFilterModel;
import com.waypoints1.NavigationMap.SearchFilter.SearchwayModel;
import com.waypoints1.R;
import com.waypoints1.utility.Const;
import com.waypoints1.utility.Utils;
import com.waypoints1.utility.WebInterface;
import com.github.clans.fab.FloatingActionButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.acl.Permission;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class SmartStops extends Fragment implements View.OnClickListener, Permission, ActivityCompat.OnRequestPermissionsResultCallback, OnMapReadyCallback, LocationListener, GoogleMap.OnMyLocationChangeListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private DrawerLayout drawer_layout;
    private RelativeLayout rl_header;
    private Context context;
    String Image_url_From_result, Offline_Images_path;
    private FragmentManager fm;
    private View rootView;
    ProgressDialog progressDoalog;
    int REquestConde = 1;
    private ProgressDialog mProgressDialog;
    //TODO Import MapBox Contants..........
    Realm realm;
    Marker marker, MyLocationIcon, flagMarkerone, flagMarkertwo;
    // variables for adding location layer
    private GoogleMap map;
    Marker markersearchCategory;
    Polyline polyline;
    SharedPreferences pref;
    public Location getGetOriginLocation;
    BroadcastReceiver receiverDataChange;
    // variables for adding a marker
    private Marker destinationMarker;
    private LatLng originCoord;
    private com.mapbox.mapboxsdk.geometry.LatLng originCoord_nav;
    private com.mapbox.mapboxsdk.geometry.LatLng destinationCoord_nav;

    private LatLng destinationCoord;
    EditText locationSearch;
    Location getOriginLocation;
    int all_pin_point_addes;
    double currentLatitude, currentLOngitude;
    // variables for calculating and drawing a route
    private LatLng originPosition;
    private Point originPosition_nav;
    private Point destinationPosition_nav;

    private LatLng destinationPosition;
    private static final String TAG = "DirectionsActivity";
    static NavigationMenuView navigationMapRoute;
    static Button button, search_button, getdirection_BT, back_buttion_map, back_bution_search, goTo_BT, End_Trip_BT;
    Animation animation;
    CustomAdapter adapter;
    URL url;
    static FloatingActionButton fab, recenter_icon;
    RecyclerView recyclerViewsearchfilter;
    static LinearLayout layoutone, layout_search, Map_layout;
    ArrayList<Double> all_latLong = new ArrayList<>();
    Bitmap myBitmap;
    Bitmap bit_from_device, AllImagesFromBitmap;
    RealmResults<MapModal> results_realm_search;
    SearchView searchView;
    Realm realm_search;
    List<MapModal> datastore = new ArrayList<>();
    List<SearchFilterModel> searchFilterModelslist = new ArrayList<>();
    Double lat = null, lng = null;
    List<String> dataSearchFilter = new ArrayList<>();
    RealmResults<MapModal> results_realm_search1;
    List<SearchwayModel> searchwayModels;
    MarkerOptions markerOptions;
    Thread thread;
    LatLng latLngdatabase_online_pins;
    PolylineOptions lineOptions;
    double Googlelatitude, Googlelong;
    List<String> images_icon = new ArrayList<>();
    ArrayList<ImageModel> image_model = new ArrayList<>();
    static ArrayList<OfflineImagePathModel> OfflienPath = new ArrayList<>();
    SharedPreferences.Editor editor;
    LocationManager locationManager;
    GoogleApiClient apiClient;
    LocationRequest locationRequest;
    String search;
    Bitmap bitmap;
    File mediaImage;
    int First_time_load_key;
    private FragmentTransaction ft;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Timer T;
    ImageView iv_menu;
    Button btn_call, whare_to;
    ProgressDialog progressDoalog1;
    int count = -1;
    String waypoint_id;
    String waypoint_name;
    double cat_lat;
    double cat_longg;
    int digit = 0;
    private TextToSpeech tts;
    String image;
    GPSTracker gpsTracker;
    private String provider;
    Circle circle;
    Marker markersearch = null;
    public String awsPoolId = null;
    public boolean simulateRoute = false;
    public Point origin_nav, destination_nav;
    List<LatLng> AllPolylinelatlongg;

    double Routeslat;
    double Routeslongg;
    Location onlocationchange;
    LatLng RoutesLAtLongg = null;
    double DestinationLatt = 0.0, DestinationLongg = 0.0;
    SharedPreferences sharedPreferences;


    //TODO Import MapBox Contants..........
    public SmartStops(Context context, DrawerLayout drawer_layout, RelativeLayout rl_header, FragmentManager fm) {
        this.drawer_layout = drawer_layout;
        this.rl_header = rl_header;
        this.context = context;
        this.fm = fm;
    }

    public SmartStops(Context context, FragmentManager fm, DrawerLayout drawer_layout, RelativeLayout rl_header, String waypoint_id, String waypoint_name, double lat, double longg, String image, int digit) {
        this.context = context;
        this.fm = fm;
        this.rl_header = rl_header;
        this.drawer_layout = drawer_layout;
        this.waypoint_id = waypoint_id;
        this.waypoint_name = waypoint_name;
        this.cat_lat = lat;
        this.cat_longg = longg;
        this.digit = digit;
        this.image = image;
        Log.d("Digit", "Now Digit===>" + digit);
        Log.d("Digit", "Now Digit===>" + lat);
        Log.d("Digit", "Now Digit===>" + longg);
        Log.d("Digit", "Now Digit===>" + waypoint_name);
        Log.d("Digit", "Now Digit===>" + waypoint_id);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_smart_stops, container, false);
        Mapbox.getInstance(getContext(), getString(R.string.access_token));
        CustomHeaderWithRelative.setOuter(getActivity(), drawer_layout, rl_header, "SMARTSTOPS");
        gpsTracker = new GPSTracker(context);

        //TODO LOCATION MANGER

        //TODO LOCATION MANGER

        iv_menu = rl_header.findViewById(R.id.iv_menu);
        btn_call = rl_header.findViewById(R.id.btn_call);

        iv_menu.setVisibility(View.VISIBLE);
        btn_call.setVisibility(View.VISIBLE);

        //TODO GOOLGE MAP INTIGRATION HERE
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        Map_layout = (LinearLayout) rootView.findViewById(R.id.Map_layout);

        //TODO Loader
        progressDoalog1 = new ProgressDialog(context);
        progressDoalog1.setMessage("Fetching waypoints....");
        progressDoalog1.setCanceledOnTouchOutside(true);
        progressDoalog1.setCancelable(false);
        progressDoalog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pref = context.getApplicationContext().getSharedPreferences(getString(R.string.SharedPrefranceKey), Context.MODE_PRIVATE); // 0 - for private mode

        //TODO Loader

        //TODO GOOLGE MAP INTIGRATION HERE


        Realm.init(getActivity().getApplicationContext());
        realm = Realm.getDefaultInstance();

        if (realm.isInTransaction()) {
            Log.d("DATA", "ONCREAT IF InTranscation");
        } else {
            Log.d("DATA", "ONCREAT IF else");
            realm.beginTransaction();

        }
        if (Build.VERSION.SDK_INT >= 19) {
            if (checkPermission()) {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission(); // Code for permission
            }
        } else {

            // Code for Below 23 API Oriented Device
            // Do next code
        }


        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
            checkpermission();
        }

        whare_to = (Button) rootView.findViewById(R.id.whare_to);
        whare_to.setVisibility(View.VISIBLE);
        whare_to.setOnClickListener(this);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        recenter_icon = (FloatingActionButton) rootView.findViewById(R.id.recenter_icon);
        recenter_icon.setOnClickListener(this);

        layoutone = (LinearLayout) rootView.findViewById(R.id.layoutone);
//           layoutVisbility();
        layout_search = (LinearLayout) rootView.findViewById(R.id.layout_search);
        getdirection_BT = (Button) rootView.findViewById(R.id.getdirection_BT);
        goTo_BT = (Button) rootView.findViewById(R.id.goTo_BT);

        End_Trip_BT = (Button) rootView.findViewById(R.id.End_Trip_BT);

//
        animation = AnimationUtils.loadAnimation(context, R.anim.anim);
        back_buttion_map = (Button) rootView.findViewById(R.id.back_buttion_map);
        back_bution_search = (Button) rootView.findViewById(R.id.back_bution_search);
        button = (Button) rootView.findViewById(R.id.startButton);


        MapBoxCallHere();
        registerData();
        listner();
        onChangelocationupdateicon();

        return rootView;
    }

    private void onChangelocationupdateicon() {
        T = new Timer();
        if (T != null) {
            T.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (getActivity() != null) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                if (gpsTracker.canGetLocation()) {

//                                    Toast.makeText(context, "Long==>" + gpsTracker.getLocation().getLongitude(), Toast.LENGTH_SHORT).show();

                                    if (MyLocationIcon != null) {
                                        MyLocationIcon.remove();
                                    }
                                    Marker markersearchFirst = map.addMarker(new MarkerOptions()
                                            .position(new LatLng(gpsTracker.getLocation().getLatitude(), gpsTracker.getLocation().getLongitude()))
                                            .title("My Location")
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_car))
                                    );
                                    MyLocationIcon = markersearchFirst;

                                    updateCameraBearing(map, gpsTracker.getLocation().getBearing());


                                }

                            }
                        });
                    }
                }
            }, 5200, 5100);


        } else {
            T.cancel();

        }
}


    private void updateCameraBearing(GoogleMap map, float bearing) {
//        Toast.makeText(context, "bearing", Toast.LENGTH_SHORT).show();//

        if (map == null) return;
        CameraPosition camPos = CameraPosition
                .builder(
                        map.getCameraPosition()
                        // current Camera
                )
                .bearing(bearing)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));

    }

    private void listner() {


        End_Trip_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                digit = 0;
                if (markersearchCategory != null) {
                    markersearchCategory.hideInfoWindow();
                }
                if (markersearch != null) {
                    markersearch.remove();

                    if (flagMarkerone != null) {
                        flagMarkerone.remove();
                        flagMarkertwo.remove();
                    }

                }


                if (polyline != null) {
                    polyline.remove();
//                      map.clear();
                    if (flagMarkerone != null) {

                        flagMarkerone.remove();
                        flagMarkertwo.remove();
                    }

                }


                fab.setVisibility(View.VISIBLE);
                whare_to.setVisibility(View.VISIBLE);
                goTo_BT.setVisibility(View.GONE);
                End_Trip_BT.setVisibility(View.GONE);


//
                try {


                    sharedPreferences = context.getSharedPreferences(getString(R.string.SharedPrefranceKey), Context.MODE_PRIVATE);
                    DestinationLatt = Double.parseDouble(sharedPreferences.getString(getString(R.string.destination_lat), "No Destinatoin lat Found"));
                    DestinationLongg = Double.parseDouble(sharedPreferences.getString(getString(R.string.destination_longg), "No Destinatoin longg Found"));

//                        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.SharedPrefranceKey), Context.MODE_PRIVATE);
//                        double DestinationLatt = Double.parseDouble(sharedPreferences.getString(getString(R.string.destination_lat), "No Destination Lat Found"));
//                        double DestinationLongg = Double.parseDouble(sharedPreferences.getString(getString(R.string.destination_longg), "No Destinatoin longg Found"));
//                        if(DestinationLatt != 0.0){

                    float[] results = new float[1];
                    Location.distanceBetween(DestinationLatt, DestinationLongg, lat, lng, results);
                    float distanceInMeters = results[0];
                    float Km = distanceInMeters / 1000;
                    Log.d("mytag", "Current Distance Betww" + distanceInMeters);
                    Log.d("mytag", "Current Distance Betww in KM ==>" + Km);
                    if (distanceInMeters <= 5) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("SmartStop");
                        builder.setMessage("You have sucessfully reached your desired waypoint destination");
                        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                if (polyline != null) {
                                    polyline.remove();
                                    digit = 0;

                                    if (MyLocationIcon != null) {
                                        MyLocationIcon.remove();
                                        digit = 0;
                                    }
                                }
                                fab.setVisibility(View.VISIBLE);

                                whare_to.setVisibility(View.VISIBLE);
                                goTo_BT.setVisibility(View.GONE);
                                End_Trip_BT.setVisibility(View.GONE);


                                //TODO TExt To Speech
                                tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int i) {
                                        Log.d("TextTo", "speech====>" + i);
                                        if (i == TextToSpeech.SUCCESS) {
                                            int result = tts.setLanguage(Locale.ENGLISH);
                                            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                                Log.e("TTS", "This Language is not supported");
                                            }


                                            String text = "You have sucessfully reached your desired waypoint destination";
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                                            } else {
                                                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                                            }
                                        }
                                    }
                                });
                                //TODO TExt To Speech
                                if (digit == 1) {

//                                    Toast.makeText(context, "Calll==>1", Toast.LENGTH_SHORT).show();

                                    Fragment fragment = fm.findFragmentByTag("home");
                                    FragmentTransaction transaction = fm.beginTransaction();
                                    transaction.detach(fragment);
                                    map.clear();
                                    transaction.attach(fragment);
                                    transaction.commit();
                                }
                                dialog.dismiss();

                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();


                    } else {


                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("SmartStop");
                        builder.setMessage("This is Your Current Location");
                        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (polyline != null) {
                                    polyline.remove();
//                                        map.clear();
                                    if (flagMarkerone != null) {

                                        flagMarkerone.remove();
                                        flagMarkertwo.remove();
                                    }

                                }

                                fab.setVisibility(View.VISIBLE);

                                whare_to.setVisibility(View.VISIBLE);
                                goTo_BT.setVisibility(View.GONE);
                                End_Trip_BT.setVisibility(View.GONE);


                                //TODO TExt To Speech
                                tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int i) {
                                        Log.d("TextTo", "speech====>" + i);
                                        if (i == TextToSpeech.SUCCESS) {
                                            int result = tts.setLanguage(Locale.ENGLISH);
                                            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                                Log.e("TTS", "This Language is not supported");
                                            }


                                            String text = "This is Your Current Location ";
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                                            } else {
                                                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                                            }
                                        }
                                    }
                                });
                                //TODO TExt To Speech

                                if (digit == 1) {
//                                    Toast.makeText(context, "Calll==>1", Toast.LENGTH_SHORT).show();
                                    Fragment fragment = fm.findFragmentByTag("home");
                                    FragmentTransaction transaction = fm.beginTransaction();
                                    transaction.detach(fragment);
                                    map.clear();
                                    transaction.attach(fragment);
                                    transaction.commit();
                                }
                                dialog.dismiss();

                                //    pushFragment(new SmartStops(context, drawer_layout, rl_header,fm), "home", false);


                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();


                    }
//                    }else {
//
//                            Log.d("mytag","INside OK===>");
//                            // JAHA DESTINATION LAT LONGG NAI MIL REHY H VAHA PER
//                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                            builder.setTitle("SmartStop");
//                            builder.setMessage("This is Your Current Location");
//                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//
//                                    if (polyline != null) {
//                                        polyline.remove();
//                                        digit = 0;
////                                        map.clear();
//                                        if(flagMarkerone!=null){
//
//                                            flagMarkerone.remove();
//                                            flagMarkertwo.remove();
//                                        }
//
//                                    }
//
//                                    fab.setVisibility(View.VISIBLE);
//                                    whare_to.setVisibility(View.VISIBLE);
//                                    goTo_BT.setVisibility(View.GONE);
//                                    End_Trip_BT.setVisibility(View.GONE);
//
//                                    //TODO TExt To Speech
//                                    tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
//                                        @Override
//                                        public void onInit(int i) {
//                                            Log.d("TextTo", "speech====>" + i);
//                                            if (i == TextToSpeech.SUCCESS) {
//                                                int result = tts.setLanguage(Locale.ENGLISH);
//                                                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                                                    Log.e("TTS", "This Language is not supported");
//                                                }
//
//
//                                                String text = "This is Your Current Location";
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
//                                                } else {
//                                                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
//                                                }
//                                            }
//                                        }
//                                    });
//                                    //TODO TExt To Speech
//
//                                    dialog.dismiss();
//
//
//                                 //   pushFragment(new SmartStops(context, drawer_layout, rl_header,fm), "home", false);
//
//
//                                  //  pushFragment(new SmartStops(context, drawer_layout, rl_header,fm), "home", false);
//
//                                    dialog.dismiss();
//
//                                    Fragment fragment = fm.findFragmentByTag("home");
//                                    FragmentTransaction transaction = fm.beginTransaction();
//                                    transaction.detach(fragment);
//                                    transaction.attach(fragment);
//                                    transaction.commit();
//
//
//
//                                }
//                            });
//                            AlertDialog alertDialog = builder.create();
//                            alertDialog.setCanceledOnTouchOutside(false);
//                            alertDialog.show();
//
//
//
//
//                        }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                EndTripsetCameraPosition(lat, lng);
            }
        });


    }


    private void EndTripsetCameraPosition(Double lat, Double lng) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lat, lng), 15));

    }


    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            CheckPermission();
        }

        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


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
        new android.support.v7.app.AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }


    public static void hoempageopen() {
        layoutone.setVisibility(View.GONE);
        layout_search.setVisibility(View.GONE);
//        mapView.setVisibility(View.VISIBLE);
        Map_layout.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);

        goTo_BT.setVisibility(View.GONE);
    }


    private void checkpermission() {

        PackageManager pm = context.getPackageManager();
        int permissionCheck;
        int permissionCheck1;
        int permissionCheck2;
        int permissionCheck3;
        int permissionCheck4;
        permissionCheck = pm.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, context.getPackageName());
        permissionCheck1 = pm.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, context.getPackageName());
        permissionCheck2 = pm.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, context.getPackageName());
        permissionCheck3 = pm.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, context.getPackageName());
        permissionCheck4 = pm.checkPermission(Manifest.permission_group.LOCATION, context.getPackageName());


        if (permissionCheck != PackageManager.PERMISSION_GRANTED && permissionCheck1 != PackageManager.PERMISSION_GRANTED
                && permissionCheck2 != PackageManager.PERMISSION_GRANTED && permissionCheck3 != PackageManager.PERMISSION_GRANTED
                && permissionCheck4 != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(getActivity().getApplicationContext(), "LOCATION 2", Toast.LENGTH_SHORT).show();

        } else {
//            Toast.makeText(getActivity().getApplicationContext(), "DENIED 2", Toast.LENGTH_SHORT).show();
        }


        boolean hasPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        final String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE};
        if (hasPermission) {
//            Toast.makeText(getActivity().getApplicationContext(), "LOCATION 1", Toast.LENGTH_SHORT).show();

            ActivityCompat.requestPermissions((Activity) context.getApplicationContext(), permissions, 0);
        } else {
//            Toast.makeText(getActivity().getApplicationContext(), "DENIED 1", Toast.LENGTH_SHORT).show();

//            AlertDialog.Builder builder = new AlertDialog.Builder(applicationContext);
//            builder.setMessage("Call Permission is Required.");
//            builder.setTitle("Smart Stop");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    ActivityCompat.requestPermissions((Activity) applicationContext.getApplicationContext(), permissions, 0);
//                }
//            });
//
//            builder.show();
        }


    }


    private void MapBoxCallHere() {
        Log.d("mytag", "MapBoxCallHere");


        recyclerViewsearchfilter = (RecyclerView) rootView.findViewById(R.id.searchfileter_recycle_view);
        search_button = (Button) rootView.findViewById(R.id.search_button);
        locationSearch = (EditText) rootView.findViewById(R.id.editText);
        Log.d("mytag", "onMapSearch" + locationSearch.getText().toString());
        recyclerViewsearchfilter.setHasFixedSize(true);
        recyclerViewsearchfilter.setLayoutManager(new LinearLayoutManager(context));

        if (realm.isEmpty()) {
        } else {
            Log.d("REALM", "DATA NOT NULL==else==>" + realm.isEmpty());


            Realm.init(getActivity().getApplicationContext());
            realm_search = Realm.getDefaultInstance(); //creating  database oject
            results_realm_search = realm_search.where(MapModal.class).findAllAsync();
            results_realm_search.load();
            Log.d("mytag", "Result Size when get data from result : results_realm_search=>" + results_realm_search.size());

            for (int i = 0; i < results_realm_search.size(); i++) {
                dataSearchFilter.add(results_realm_search.get(i).getName());
//                    dataSearchFilter.add(datastore.get(i).getWaypoint_id());
                Log.d("tttag", "All_data_ehh:-" + dataSearchFilter);
                Log.d("tttag", "All_data_ehh:-" + datastore);
            }
        }


        Log.d("tttag", "search Per Click Hua == >" + results_realm_search);

//        adapter = new CustomAdapter(results_realm_search, realm, lat, lng);

        //TODO String ArrayList object Add  addraylist object for filter


        locationSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int wi, int i1, int i2) {
                Log.d("Adapterq", "beforeTextChanged" + results_realm_search);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int iw, int i1, int i2) {
                Log.d("Adapterq", "onTextChanged" + results_realm_search);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("mytag", "onMapSearch==1=>" + locationSearch.getText().toString());

                search = locationSearch.getText().toString().trim();
                Log.d("Adapterq", "afterTextChanged===>" + results_realm_search + "===>search+++" + search);


                results_realm_search1 = realm.where(MapModal.class).contains("name", search.toLowerCase(Locale.getDefault()), Case.INSENSITIVE)
                        .or().contains("waypoint_id", search.toLowerCase(Locale.getDefault()), Case.INSENSITIVE)
                        .or().contains("description", search.toLowerCase(Locale.getDefault()), Case.INSENSITIVE).findAllAsync();
                results_realm_search1.load();

                for (int i = 0; i < results_realm_search1.size(); i++) {

                    Log.d("myta", "all_results_details====>" + results_realm_search1.get(i).getName());

//
                    Log.d("myta", "Nosuch Result Found==========");

                    String Result_final = results_realm_search1.get(i).getName();
                    Log.d("myta", "NOSUCH RESULT FOUND++++++>" + Result_final);

                    searchwayModels = new ArrayList<>();


                    searchwayModels.add(new SearchwayModel(results_realm_search1.get(i).getLat(), results_realm_search1.get(i).getLng(), results_realm_search1.get(i).getAddress(), results_realm_search1.get(i).getWaypoint_id(), results_realm_search1.get(i).getPulsing(), results_realm_search1.get(i).getCategory_id(), results_realm_search1.get(i).getName(), results_realm_search1.get(i).getState(), results_realm_search1.get(i).getCountry(), results_realm_search1.get(i).getEmail(), results_realm_search1.get(i).getAdditional_info(), results_realm_search1.get(i).getExpire_date(), results_realm_search1.get(i).getStatus(), results_realm_search1.get(i).getNever_expire(), results_realm_search1.get(i).getCreated_at(), results_realm_search1.get(i).getUpdated_at(), results_realm_search1.get(i).getStatus_value()));
                    Log.d("myta", "NOSUCH RESULT FOUND+++SIZE+++>" + searchwayModels.size());


                    adapter = new CustomAdapter(results_realm_search1, realm, lat, lng);
//                        adapter = new CustomAdapter(searchwayModels, realm, lat, lng);
                    recyclerViewsearchfilter.setAdapter(adapter);
                    recyclerViewsearchfilter.setVisibility(View.VISIBLE);

//                }


                    if (search == results_realm_search1.get(i).getName()) {
                        Log.d("myta", "Nosuch Result Found");

                    } else {
                        Log.d("myta", "Nosuch Result Found +++++++++++++++++");
                    }

                    if (search.isEmpty()) {
                        recyclerViewsearchfilter.setVisibility(View.GONE);
                    }

//

                }


                //TODO REALM OLD CODE IS HEERRERR


            }


        });


        recyclerViewsearchfilter.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                // TODO Handle item click
                Log.d("clickhua", String.valueOf(position));
                Map_layout.setVisibility(View.VISIBLE);
                layoutone.setVisibility(View.GONE);
                layout_search.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);


                whare_to.setVisibility(View.GONE);
                goTo_BT.setVisibility(View.VISIBLE);

                try {
                    InputMethodManager inputManager = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);

                    // check if no view has focus:
                    View currentFocusedView = getActivity().getCurrentFocus();
                    if (currentFocusedView != null) {
                        inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {

                    final LatLng latLngdatabase = new LatLng(results_realm_search1.get(position).getLat(), results_realm_search1.get(position).getLng());
//                    final LatLng latLngdatabase = new LatLng(searchwayModels.get(position).getLat(), searchwayModels.get(position).getLng());
                    Log.d("mytag", "serverrr==>" + latLngdatabase);

                    String Destination_lat = String.valueOf(latLngdatabase.latitude);
                    String Destination_longg = String.valueOf(latLngdatabase.longitude);
                    SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.SharedPrefranceKey), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.destination_lat), Destination_lat);
                    editor.putString(getString(R.string.destination_longg), Destination_longg);
                    editor.commit();

                    if (markersearch != null) {
                        Log.d("Mytag", "Remove Marker");
                        markersearch.remove();
                    }


                    float[] resultsdb = new float[1];
                    Location.distanceBetween(results_realm_search1.get(position).getLat(), results_realm_search1.get(position).getLng(), lat, lng, resultsdb);

                    float distanceInMeters = resultsdb[0];

                    float KmDB = distanceInMeters / 1000;
                    Log.d("mytag", "Database Distance" + KmDB);
                    if (KmDB <= 5) {

                        LatLng innerlatlong = new LatLng(results_realm_search1.get(position).getLat(), results_realm_search1.get(position).getLng());
                        Log.d("mytag", "serverrr=inner=>" + innerlatlong);


                        if (results_realm_search1.get(position).getWaypoint_id().equals("N/A")) {
                            markersearch = map.addMarker(new MarkerOptions()
                                    .position(latLngdatabase)
                                    .title(results_realm_search1.get(position).getName())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.search_marker_dots))
                                    .snippet("SmartStop")
                            );
                        } else {
                            markersearch = map.addMarker(new MarkerOptions()
                                    .position(latLngdatabase)
                                    .title(results_realm_search1.get(position).getName())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.search_marker_dots))
                                    .snippet(results_realm_search1.get(position).getWaypoint_id())
                            );
                        }
                        markersearch.showInfoWindow();


                    } else {
                        int height = 95;
                        int width = 90;
                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.mylocation_pin_resize_one);
                        Bitmap b = bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                        if (results_realm_search1.get(position).getWaypoint_id().equals("N/A")) {

                            markersearch = map.addMarker(new MarkerOptions()
                                    .position(latLngdatabase)
                                    .title(results_realm_search1.get(position).getName())
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                    .snippet("SmartStop")
                            );

                        } else {
                            markersearch = map.addMarker(new MarkerOptions()
                                    .position(latLngdatabase)
                                    .title(results_realm_search1.get(position).getName())
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                    .snippet(results_realm_search1.get(position).getWaypoint_id())
                            );
                        }
//                        markersearch.showInfoWindow();
                        //TODO ADD MARKER ADAPTER

                        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                            // Use default InfoWindow frame
                            @Override
                            public View getInfoWindow(Marker arg0) {
                                Log.d("InfoAdapter", "Adapter===>" + arg0.getTitle());
                                Log.d("InfoAdapter", "Adapter===>" + arg0.getSnippet());
                                Log.d("InfoAdapter", "Adapter===>" + arg0.getPosition());
                                Log.d("InfoAdapter", "Adapter===>" + arg0);
                                // Getting view from the layout file info_window_layout
                                View v = getLayoutInflater().inflate(R.layout.map_marker_info_window, null);
                                // Getting the position from the marker

                                // Getting reference to the TextView to set latitude
                                TextView headingone = v.findViewById(R.id.tv_lat_INFO);
                                TextView headingtwo = v.findViewById(R.id.tv_lng_INFO);
                                headingtwo.setSelected(true);

                                headingone.setText(arg0.getTitle());
                                headingtwo.setText(arg0.getSnippet());


//


                                return v;
                            }


                            @Override
                            public View getInfoContents(Marker arg0) {
                                return null;


                            }
                        });
                        markersearch.showInfoWindow();

                        //TODO ADD MARKER ADAPTER


                    }


                    setCameraPositionInfowindowbackbution(results_realm_search1.get(position).getLat(), results_realm_search1.get(position).getLng());
//                    getDirectionButtion(latLngdatabase);
                    LatLng search_lat_long = new LatLng(lat, lng);


                    String url = getDirectionsUrl(search_lat_long, latLngdatabase);
                    Log.d("mytag", "URL++++>" + url);
                    DownloadTask downloadTask = new DownloadTask();

// Start downloading json data from Google Directions API
                    downloadTask.execute(url);


                    getdirection_BT.setVisibility(View.GONE);


                    //TODO NAVIGATION
                    originCoord_nav = new com.mapbox.mapboxsdk.geometry.LatLng(lat, lng);
                    destinationCoord_nav = new com.mapbox.mapboxsdk.geometry.LatLng(results_realm_search1.get(position).getLat(), results_realm_search1.get(position).getLng());
                    originPosition_nav = Point.fromLngLat(originCoord_nav.getLongitude(), destinationCoord_nav.getLatitude());
                    destinationPosition_nav = Point.fromLngLat(destinationCoord_nav.getLongitude(), destinationCoord_nav.getLatitude());
                    origin_nav = originPosition_nav;
                    destination_nav = destinationPosition_nav;

                    //TODO NAVIGATION


                    goTo_BT.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            recenter_icon.setVisibility(View.VISIBLE);


                            //TODO TExt To Speech
                            tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int i) {

                                    Log.d("TextTo", "speech====>" + i);
                                    if (i == TextToSpeech.SUCCESS) {
                                        int result = tts.setLanguage(Locale.ENGLISH);
                                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                            Log.e("TTS", "This Language is not supported");
                                        }


                                        String text = "Proceed to highlighted route.";
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                                        } else {
                                            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                                        }
                                    }
                                }
                            });
                            //TODO TExt To Speech

                            goTo_BT.setVisibility(View.GONE);
                             NavigationStartMapBox(origin_nav,destination_nav);
                            End_Trip_BT.setVisibility(View.VISIBLE);

//                            if(MyLocationIcon != null){
//                                MyLocationIcon.remove();
//                            }
                            setCameraPositionInfowindowbackbution(lat, lng);
//
//                            LatLng mylocation = new LatLng(lat, lng);
//                            map.addMarker(new MarkerOptions().position(mylocation).title("You Are Here")
//                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_nav))
//                            );


                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.d("mytag", "onMapSearch==2==>" + locationSearch.getText().toString());
                if (locationSearch.getText().toString() != null) {
                    Log.d("mytag", "onMapSearch==3==>" + locationSearch.getText().toString());
                    locationSearch.getText().clear();
                    Log.d("mytag", "onMapSearch==4==>" + locationSearch.getText().toString());
                }

            }
        }));


        back_bution_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    InputMethodManager inputManager = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);

                    // check if no view has focus:
                    View currentFocusedView = getActivity().getCurrentFocus();
                    if (currentFocusedView != null) {
                        inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                mapView.setVisibility(View.VISIBLE);
                Map_layout.setVisibility(View.VISIBLE);
                whare_to.setVisibility(View.VISIBLE);

                layoutone.setVisibility(View.GONE);
                layout_search.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                recenter_icon.setVisibility(View.VISIBLE);


            }
        });

    }

    private void NavigationStartMapBox(Point origin_nav, Point destination_nav) {
        Log.d("mytag","Call--->");
        Log.e("mytag","call_navigation==:>"+origin_nav+"==>"+destination_nav);

       double source_lat = origin_nav.latitude();
       double source_lngg = origin_nav.longitude();

       double destin_lat = destination_nav.latitude();
       double destin_lngg = destination_nav.longitude();
        Intent intent = new Intent(getActivity(), NavigationActivity.class);
        intent.putExtra("s_lat",source_lat);
        intent.putExtra("s_lngg",source_lngg);
        intent.putExtra("d_lat",destin_lat);
        intent.putExtra("d_lngg",destin_lngg);
        getActivity().startActivity(intent);
        new NavigationActivity(origin_nav,destination_nav);
        //TODO FOR NAVIGATION MAP DIRECTION MAPB BOX

//        NavigationViewOptions options = NavigationViewOptions.builder()
//                .origin(origin_nav)
//                .destination(destination_nav)
//                .awsPoolId(awsPoolId)
//                .shouldSimulateRoute(simulateRoute)
//                .build();
//        // Call this method with Context from within an Activity
//        NavigationLauncher.startNavigation(getActivity(), options);


        //TODO FOR NAVIGATION MAP DIRECTION MAPB BOX

    }

    private void getSmartStops(final Double Currentlat, final Double Currentlng) {
        Log.d("mytag", "LAT___LONG" + Currentlat + "===>" + Currentlng);
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
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
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("lat", Currentlat);
                    jsonObject.put("longg", Currentlng);

                    Utils.getInstance().d("json object" + jsonObject.toString());
                    response = WebInterface.getInstance().doGet(Const.ALL_WAYPOINTS);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Utils.getInstance().d(" smart stop Final Response : " + response);
                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                mProgressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("mytag", "DATA==>" + jsonObject.length());
                    int status = jsonObject.getInt("status");
                    Log.d("mymy", "sss" + status);
                    JSONArray jsonArraycategory = jsonObject.getJSONArray("category");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject smart_stop_data = jsonArray.getJSONObject(i);

                        int id = smart_stop_data.getInt("id");
                        Log.d("mytag", "ID=>>" + id);
                        String waypoint_id = smart_stop_data.getString("waypoint_id");
                        Log.d("mytag", "waypoint_id_j" + waypoint_id);

                        String name = smart_stop_data.getString("name");
                        Log.d("mytag", "name");

                        String address = smart_stop_data.getString("address");
                        String lat = smart_stop_data.getString("lat");
                        Log.d("mytag", "LAt=>>" + lat);
                        String longg = smart_stop_data.getString("longg");
                        Log.d("mytag", "longg" + longg);
                        int category_id = smart_stop_data.getInt("category_id");

                        String state = smart_stop_data.getString("state");
                        String country = smart_stop_data.getString("country");
                        String email = smart_stop_data.getString("email");
                        String pulsing = smart_stop_data.getString("pulsing");
                        String additional_info = smart_stop_data.getString("additional_info");
                        String expire_date = smart_stop_data.getString("expire_date");
                        int never_expire = smart_stop_data.getInt("never_expire");
                        String created_at = smart_stop_data.getString("created_at");
                        String updated_at = smart_stop_data.getString("updated_at");
                        int status_value = smart_stop_data.getInt("status");
                        Log.d("mytag", "pint_images" + status);
                        String description = smart_stop_data.getString("description");
                        Log.d("mytag", "description==>" + description);

//                        OnlineMarkerserver(Double.parseDouble(lat), Double.parseDouble(longg), pulsing, id, name);


                        datastore.add(new MapModal(Double.parseDouble(lat), Double.parseDouble(longg), address, id,
                                waypoint_id, pulsing, category_id, name, state, country, email,
                                additional_info, expire_date, status, never_expire, created_at, updated_at, status_value, description));
                        //TODO STORE IMAGE IN GALLERY
//                        new DownloadImage().execute(pin_image);
                        //TODO STORE IMAGE IN GALLERY

                        Log.d("mytag", "SERVER LAT LONG" + datastore.size());
                        Log.d("mytag", "serverdata=lat=>" + Double.parseDouble(lat));
                        Log.d("mytag", "serverdata=long=>" + Double.parseDouble(longg));
                        float[] results = new float[1];
                        Location.distanceBetween(Double.parseDouble(lat), Double.parseDouble(longg), Currentlat, Currentlng, results);
                        float distanceInMeters = results[0];
                        float Km = distanceInMeters / 1000;
                        Log.d("mytag", "Current Distance Betww" + distanceInMeters);
                        Log.d("mytag", "Current Distance Betww in KM ==>" + Km);
                        if (Km <= 5) {

                            Log.d("mytag", "Current DisTance is Betwwen 10" + Km);

//                            OnlineMarkerserver(Double.parseDouble(lat), Double.parseDouble(longg), pulsing, id, name);
                        }
                        Log.d("RealM++", "CalllllYHA TAK");
                    }


                    for (int i = 0; i < jsonArraycategory.length(); i++) {

                        JSONObject smart_stop_datacategory = jsonArraycategory.getJSONObject(i);

                        int category_id_pin = smart_stop_datacategory.getInt("category_id");
                        Log.d("mytag", "category_id_pin===>+" + category_id_pin);
                        String categoty_name_pin = smart_stop_datacategory.getString("category_name");
                        Log.d("mytag", "categoty_name_pin===>+" + categoty_name_pin);
                        String categoty_image_pin = smart_stop_datacategory.getString("image");
                        Log.d("mytag", "categoty_image_pin===>+" + categoty_image_pin);

//                        image_model.add(new ImageModel())
                        image_model.add(new ImageModel(category_id_pin, categoty_name_pin, categoty_image_pin));


                    }
                    String msg = jsonObject.getString("msg");
                    Log.d("mymy", "sss===>" + msg);


                    if (msg.equals("success")) {
                        insertRealM(Currentlat, Currentlng);
                    }

                    mProgressDialog.dismiss();

                    Log.d("RealM++", "kal yaha bhi hua");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("mytag", "CALL YEWALA---");

            }


        }.execute();

        Log.d("mytag", "CALL YEWALA---");
    }

    private void OnlineMarkerserver(double v, double v1, String pulsing, int id, String name) {


        final LatLng latLngdatabase = new LatLng(v, v1);
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLngdatabase);
        markerOptions.title(name);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation_pin));
        map.addMarker(markerOptions);


    }


    private void setMarkerBounce(final Marker marker) {
        final Handler handler = new Handler();
        final long startTime = SystemClock.uptimeMillis();
        final long duration = 1000;
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - startTime;
                float t = Math.max(1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                marker.setAnchor(0.2f, 0.1f + t);

                if (t > 0.0) {
                    handler.postDelayed(this, 50);
                } else {
                    setMarkerBounce(marker);
                }
            }
        });
    }

    //TODO Calling All Other OF MapBox Here.............................................................................................................................
    private void realmResultData(double latitude, double longitude) {

        Log.d("mytag", "CurrentValues==>" + latitude);
        Realm.init(getActivity().getApplicationContext());
        final Realm realm1 = Realm.getDefaultInstance(); //creating  database oject
        final RealmResults<MapModal> results = realm1.where(MapModal.class).findAllAsync();
        final RealmResults<ImageModel> imageResult = realm1.where(ImageModel.class).findAll();
        final RealmResults<OfflineImagePathModel> OfflienIMages = realm1.where(OfflineImagePathModel.class).findAll();
        results.load();

        //TODO CHACKING A DATA in result

        Log.d("mytag", "Result Size when get data from result : " + results.size());
        Log.d("mytag", "Result Size when get data from result222 : " + imageResult.size());
        Log.d("mytag", "Result Size when get data from result23333 : " + OfflienIMages.size());
        Log.d("mytag", "Result Size when get data from result23111 : " + OfflienPath.size());
//TODO SAVE IMAGE AND INSERT INTO DB
//
//        if(OfflienIMages.size() == 0) {
//
//            for (int i = 0; i < imageResult.size(); i++) {
//
//                new DownloadPinUrlImage(imageResult.get(i).getCategoty_image_pin(), context, imageResult.get(i).getCategoty_name_pin(), imageResult.get(i).getCategory_id_pin(), realm).execute(imageResult.get(i).getCategoty_image_pin());
//                Log.d("myImage", "Im====>" + imageResult.get(i).getCategoty_image_pin());
//            }
//
//        }


        //TODO SAVE IMAGE AND INSERT INTO DB

        editor = pref.edit();
        editor.putInt(getString(R.string.Key_waypoints), 1);
        // Storing long
        editor.commit();
        int Value = pref.getInt(getString(R.string.Key_waypoints), 0);

        Log.d("Category_pref", "Smart====>" + Value);


        for (int i = 0; i < results.size(); i++) {
            Log.d("mytag", "Result Size lat ==>" + results.get(i).getLat());
            Log.d("mytag", "Result Size long==> " + results.get(i).getLng());
            Log.d("mytag", "Pulsing====>" + results.get(i).getPulsing());

        }

        for (int i = 0; i < results.size(); i++) {

            String Address = results.get(i).getAddress();
            Log.d("mytag", "ADDRESSSS+++>" + Address + "RESU:T SIZE" + results.size());

            float[] resultsdb = new float[1];
            Location.distanceBetween(results.get(i).getLat(), results.get(i).getLng(), latitude, longitude, resultsdb);

            float distanceInMeters = resultsdb[0];

            float KmDB = distanceInMeters / 1000;
            Log.d("mytag", "Database Distance" + KmDB);
            if (KmDB <= 5) {
                Log.d("mytag", "Database Distance ===TOTAL===>" + KmDB);


                addMarkerserver(results.get(i).getLat(), results.get(i).getLng(), results.get(i).getPulsing(),
                        results.get(i).getId(), results.get(i).getName(), results.get(i).getWaypoint_id(),
                        results.get(i).getCategory_id(), imageResult, OfflienIMages);
                Log.d("mytag", "Database Distance===id=>" + results.get(i).getAddress() + "LAt==>" + results.get(i).getLat() + "Total Size==>" + KmDB + "Size==>" + results.size());


                all_latLong.add(results.get(i).getLat());

                all_pin_point_addes = all_latLong.size();
                Log.d("mySize", "Alll===>" + all_latLong.size());
            }

        }
        realm.close();
    }

    private void addMarkerserver(final double latitutdefromreaml, final double longitudefromrealm, final String pulsing, int id, final String name, final String waypoint_id, final int category_id, final RealmResults<ImageModel> imageResult, RealmResults<OfflineImagePathModel> offlienIMages) {
        Log.d("mytag", "All Data Here==>name==>" + name + "id==>" + id + "pulsing===>" + pulsing + "catory==>" + waypoint_id + "Images==>");

        Log.d("mytag", "server=1=?" + latitutdefromreaml);
        Log.d("mytag", "server=2=>" + longitudefromrealm);
        Log.d("mytag", "pulsing=>" + pulsing);
        Log.d("mytag", "server=3id wala=>" + id);
        Log.d("mytag", "server=43id wala=>" + name);
        Log.d("mytag", "server=33id wala=>" + waypoint_id);


        ///TODO HTILL NETWORK HEWRE

        int Pin_id;
        String Pin_image;
        for (int i = 0; i < imageResult.size(); i++) {

            Pin_id = imageResult.get(i).getCategory_id_pin();
            Pin_image = imageResult.get(i).getCategoty_image_pin();

            Log.d("AddMArker", "Pin_ID+++++++===>" + Pin_id);
            Log.d("AddMArker", "Pin_ID_CAtegorey_id+++++++===>" + category_id);


            if (category_id == Pin_id) {
                Log.d("AddMArker", "Pin_ID++++afterMAtch+++===>" + Pin_id);
                Log.d("AddMArker", "Pin_ID_CAtegorey_id++++afterMAtch+++===>" + category_id);


                //TODO CONNECTED IMAGE HERE AFTER INSERTED INTO DB
                final LatLng latLngdatabase_online_pins1 = new LatLng(latitutdefromreaml, longitudefromrealm);
                File imgFile = new File(Pin_image);
                Log.d("mytt", "aaa==aa=>" + imgFile);
                Bitmap myBitmapDevice = BitmapFactory.decodeFile(imgFile.getPath());
                Log.d("mytt", "aaa==aa======xxx>" + myBitmapDevice);
                int height = 100;
                int width = 100;
                Bitmap smallMarker = Bitmap.createScaledBitmap(myBitmapDevice, width, height, false);
                if (myBitmapDevice != null) {

                    markerOptions = new MarkerOptions()
                            .position(latLngdatabase_online_pins1)
                            .title(name)
                            .snippet(waypoint_id)
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    marker = map.addMarker(markerOptions);
                    if (pulsing.equals("1")) {
                        setMarkerBounce(marker);
                    }
                }

                //TODO CONNECTED IMAGE HERE AFTER INSERTED INTO DB
                //TODO CONNECTED IMAGE HERE

            }


        }


        //TODO CHECKING INTERNET CONNECTION


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                marker.showInfoWindow();

                String name = marker.getTitle();
                marker.getPosition();
                if (digit == 1) {
//                   Toast.makeText(context,"Inside",Toast.LENGTH_SHORT).show();
                    goTo_BT.setVisibility(View.VISIBLE);
                    whare_to.setVisibility(View.GONE);
                    fab.setVisibility(View.GONE);

                } else {
//                    Toast.makeText(context,"Inside+==else",Toast.LENGTH_SHORT).show();
                    getDirectionButtion(marker.getPosition());
                }
                Log.d("Mytag", "All Awy pint===>" + name);
                return true;
            }
        });

        //TODO When Come From Cat........End Buttion Click
//
        //TODO When Come From Cat........End Buttion Click

        //TODO INFO WINDOW CODE HERE
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                Log.d("InfoAdapter", "Adapter===>" + arg0.getTitle());
                Log.d("InfoAdapter", "Adapter===>" + arg0.getSnippet());
                Log.d("InfoAdapter", "Adapter===>" + arg0.getPosition());
                Log.d("InfoAdapter", "Adapter===>" + arg0);
                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.map_marker_info_window, null);
                // Getting the position from the marker

                // Getting reference to the TextView to set latitude
                TextView headingone = v.findViewById(R.id.tv_lat_INFO);
                TextView headingtwo = v.findViewById(R.id.tv_lng_INFO);

//                if (marker != null) {
                headingone.setText(arg0.getTitle());
                headingtwo.setText(arg0.getSnippet());
//                }

//                arg0.setBackgroundColor(Color.TRANSPARENT);

                return v;
            }

            // Defines the contents of the InfoWindow
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @SuppressLint("ResourceAsColor")
            @Override
            public View getInfoContents(Marker arg0) {
                return null;


            }
        });

        //TODO INFO WINDOW CODE HERE


    }

    private void getDirectionButtion(final LatLng markerlatlong) {


        getdirection_BT.setVisibility(View.VISIBLE);
        button.setVisibility(View.GONE);
        button.setEnabled(true);
        back_buttion_map.setVisibility(View.GONE);
        fab.setVisibility(View.VISIBLE);
        goTo_BT.setVisibility(View.GONE);


        getdirection_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.custom_progress_dialog_one);
                ImageView imageView = (ImageView) dialog.findViewById(R.id.dialog_image);
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim);
                imageView.startAnimation(animation);
                dialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 3000);

                //TODO DIRECTION CODE

                //TODO Network Setting Here....

                try {
                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    final boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnected();
                    if (isConnected) {
                        Log.d("Network", "Connected");
                        Log.d("mytag", "new LAt Long==-=" + lat + "Long==>" + lng + "destinatio9n lat long===>" + markerlatlong);
                        originCoord = new LatLng(lat, lng);


                        destinationCoord = markerlatlong;

                        originPosition = new LatLng(originCoord.latitude, originCoord.longitude);

//                final double googleLatDestination = destinationCoord.latitude;
//                final double googleLongDestination = destinationCoord.longitude;

                        Log.d("MYYP", "originPosition==>(" + originPosition + ")");

//                getRoute(originPosition, destinationCoord);
                        // Getting URL to the Google Directions API
                        // Getting URL to the Google Directions API
                        String url = getDirectionsUrl(originCoord, destinationCoord);
                        Log.d("mytag", "URL++++>" + url);
                        DownloadTask downloadTask = new DownloadTask();

// Start downloading json data from Google Directions API
                        downloadTask.execute(url);


                        getdirection_BT.setVisibility(View.GONE);
                        button.setVisibility(View.VISIBLE);
                        button.setEnabled(true);
                        back_buttion_map.setVisibility(View.VISIBLE);
                        fab.setVisibility(View.GONE);

                        final LatLng origin = originPosition;
                        final LatLng destination = destinationCoord;
                        Log.d("googleby", "Cheching_direction====>" + origin + "=====>" + destination);

                        //TODO DIRECTION CODE
                        GotoDirectionBution(origin, destination);

//
                    } else {
                        //ToDo Network Error
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("SmartStop");
                        builder.setMessage("You are currently navigating in offline mode" +
                                " please download the google map offline mode for better performace.");
                        builder.setCancelable(true);
                        builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("Continue ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                                Log.d("mytag", "new LAt Long==-=" + lat + "Long==>" + lng + "destinatio9n lat long===>" + markerlatlong);
                                double lat_google_offline = markerlatlong.latitude;
                                double lng_google_offline = markerlatlong.longitude;

                                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", lat_google_offline, lng_google_offline, "Where the party is at");
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                intent.setPackage("com.google.android.apps.maps");
                                startActivityForResult(intent, 0);
                                startActivity(intent);


                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                        //ToDo Till HERE
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //TODO NETEWORK CHECKING
                //TODO Network Setting Here....


            }
        });


    }

    private void GotoDirectionBution(final LatLng origin, final LatLng destination) {
        Log.d("googleby", "GotoDirectionBution====>" + origin + "=====>" + destination);

        if (destination != null) {

            Googlelatitude = destination.latitude;
            Googlelong = destination.longitude;
            Log.d("googleby", "GotoDirectionBution==333333==>" + Googlelatitude + "=====>" + Googlelong);


            //TODO NAVIGATION
            originCoord_nav = new com.mapbox.mapboxsdk.geometry.LatLng(lat, lng);
            destinationCoord_nav = new com.mapbox.mapboxsdk.geometry.LatLng(Googlelatitude, Googlelong);
            originPosition_nav = Point.fromLngLat(originCoord_nav.getLongitude(), destinationCoord_nav.getLatitude());
            destinationPosition_nav = Point.fromLngLat(destinationCoord_nav.getLongitude(), destinationCoord_nav.getLatitude());
            origin_nav = originPosition_nav;
            destination_nav = destinationPosition_nav;

            //TODO NAVIGATION

        }

        back_buttion_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("mytag", "BACK BUTTION PEWR CLOFIDF" + polyline);

                if (polyline != null) {
                    polyline.remove();
                    if (flagMarkerone != null) {

                        flagMarkerone.remove();
                        flagMarkertwo.remove();
                    }
                }
                button.setVisibility(View.GONE);
                back_buttion_map.setVisibility(View.GONE);
                getdirection_BT.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                goTo_BT.setVisibility(View.GONE);
                if (marker != null) {
//                    marker.hideInfoWindow();
                    setCameraPositionInfowindowbackbution(origin.latitude, origin.longitude);

                }
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("mytag","Bution Click hua==>");


                //TODO Checking Internet Connection

                try {
                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    final boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnected();
                    if (isConnected) {
                        Log.d("Network", "Connected");
                        try {

                            Log.d("GOOGLE MAp", "DIRECT TO ====>" + Googlelatitude + "===>" + Googlelong);
                            //ToDo Network Error



                            button.setVisibility(View.GONE);
                            back_buttion_map.setVisibility(View.GONE);
                            getdirection_BT.setVisibility(View.GONE);
                            fab.setVisibility(View.GONE);
                            goTo_BT.setVisibility(View.GONE);
                            End_Trip_BT.setVisibility(View.VISIBLE);
                            whare_to.setVisibility(View.GONE);

                            //TODO TExt To Speech
                            tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int i) {
                                    Log.d("TextTo", "speech====>" + i);
                                    if (i == TextToSpeech.SUCCESS) {
                                        int result = tts.setLanguage(Locale.ENGLISH);
                                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                            Log.e("TTS", "This Language is not supported");
                                        }


                                        String text = "Proceed to highlighted route.";
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                                        } else {
                                            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                                        }
                                    }
                                }
                            });
                            //TODO TExt To Speech
                            NavigationStartMapBox(origin_nav,destination_nav);


                            //TODO REFRESH PAGE


                            if (marker != null) {
                                setCameraPositionInfowindowbackbution(origin.latitude, origin.longitude);

                            }
                            //TODO REFRESH PAGE.3


                            //ToDo Till HERE


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        //ToDo Network Error

                        Log.d("GOOGLE MAp", "DIRECT TO ===33=>" + Googlelatitude + "===>" + Googlelong);

                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("SmartStop");
                        builder.setMessage("No internet Connection");
                        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try {


                                    //TODO TExt To Speech
                                    tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                                        @Override
                                        public void onInit(int i) {
                                            Log.d("TextTo", "speech====>" + i);
                                            if (i == TextToSpeech.SUCCESS) {
                                                int result = tts.setLanguage(Locale.ENGLISH);
                                                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                                    Log.e("TTS", "This Language is not supported");
                                                }


                                                String text = "Proceed to highlighted route. ";
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                                                } else {
                                                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                                                }
                                            }
                                        }
                                    });
                                    //TODO TExt To Speech

//                                    //TODO GOOGLE MAP REDIRECT
//                                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", Googlelatitude, Googlelong, "Where the party is at");
//                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                                    intent.setPackage("com.google.android.apps.maps");
//                                    startActivityForResult(intent, 0);
//                                    startActivity(intent);


                                    //TODO REFRESH PAGE

                                    if (polyline != null) {
                                        polyline.remove();
                                        if (flagMarkerone != null) {

                                            flagMarkerone.remove();
                                            flagMarkertwo.remove();
                                        }


                                    }
                                    button.setVisibility(View.GONE);
                                    back_buttion_map.setVisibility(View.GONE);
                                    getdirection_BT.setVisibility(View.GONE);
                                    fab.setVisibility(View.VISIBLE);
                                    goTo_BT.setVisibility(View.GONE);
                                    if (marker != null) {
//                    marker.hideInfoWindow();
                                        setCameraPositionInfowindowbackbution(origin.latitude, origin.longitude);

                                    }
                                    //TODO REFRESH PAGE

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                        //ToDo Till HERE
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
////                        //TODO NETEWORK CHECKING
////
////
////                        //TODO Checking Internet Connection
////
//
            }
        });//TODO BUTTION HEHEHE


    }

    private void setCameraPositionInfowindowbackbution(double latitude, double longitude) {

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(latitude, longitude), 18));

    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.fab:
//                mapView.setVisibility(View.GONE);
                Map_layout.setVisibility(View.GONE);
                whare_to.setVisibility(View.GONE);

                layoutone.setVisibility(View.VISIBLE);
                layout_search.setVisibility(View.VISIBLE);
                fab.setVisibility(View.GONE);
                recenter_icon.setVisibility(View.GONE);

                getdirection_BT.setVisibility(View.GONE);
                break;

            case R.id.whare_to:
                if (digit != 0) {
                    digit = 0;
                }

                if (polyline != null) {
                    polyline.remove();
                    if (flagMarkerone != null) {

                        flagMarkerone.remove();
                        flagMarkertwo.remove();
                    }


                }
                if (MyLocationIcon != null) {
                    MyLocationIcon.remove();

                }

                pushInnerFragment(new Category(context, drawer_layout, rl_header, fm, 1), "category", true);
                drawer_layout.closeDrawer(GravityCompat.START);

                break;

            case R.id.recenter_icon:
                recenter_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (gpsTracker.canGetLocation()) {
                            double culat = gpsTracker.getLatitude();
                            double culongg = gpsTracker.getLongitude();
                            Log.d("locationmily", "gpsLocation===>" + culat + "===>" + culongg);
                            LatLng currentlat = new LatLng(culat, culongg);
                            setCameraPosition(currentlat);


//                                 onMyLocationChange(location);

                        }
                    }
                });

                break;


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

    private void pushFragment(Fragment fragment, String tag, boolean addToBackStack) {
        FragmentManager fm = getChildFragmentManager();
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

    ///TODO CHECKING NETWORK
    private void registerData() {

        try {

            receiverDataChange = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    IntentFilter filterData = new IntentFilter();
                    filterData.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//            registerReceiver(receiverDataChange, filterData);
                    String action = intent.getAction();

                    if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                        int state = telephonyManager.getDataState();
                        switch (state) {

                            case TelephonyManager.DATA_DISCONNECTED: // off
                                Log.d("DavidJ", "DISCONNECTED");
                                break;

                            case TelephonyManager.DATA_CONNECTED: // on
                                Log.d("DavidJ", "CONNECTED");
                                break;
                        }
                    }
                }
            };


        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    ///TODO CHECKING NETWORK
    private void insertRealM(Double currentlat, Double currentlng) {
        Log.d("RealM ", "Call===>" + currentlat);


        //TODO Checking Internet Connection

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            final boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnected();
            if (isConnected) {
                Log.d("Network", "Connected");
                if (realm.isEmpty()) {
                    Log.d("RealM", "IS Empty==" + realm.isEmpty());
                } else {
                    Log.d("Network", "Connected====Delet ho gaya");

                    realm.deleteAll();

                }
            } else {
                //ToDo Network Error
//                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("No internet Connection");
//                builder.setMessage("Please turn on internet connection to continue");
//                builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        dialog.dismiss();
//
//
//                    }
//                });
//                AlertDialog alertDialog = builder.create();
//                alertDialog.setCanceledOnTouchOutside(false);
//                alertDialog.show();
                //ToDo Till HERE
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO NETEWORK CHECKING


        //open the database

        for (int i = 0; i < datastore.size(); i++) {
            final MapModal obj = realm.createObject(MapModal.class);

            obj.setLat(datastore.get(i).getLat());
            obj.setLng(datastore.get(i).getLng());

            obj.setAddress(datastore.get(i).getAddress());
            obj.setId(datastore.get(i).getId());

            obj.setWaypoint_id(datastore.get(i).getWaypoint_id());
            obj.setPulsing(datastore.get(i).getPulsing());

            obj.setCategory_id(datastore.get(i).getCategory_id());
            obj.setName(datastore.get(i).getName());
            Log.d("mytag", "objectcreaterealm=2eeee=>" + datastore.get(i).getName());


            obj.setState(datastore.get(i).getState());
            obj.setCountry(datastore.get(i).getCountry());

            obj.setEmail(datastore.get(i).getEmail());
            obj.setAdditional_info(datastore.get(i).getAdditional_info());

            obj.setExpire_date(datastore.get(i).getExpire_date());
            obj.setStatus(datastore.get(i).getStatus());

            obj.setExpire_date(datastore.get(i).getExpire_date());
            obj.setCreated_at(datastore.get(i).getCreated_at());
            obj.setUpdated_at(datastore.get(i).getUpdated_at());
            obj.setStatus_value(datastore.get(i).getStatus_value());

            obj.setDescription(datastore.get(i).getDescription());
            Log.d("mytag", "objectcreaterealm=2=>" + datastore.get(i).getStatus_value());

            Log.d("mytag", "objectcreaterealm=2=>" + image_model.size());

            Log.d("mytag", "objectcreaterealm====5=>" + obj);


        }


        addAllImageOffline(currentlat, currentlng);


//        for (int i = 0; i <image_model.size() ; i++) {
//            ImageModel imageObj= realm.createObject(ImageModel.class);
//            imageObj.setCategory_id_pin(image_model.get(i).getCategory_id_pin());
//
//            Log.d("mytag", "objectcreaterealm=233=>" + image_model.get(i).getCategory_id_pin());
//
//
//            imageObj.setCategoty_name_pin(image_model.get(i).getCategoty_name_pin());
//            Log.d("mytag", "objectcreaterealm=333=>" + image_model.get(i).getCategoty_name_pin());
//
//            imageObj.setCategoty_image_pin(image_model.get(i).getCategoty_image_pin());
//            Log.d("mytag", "objectcreaterealm=444=>" + image_model.get(i).getCategoty_image_pin());
//
//            Log.d("mytag", "objectcreaterealm====55=>" + imageObj);
//
//
//        }


//        datastore.add(obj);

        Log.d("mytag", "objectcreaterealm''Arrattt''==>" + datastore.size() + "\n");
        Log.d("mytag", "objectcreaterealm''Store==>" + datastore + "\n");

//        realm.commitTransaction();
////        realm.close();
//
//        if (realm.isEmpty()) {
//            Log.d("mytag", "REALM  is ther add a value" + realm.isEmpty());
//
//        } else {
//            realmResultData(currentlat, currentlng);
//        }


    }

    private void addAllImageOffline(final Double currentlat, final Double currentlng) {


        count++;


        if (count == image_model.size()) {

            progressDoalog1.dismiss();

            realm.commitTransaction();
//        realm.close();

            if (realm.isEmpty()) {
                Log.d("mytag", "REALM  is ther add a value" + realm.isEmpty());

            } else {
                realmResultData(currentlat, currentlng);
            }

        }


        if (count < image_model.size()) {

            Log.d("Smart_Stop", "Count_size__________" + count);
            Log.d("Smart_Stop", "image_model__________" + image_model.size());


            final String image = image_model.get(count).getCategoty_image_pin();
            final String s = image_model.get(count).getCategoty_name_pin();


            if (progressDoalog1.isShowing()) {

            } else {
                progressDoalog1.show();
            }
            //Todo DownLoad And Set Image Into RealM

            //TODO ADD IMAGE IN DB
//           final File file;
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        URL url = null;
                        url = new URL(image);
                        URLConnection conn = null;
                        conn = url.openConnection();
                        bitmap = BitmapFactory.decodeStream(conn.getInputStream());
                        Log.e("Call_InsertData", "PAth===Image URl=>" + url);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bitmap.setHasAlpha(true);
                                File sdcard = Environment.getExternalStorageDirectory();
                                File folder = new File(sdcard.getAbsoluteFile(), ".smartStop");//the dot makes this directory hidden to the user
                                folder.mkdir();
                                mediaImage = new File(folder.getAbsoluteFile(), s + ".png");
                                Log.d("Call_InsertData", "File PAth=====>" + mediaImage.getPath());

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


                                    ImageModel imageObj = realm.createObject(ImageModel.class);
                                    imageObj.setCategory_id_pin(image_model.get(count).getCategory_id_pin());
//
                                    imageObj.setCategoty_image_pin(mediaImage.getPath());
                                    Log.d("Call_InsertData", "categoryList id==for=>" + imageObj.getCategoty_image_pin());

                                    imageObj.setCategoty_name_pin(image_model.get(count).getCategoty_name_pin());

                                    Log.d("Call_InsertData", "categoryList name==for=>" + imageObj.getCategoty_name_pin());
                                } else {
                                    Log.d("Category", "PAth_is_null");

                                }
                                //TODO SET ADTA INTO REALM

                                addAllImageOffline(currentlat, currentlng);

                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            });
            thread.start();
//            thread.destroy();
            //TODO ADD IMAGE IN DB


            //Todo DownLoad And Set Image Into RealM

        }


    }

    /**
     * A callback method, which is executed when the activity that is called from this activity is finished its execution
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * requestCode : an integer code passed to the called activity set by caller activity
         * resultCode : an integer code returned from the called activity
         * data : an intent containing data set by the called activity
         */
        if (requestCode == REquestConde && resultCode == RESULT_OK) {
//            Toast.makeText(getContext(), data.getStringExtra("data"), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d("onLocationChanged", "changeList===2>" + location.getLongitude());
    }

    @Override
    public void onMyLocationChange(final Location location) {

//        onlocationchange = location;
        Log.d("onMyLocationChange", "changeList===1>" + location);

        if (AllPolylinelatlongg != null) {

            for (int i = 0; i < AllPolylinelatlongg.size(); i++) {
                Routeslat = AllPolylinelatlongg.get(i).latitude;
                Routeslongg = AllPolylinelatlongg.get(i).longitude;
                RoutesLAtLongg = new LatLng(Routeslat, Routeslongg);
                Log.d("AllLATLONGG", "onMyLocationCh==>" + Routeslat);
            }
            float[] results = new float[1];
            Location.distanceBetween(RoutesLAtLongg.latitude, RoutesLAtLongg.longitude, location.getLatitude(), location.getLongitude(), results);
            float distanceInMeters = results[0];
            float Km = distanceInMeters / 1000;
            Log.d("mytag1", "Current Distance Betww" + distanceInMeters);
            Log.d("mytag", "Current Distance Betww in KM ==>" + Km);
            if (distanceInMeters <= 30.01) {
                Log.d("AllLATLONGG", "onMyLocation===>inside===>" + location.getLongitude() + "======>" + Routeslongg);
                if (MyLocationIcon != null) {
                    MyLocationIcon.remove();
                }
                Marker markersearchFirst = map.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .title("My Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_car))
                );
                MyLocationIcon = markersearchFirst;
                //TODO GEOFANCE CIRCLES
                if (circle != null) {
                    if (circle.isVisible()) {
                        circle.remove();
                    }
                }
                CircleOptions circleOptions = new CircleOptions()
                        .center(new LatLng(location.getLatitude(), location.getLongitude()))
                        .radius(5)
                        .strokeColor(Color.TRANSPARENT)
                        .strokeWidth(2)
                        .fillColor(Color.TRANSPARENT)
                        .strokeColor(Color.RED);

// Get back the mutable Circle
                circle = map.addCircle(circleOptions);
                //TODO GEOFANCE CIRCLES


            }
        }
        Log.d("onMyLocationChange", "changeList" + location.getLongitude());


    }

    private void setCameraPosition(LatLng location) {
        Log.d("mytag", "setCameraPosition");

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.latitude, location.longitude), 15));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(100);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    /////////////////////////////////////...........................................................................
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
//
            MarkerOptions markerOptions = new MarkerOptions();

            try {


                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList();
                    lineOptions = new PolylineOptions();

                    List<HashMap<String, String>> path = result.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    lineOptions.addAll(points);

                    //TODO TEST TO REMOVE POLYLINE

                    lineOptions.width(15);
                    lineOptions.color(Color.BLUE);
                    lineOptions.geodesic(true);
                }

                if (polyline != null) {
                    polyline.remove();
                    if (flagMarkerone != null) {
                        flagMarkerone.remove();
                        flagMarkertwo.remove();
                    }

                }
                polyline = map.addPolyline(lineOptions);

                LatLng first = lineOptions.getPoints().get(0);
                LatLng last = lineOptions.getPoints().get(lineOptions.getPoints().size() - 1);

                Log.d("AllLATLONGG", "All Waylat=000==>" + polyline.getPoints().size());
                for (int i = 0; i < polyline.getPoints().size(); i++) {

                    double latLng = polyline.getPoints().get(i).latitude;
                    double latLng1 = polyline.getPoints().get(i).longitude;
                    Log.d("AllLATLONGG", "All Waylat===>" + latLng);
                    LatLng latLng3 = new LatLng(latLng, latLng1);

                    AllPolylinelatlongg = new ArrayList<>();
                    AllPolylinelatlongg.add(latLng3);


                }

                if (first != null) {


                    Log.d("Mytag", "Polinine LAst Values-======>" + first + "===>" + last);

                    Marker markersearchFirst = map.addMarker(new MarkerOptions()
                            .position(first)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_start_new))

                    );
                    flagMarkerone = markersearchFirst;

                    Marker markersearchFirst2 = map.addMarker(new MarkerOptions()
                            .position(last)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_end_new))

                    );
                    flagMarkertwo = markersearchFirst2;

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    /////............................................................................................................................................


    /////............................................................................................................................................


    //TODO ONLOCATION CHANGE POSTION DATA

//    currentLatitude = location.getLatitude();
//    currentLOngitude = location.getLongitude();
//
//        if(First_time_load_key == 1){
//        realmResultData(lat,lng);
//    }else {
//
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        final boolean isConnected = activeNetwork != null &&
//                activeNetwork.isConnected();
//        if (isConnected) {
//            Log.d("Network", "Connected");
//            try {
//                getSmartStops(location.getLatitude(), location.getLongitude());
//
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//        if (realm.isEmpty()) {
//        Log.d("mytag", "REALM  is ther add a value" + realm.isEmpty());
//
//    } else {
//        Log.d("mytag", "REALM  is ther add _____---__" + realm.isEmpty());
//    }
//
//
//    currentLatitude = location.getLatitude();
//        Log.d("mytag", "Current LT==Database Distance =33=>=>" + currentLatitude);
//    currentLOngitude = location.getLongitude();
//        Log.d("mytag", "Current LT==Database Distance =33=>=>" + currentLOngitude);
//
//
//        if (location != null) {
//        Log.d("mytag", "onLocationChanged==if");
//
//        originLocation = location;
//        Log.d("mytag", "onLocationChanged==>" + originLocation);
//
//        setCameraPosition(location);
    //TODO ONLOCATION CHANGE POSTION DATA
    //TODO ONLOCATION CHANGE POSTION DATA

    @Override
    public void onResume() {
        super.onResume();
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        Log.d("DATA", "onResume");
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if(locationManager != null){

            locationManager.requestLocationUpdates(provider, 400, 1, (android.location.LocationListener) context.getApplicationContext());
        }
    }
    @Override
    public void onMapReady(final GoogleMap googleMap) {

        map = googleMap;
        map.clear();
//        map.setMinZoomPreference(18.0f);//kam zoom
        map.setMaxZoomPreference(18.0f);//jada
        map.getUiSettings().setRotateGesturesEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setTrafficEnabled(false);
        map.setIndoorEnabled(false);
        map.setBuildingsEnabled(false);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        map.setMyLocationEnabled(false);

//        map.setOnMyLocationChangeListener(this);






        //TODO CAlling Location Change
        apiClient = new GoogleApiClient.Builder(context).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).build();
        apiClient.connect();

        //TODO CAlling Location Change



        if (gpsTracker.canGetLocation()) {

            Log.d("mytag", "in gps enabled");
            lat = gpsTracker.getLatitude();
            lng = gpsTracker.getLongitude();
            LatLng mylocation = new LatLng(lat, lng);
            Marker  markersearchFirst = map.addMarker(new MarkerOptions()
                    .position(mylocation)
                    .title("My Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation_pin_resize_one))
            );
            MyLocationIcon = markersearchFirst;

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(lat, lng), 18));
        } else {
            gpsTracker.showSettingsAlert();
        }
        Log.d("mytag", "lat -" + lat + " lng -" + lng);
        if(digit == 1){
            map.setTrafficEnabled(false);
            map.setMinZoomPreference(12f);
            Log.d("Digit","Now Digit==OnMap=>"+digit);
            Log.d("Digit","Now Digit==OnMap=>"+cat_lat+"======"+cat_longg);
            realmResultData(lat,lng);
            //TODO SHOIW GOTo BUTION
            goTo_BT.setVisibility(View.VISIBLE);
            whare_to.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            //TODO NAVIGATION

            originCoord_nav = new com.mapbox.mapboxsdk.geometry.LatLng(lat, lng);
            destinationCoord_nav = new com.mapbox.mapboxsdk.geometry.LatLng(cat_lat,cat_longg);
            originPosition_nav = Point.fromLngLat(originCoord_nav.getLongitude(), destinationCoord_nav.getLatitude());
            destinationPosition_nav = Point.fromLngLat(destinationCoord_nav.getLongitude(), destinationCoord_nav.getLatitude());
            origin_nav = originPosition_nav;
            destination_nav = destinationPosition_nav;

            //TODO NAVIGATION

            goTo_BT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //TODO TExt To Speech
                    tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int i) {
                            Log.d("TextTo","speech====>"+i);
                            if(i == TextToSpeech.SUCCESS) {
                                int result = tts.setLanguage(Locale.ENGLISH);
                                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                    Log.e("TTS", "This Language is not supported");
                                }
                                String text = "Proceed to highlighted route.";
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                                } else {
                                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                                }
                            }
                        }
                    });
                    //TODO TExt To Speech
                     goTo_BT.setVisibility(View.GONE);
                     NavigationStartMapBox(origin_nav,destination_nav);
                    End_Trip_BT.setVisibility(View.VISIBLE);
                    if(MyLocationIcon != null){
                        MyLocationIcon.remove();

                    }
                    setCameraPositionInfowindowbackbution(lat,lng);

                    LatLng mylocation = new LatLng(lat, lng);

                    Marker  markersearchFirst = map.addMarker(new MarkerOptions()
                            .position(mylocation)
                            .title("My Location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_car))

                    );
                    MyLocationIcon = markersearchFirst;


                }
            });
            LatLng waypointspoint = new LatLng(cat_lat,cat_longg);
            Log.d("Digit","Now Digit==OnMap=>"+waypointspoint);

            String Destination_lat = String.valueOf(cat_lat);
            String Destination_longg = String.valueOf(cat_longg);
            SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.SharedPrefranceKey),Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.destination_lat),Destination_lat);
            editor.putString(getString(R.string.destination_longg),Destination_longg);
            editor.commit();


            LatLng mylocation = new LatLng(lat, lng);


            if(lat != null && cat_lat != 0.0){

                String url = getDirectionsUrl(mylocation, waypointspoint);
                Log.d("mytag", "URL++++>" + url);
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);

            }

            Bitmap myBitmapDevice = BitmapFactory.decodeFile(image);
            Log.d("mytt", "aaa==aa======xxx>" + myBitmapDevice);
            int height = 100;
            int width = 100;
            Bitmap smallMarker = Bitmap.createScaledBitmap(myBitmapDevice, width, height, false);

            markersearchCategory = map.addMarker(new MarkerOptions()
                    .position(waypointspoint)
                    .title(waypoint_name)
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    .snippet(waypoint_id)
            );

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(cat_lat, cat_longg), 15));



            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    marker.showInfoWindow();

                    String name = marker.getTitle();
                    marker.getPosition();
                    if(digit == 1){

                        goTo_BT.setVisibility(View.VISIBLE);
                        whare_to.setVisibility(View.GONE);
                        fab.setVisibility(View.GONE);
                    }else {

                        getDirectionButtion(marker.getPosition());
                    }

                    Log.d("Mytag", "All Awy pint===>" + name);
                    return true;
                }
            });


            map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker arg0) {
                    Log.d("InfoAdapter","arg0===>"+arg0.getTitle());
                    Log.d("InfoAdapter","arg0===>"+arg0.getSnippet());
                    Log.d("InfoAdapter","arg0===>"+arg0.getPosition());
                    Log.d("InfoAdapter","arg0===>"+arg0);
                    // Getting view from the layout file info_window_layout
                    View v = getLayoutInflater().inflate(R.layout.map_marker_info_window, null);
                    // Getting the position from the marker

                    // Getting reference to the TextView to set latitude
                    TextView headingone = v.findViewById(R.id.tv_lat_INFO);
                    TextView headingtwo = v.findViewById(R.id.tv_lng_INFO);

//                if (marker != null) {
                    headingone.setText(arg0.getSnippet());
                    headingtwo.setText(arg0.getTitle());

                    return v;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    return null;
                }
            });

            markersearchCategory.showInfoWindow();



        }else {
            Log.d("Digit", "Now Digit==OnMapReady=>" + digit);


            First_time_load_key =   pref.getInt(getString(R.string.Key_waypoints), 0);
            Log.d("Category_pref","Smart===0=>"+First_time_load_key);
             if(First_time_load_key == 1){
                 realmResultData(lat,lng);
             }else {
                 ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                 NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                 final boolean isConnected = activeNetwork != null &&
                         activeNetwork.isConnected();
                 if (isConnected) {
                     Log.d("Network", "Connected");
                     try {

                         getSmartStops(lat, lng);
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
             }




//                 map.addMarker(new MarkerOptions().position(mylocation).title("You Are Here").icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation_pin_resize_one))
//                 );
//
//                 map.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                         new LatLng(lat, lng), 12));

        }

        //TODO TIMER COunt
        if(T != null) {

//            T.scheduleAtFixedRate(new TimerTask() {
//                @Override
//                public void run() {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            if (lat != null & lng != null) {
//                                map.clear();
//
//                                final Dialog dialog = new Dialog(getContext());
//                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                                dialog.setContentView(R.layout.custom_progress_dialog_one);
//                                ImageView imageView = (ImageView) dialog.findViewById(R.id.dialog_image);
//                                final TextView Way_point_TV = (TextView)dialog.findViewById(R.id.Way_point_TV);
//                                Way_point_TV.setVisibility(View.VISIBLE);
//                                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim);
//                                imageView.startAnimation(animation);
//                                dialog.show();
//                                Handler handler = new Handler();
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Way_point_TV.setVisibility(View.GONE);
//                                        dialog.dismiss();
//                                    }
//                                }, 4000);
//
//                                //TODO DIRECTION CODE
//                                LatLng mylocation = new LatLng(lat, lng);
//                                map.addMarker(new MarkerOptions().position(mylocation).title("You Are Here").icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation_pin_resize_one))
//                                );
//                                realmResultData(lat, lng);
//                                int current_pin_point_data = all_latLong.size();
//
//                                if (current_pin_point_data != all_pin_point_addes) {
//                                    Log.d("mySize", "MyNEw Size=if==>" + all_latLong.size() + "===>" + all_pin_point_addes);
//                                    all_latLong.clear();
//
//                                } else {
//                                    Log.d("mySize", "MyNEw Size=else==>" + all_latLong.size() + "====>" + all_pin_point_addes);
//                                    all_latLong.clear();
//
//                                }
//                            }
//
//
//                        }
//                    });
//                }
//            }, 1100000, 1100000);

        }
 else {
            T.cancel();
        }






        //TODO TIMER COunt



        //TODO Network Setting Here....

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            final boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnected();
            if (isConnected) {
                Log.d("Network", "Connected");
                ///TODO HTILL NETWORK HEWRE
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                    }
                });

//
            } else {
                //ToDo Network Error
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("SmartStop");
                builder.setMessage("No internet Connection");
                builder.setCancelable(true);
                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (realm.isEmpty()) {
                            Log.d("mytag", "REAL IS EMPTY" + realm.isEmpty());

                        } else {

                            realmResultData(lat, lng);
                        }
                        dialog.dismiss();


                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
                //ToDo Till HERE
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO NETEWORK CHECKING
        //TODO Network Setting Here....


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(T != null) {
            T.cancel();
        }
    }

    //TODO Calling All Other OF MapBox Here.............................................................................................................................


}
