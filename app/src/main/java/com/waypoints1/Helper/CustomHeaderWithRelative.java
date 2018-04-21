package com.waypoints1.Helper;

import android.app.Activity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waypoints1.R;


/**
 * Created by jack94 on 05-09-2017.
 */

public class CustomHeaderWithRelative {
    public static void setOuter(final Activity activity, final DrawerLayout drawerLayout, RelativeLayout relativeLayout, String title) {
        relativeLayout.setTag("Outer");

//        final EditText edt_search=(EditText)relativeLayout.findViewById(R.id.edt_search);
//        ImageView iv_search = (ImageView) relativeLayout.findViewById(R.id.iv_search);
//        final ImageView iv_search_cancel = (ImageView) relativeLayout.findViewById(R.id.iv_search_cancel);
//        final RelativeLayout rl_search=(RelativeLayout)relativeLayout.findViewById(R.id.rl_search);
//        iv_search.setVisibility(View.VISIBLE);
        final ImageView iv_menu = (ImageView) relativeLayout.findViewById(R.id.iv_menu);
        final TextView tv_title = (TextView) relativeLayout.findViewById(R.id.tv_title);
        tv_title.setSelected(true);
        tv_title.setText(title);
        iv_menu.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_menu));
//        tv_title.setTypeface(custom_font_regular);
//        iv_menu.setImageDrawable(activity1.getResources().getDrawable(R.drawable.ic_menu));
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

//        iv_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                edt_search.setText("");
//                rl_search.setVisibility(View.VISIBLE);
//                iv_header_logo.setVisibility(View.GONE);
//            }
//        });
//
//        iv_search_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                edt_search.setText("");
//                rl_search.setVisibility(View.GONE);
//                iv_header_logo.setVisibility(View.VISIBLE);
//                try {
//                    InputMethodManager imm = (InputMethodManager) activity1.getSystemService(INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(iv_search_cancel.getWindowToken(), 0);
//                } catch (Exception e) {
//
//                }
//            }
//        });

    }

    public static void setInnerToolbar(final Activity activity, final RelativeLayout relativeLayout, String title) {

        relativeLayout.setTag("Inner");

        ImageView iv_menu = (ImageView) relativeLayout.findViewById(R.id.iv_menu);
        final TextView tv_title = (TextView) relativeLayout.findViewById(R.id.tv_title);
        tv_title.setText(title);
//        iv_search.setVisibility(View.GONE);
        iv_menu.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_back));
        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activity.onBackPressed();

            }
        });
    }
}
