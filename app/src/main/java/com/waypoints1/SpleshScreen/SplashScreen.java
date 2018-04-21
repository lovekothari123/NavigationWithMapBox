package com.waypoints1.SpleshScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.waypoints1.Activities.HomeActivity;
import com.waypoints1.R;


/**
 * Created by Stegowl on 10-02-2018.
 */

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 5000;
    ImageView ping;

    int one = 0;
    int two = 0;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ping = (ImageView) findViewById(R.id.imageping);
        //todo olda cas  anmation her
        Animation animation=AnimationUtils.loadAnimation(getBaseContext(),R.anim.anim);
        ping.startAnimation(animation);


        pref = getApplicationContext().getSharedPreferences(getString(R.string.SharedPrefranceKey), Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.remove(getString(R.string.Key_waypoints));
        editor.remove(getString(R.string.key_cat));
        editor.clear();
        editor.commit();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ping.clearAnimation();
                Intent gowe=new Intent(SplashScreen.this,HomeActivity.class);
                startActivity(gowe);
//                finish();


            }
        },SPLASH_TIME_OUT);
    }


    @Override
    protected void onResume() {
        Log.d("Category_pref","Call OnResume");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        StopServicess();


        Log.d("Category_pref","Call OnDestory");
        editor.remove(getString(R.string.key_cat));
        editor.remove(getString(R.string.Key_waypoints));
        editor.clear();
        editor.commit();
        int values33=  pref.getInt(getString(R.string.key_cat), 0);
        int values133=  pref.getInt(getString(R.string.Key_waypoints), 0);
        Log.d("Category_pref","SpleshScreen===2=OnDestory=>"+values33+"==SmartStopValue=="+values133);
        super.onDestroy();
        finish();
//
    }

    private void StopServicess() {
        stopService(new Intent(getApplicationContext(), SplashScreen.class));


    }

//



}




//


