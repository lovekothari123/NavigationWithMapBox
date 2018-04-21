package com.waypoints1.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationView;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.ui.v5.OnNavigationReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.listeners.NavigationListener;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigationOptions;
import com.mapbox.services.android.navigation.v5.navigation.NavigationConstants;
import com.mapbox.services.android.navigation.v5.navigation.NavigationUnitType;
import com.waypoints1.NavigationMap.ImageModel;
import com.waypoints1.NavigationMap.MapModal;
import com.waypoints1.R;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;

import io.realm.Realm;
import io.realm.RealmResults;

public class NavigationActivity extends AppCompatActivity implements OnNavigationReadyCallback, NavigationListener {

    NavigationView navigationVIEW;
    MapboxNavigation navigation;
    MapboxNavigationOptions navigationOptions;
    private com.mapbox.mapboxsdk.geometry.LatLng secondCoordpoint;
    double firstlat = 23.029930;
    double firstlongg = 72.561743;
 Point origin_nav,destination_nav;
    public String awsPoolId = null;
    public boolean simulateRoute = false;

           Intent intent;

       double s_lat,s_lng,d_lat,d_lng;
    private com.mapbox.mapboxsdk.geometry.LatLng originCoord_nav;
    private com.mapbox.mapboxsdk.geometry.LatLng destinationCoord_nav;
    private Point originPosition_nav;
    private Point destinationPosition_nav;
    IconFactory iconFactory;
     com.mapbox.mapboxsdk.annotations.Icon icon;
Realm realm;
String path;
    public NavigationActivity(){

    }


    public NavigationActivity(Point origin_nav, Point destination_nav) {
     this.origin_nav=origin_nav;
     this.destination_nav=destination_nav;
        Log.d("mytag","NavigationView==>"+origin_nav+"==>"+destination_nav);
        Log.d("mytag","NavigationView==>"+origin_nav+"==>"+destination_nav);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Realm.init(NavigationActivity.this.getApplicationContext());
        realm = Realm.getDefaultInstance();
         iconFactory  = IconFactory.getInstance(NavigationActivity.this);
//        icon = iconFactory.fromResource(R.drawable.user_car);


        intent = getIntent();

        s_lat = intent.getDoubleExtra("s_lat",1.0);
        Log.e("mytag","s_lat"+s_lat);
        s_lng = intent.getDoubleExtra("s_lngg",1.0);
        d_lat = intent.getDoubleExtra("d_lat",1.0);
        d_lng = intent.getDoubleExtra("d_lngg",1.0);
        Log.e("mytag","d_lng"+d_lng);



        //TODO NAVIGATION
        originCoord_nav = new com.mapbox.mapboxsdk.geometry.LatLng(s_lat, s_lng);
        Log.e("mytag","originCoord_nav"+originCoord_nav);

        destinationCoord_nav = new com.mapbox.mapboxsdk.geometry.LatLng(d_lat,d_lng);
        originPosition_nav = Point.fromLngLat(originCoord_nav.getLongitude(), destinationCoord_nav.getLatitude());
        destinationPosition_nav = Point.fromLngLat(destinationCoord_nav.getLongitude(), destinationCoord_nav.getLatitude());
        Log.e("mytag","destinationPosition_nav"+destinationPosition_nav);

        origin_nav = originPosition_nav;

        destination_nav = destinationPosition_nav;

        //TODO NAVIGATION






       Mapbox.getInstance(NavigationActivity.this, getString(R.string.access_token));
            navigationVIEW = (NavigationView) findViewById(R.id.navigationView_1);
            navigationVIEW.onCreate(savedInstanceState);
            initview();

    }

    private void Addmarkers() {

        Log.d("mytag","Call Addmarker Value===>");
        final RealmResults<MapModal> results = realm.where(MapModal.class).findAllAsync();
        final RealmResults<ImageModel> imageResult = realm.where(ImageModel.class).findAll();
         imageResult.load();
        results.load();
        Log.d("mytag","Call Addmarker Value===>"+results.size());
        Log.d("mytag","Call Addmarker Value===>"+results.get(0).getName());


        for (int i = 0; i < results.size(); i++) {

            Log.d("mytag","All Realm Data==>"+results.get(i).getName());

            addmarkeronNavigationmap(results.get(i).getLat(),results.get(i).getLng(),results.get(i).getName(),results.get(i).getWaypoint_id(),results.get(i).getCategory_id(),imageResult);

        }

    }

    private void addmarkeronNavigationmap(double lat, double lng, String name, String waypoint_id, int category_id, RealmResults<ImageModel> imageResult) {

        for (int i = 0; i < imageResult.size(); i++) {

           int Pin_id = imageResult.get(i).getCategory_id_pin();
           String Pin_image = imageResult.get(i).getCategoty_image_pin();

            Log.d("AddMArker", "Pin_ID+++++++===>" + Pin_id);
            Log.d("AddMArker", "Pin_ID_CAtegorey_id+++++++===>" + category_id);


            if (category_id == Pin_id) {

        File imgFile = new File(Pin_image);
        Log.d("mytt", "aaa==aa=>" + imgFile);
        Bitmap myBitmapDevice = BitmapFactory.decodeFile(imgFile.getPath());
        Log.d("mytt", "aaa==aa======xxx>" + myBitmapDevice);
        int height = 100;
        int width = 100;
        Bitmap smallMarker = Bitmap.createScaledBitmap(myBitmapDevice, width, height, false);

        icon = iconFactory.fromBitmap(smallMarker);

        LatLng latLng = new LatLng(lat,lng);
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.title(name);
        options.snippet(waypoint_id);
        options.icon(icon);
        navigationVIEW.getMapboxMap().addMarker(options);

            }

        }
    }

//    @Override
//    protected void onResume() {
//        navigationVIEW.onResume();
//        super.onResume();
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        navigationVIEW.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    private void initview() {
        secondCoordpoint = new LatLng(firstlat,firstlongg);
        navigationVIEW.getNavigationAsync(NavigationActivity.this);
         navigation = new MapboxNavigation(this,getString(R.string.access_token));
    }

    @Override
    public void onNavigationReady() {
//        navigationVIEW.getMapboxMap().setMaxZoomPreference(15f);
//        navigationVIEW.getMapboxMap().setMinZoomPreference(15f);
//        navigationVIEW.getMapboxMap().setZoom(15.0f);
            navigationOptions = MapboxNavigationOptions.builder()
                    .unitType(NavigationUnitType.TYPE_IMPERIAL)
                    .build();
            //TODO FOR NAVIGATION MAP DIRECTION MAPB BOX
            Log.e("mytag", "NavigationActivity-->" + origin_nav);
            NavigationViewOptions options = NavigationViewOptions.builder()
                    .origin(origin_nav)
                    .destination(destination_nav)
                    .awsPoolId(awsPoolId)
                    .shouldSimulateRoute(simulateRoute)
                    .navigationOptions(navigationOptions)
                    .build();
            // Call this method with Context from within an Activity
//        NavigationLauncher.startNavigation(getActivity(), options);
            navigationVIEW.startNavigation(options);
        Addmarkers();

        //TODO FOR NAVIGATION MAP DIRECTION MAPB BOX

//        com.mapbox.mapboxsdk.annotations.MarkerOptions markerOptions = new com.mapbox.mapboxsdk.annotations.MarkerOptions();
//        markerOptions.position(secondCoordpoint);
//        markerOptions.title("Testing");
//        markerOptions.icon(icon);
//        navigationVIEW.getMapboxMap().addMarker(markerOptions);
    }

    @Override
    public void onCancelNavigation() {

    }

    @Override
    public void onNavigationFinished() {
            this.navigationVIEW.removeAllViews();
            this.finish();

    }

    @Override
    public void onNavigationRunning() {

//                navigationVIEW.getMapboxMap().setMinZoomPreference(18f);
        double lat = navigationVIEW.getMapboxMap().getMyLocation().getLatitude();
        double lng = navigationVIEW.getMapboxMap().getMyLocation().getLongitude();

        if (lat != 0.0) {


        float[] results = new float[1];
        Location.distanceBetween(d_lat, d_lng, lat, lng, results);
        float distanceInMeters = results[0];
        float Km = distanceInMeters / 1000;
        Log.d("mytag", "Current Distance Betww" + distanceInMeters);
        Log.d("mytag", "Current Distance Betww in KM ==>" + Km);
        if (distanceInMeters <= 15) {
            Toast.makeText(NavigationActivity.this, "You have sucessfully reached your desired waypoint destination", Toast.LENGTH_SHORT).show();
            finish();
            navigationVIEW.finishNavigationView();
            navigationVIEW.destroyDrawingCache();
            navigationVIEW.onDestroy();
            navigationVIEW.removeAllViews();
        }
    }
    }


    //TODO

    @Override
    public void onBackPressed() {
        navigationVIEW.finishNavigationView();
        navigationVIEW.destroyDrawingCache();
        navigationVIEW.onDestroy();
        navigationVIEW.removeAllViews();

        finish();
        super.onBackPressed();


    }


    //TODO


}
