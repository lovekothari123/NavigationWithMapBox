package com.waypoints1.Fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.waypoints1.Helper.CustomHeaderWithRelative;
import com.waypoints1.R;
import com.waypoints1.utility.Const;
import com.waypoints1.utility.Utils;
import com.waypoints1.utility.WebInterface;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class TellAFriend extends Fragment{

    private DrawerLayout drawer_layout;
    private RelativeLayout rl_header;
    private Context context;
    private FragmentManager fm;
    private View rootView;
    private Button btn_tell_frnd;
    private ProgressDialog mProgressDialog;
    private String android_link,ios_link,text;
    ImageView share_my_app_logo;
    SharedPreferences.Editor editorrating;

    public TellAFriend(Context context, DrawerLayout drawer_layout, RelativeLayout rl_header, FragmentManager fm) {
        this.drawer_layout = drawer_layout;
        this.rl_header = rl_header;
        this.context = context;
        this.fm = fm;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tell_a_friend, container, false);
        CustomHeaderWithRelative.setOuter(getActivity(), drawer_layout, rl_header, "SHARE MY APP");
        init();
        listner();
        getData();
        return rootView;
    }

    private void init()
    {

        share_my_app_logo = (ImageView)rootView.findViewById(R.id.share_my_app_logo);
        Animation animation= AnimationUtils.loadAnimation(getContext(),R.anim.anim);
        share_my_app_logo.startAnimation(animation);

        btn_tell_frnd=rootView.findViewById(R.id.btn_tell_frnd);
//        startWebView("https://www.accuweather.com/en/in/ahmedabad/202438/current-weather/202438");
    }

    private void listner() {

        btn_tell_frnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences  pref = context.getSharedPreferences(getString(R.string.SharedPrefranceKey), Context.MODE_PRIVATE);
                final String share_my_app_android_link = pref.getString(getString(R.string.share_my_app_android_link), null);
                final String share_my_app_ios_link = pref.getString(getString(R.string.share_my_app_ios_link), null);
                final String share_my_app_text = pref.getString(getString(R.string.share_my_app_text), null);



                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = (share_my_app_text + "\n\n" + share_my_app_android_link + "\n\n" + share_my_app_ios_link);
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

    }

    private void getData() {
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
                    response = WebInterface.getInstance().doGet(Const.SHARE);
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
                        JSONObject data = jsonObject.getJSONObject("data");
                        android_link=data.getString("android_link");
                        ios_link=data.getString("ios_link");
                        text=data.getString("text");

                        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.SharedPrefranceKey), Context.MODE_PRIVATE);
                          editorrating = pref.edit();
                        editorrating.putString(context.getString(R.string.share_my_app_android_link), android_link);
                        editorrating.putString(context.getString(R.string.share_my_app_ios_link), ios_link);
                        editorrating.putString(context.getString(R.string.share_my_app_text), text);
                        editorrating.commit();


                    }
                } catch (Exception e) {

                }
            }
        }.execute();
    }

}
