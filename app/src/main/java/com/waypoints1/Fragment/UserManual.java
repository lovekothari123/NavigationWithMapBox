package com.waypoints1.Fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import com.waypoints1.Adapter.AdapterImageSlider;
import com.waypoints1.Helper.ClickableViewPager;
import com.waypoints1.Helper.CustomHeaderWithRelative;
import com.waypoints1.Model.More2;
import com.waypoints1.R;
import com.waypoints1.SMART_STOP;
import com.waypoints1.utility.Const;
import com.waypoints1.utility.Utils;
import com.waypoints1.utility.WebInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class UserManual extends Fragment{

    private DrawerLayout drawer_layout;
    private RelativeLayout rl_header;
    private Context context;
    private FragmentManager fm;
    private View rootView;
    private ClickableViewPager clickableViewpager;
    private AdapterImageSlider adapter;
    private ProgressDialog mProgressDialog, universalProgressLoader;
    private ArrayList<More2> imageArraylist = new ArrayList<More2>();
    private static final Object progressTimerSync = new Object();
    private static Timer progressTimer = null;
    private static final Object sync = new Object();
    private CircleIndicator circleIndicator;

    public UserManual(Context context, DrawerLayout drawer_layout, RelativeLayout rl_header, FragmentManager fm) {
        this.drawer_layout = drawer_layout;
        this.rl_header = rl_header;
        this.context = context;
        this.fm = fm;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_user_manual, container, false);
        CustomHeaderWithRelative.setOuter(getActivity(), drawer_layout, rl_header, "USER MANUAL");
        init();
        listner();
        return rootView;
    }

    private void init()
    {
        clickableViewpager = rootView.findViewById(R.id.clickableViewpager);
        circleIndicator = rootView.findViewById(R.id.indicator);
        getUserManualData();
    }

    private void getUserManualData() {
        imageArraylist = new ArrayList<>();
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);
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
                    response = WebInterface.getInstance().doGet(Const.USER_MANUAL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Utils.getInstance().d(" image Final Response : " + response);
                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                mProgressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject slider_image_data = data.getJSONObject(i);
                            String image = slider_image_data.getString("image");
                            imageArraylist.add(new More2(image));
                        }

                        adapter = new AdapterImageSlider(imageArraylist, context);
                        clickableViewpager.setAdapter(adapter);
                        clickableViewpager.setPagingEnabled(true);
                        circleIndicator.setViewPager(clickableViewpager);
                        adapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
                        startImageSliderTimer();

                    }
                } catch (Exception e) {

                }
            }
        }.execute();
    }

    private void startImageSliderTimer() {
        synchronized (progressTimerSync) {
            if (progressTimer != null) {
                try {
                    progressTimer.cancel();
                    progressTimer = null;
                } catch (Exception e) {
                }
            }
            progressTimer = new Timer();
            progressTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    synchronized (sync) {
                        runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                int currentItem = clickableViewpager.getCurrentItem() + 1;
                                int totalItem = adapter.getCount();
//                                Utils.getInstance().d("currentItem"+currentItem);
//                                Utils.getInstance().d("totalItem"+totalItem);
                                if (currentItem == totalItem) {
                                    clickableViewpager.setCurrentItem(0);
                                } else {
                                    clickableViewpager.setCurrentItem(currentItem);
                                }


                            }
                        });
                    }
                }
            }, 0, 3000);
        }

    }

    public static void runOnUIThread(Runnable runnable) {
        runOnUIThread(runnable, 0);
    }

    public static void runOnUIThread(Runnable runnable, long delay) {
        if (delay == 0) {
            SMART_STOP.applicationHandler.post(runnable);
        } else {
            SMART_STOP.applicationHandler.postDelayed(runnable, delay);
        }
    }

    private void listner() {

    }


}
