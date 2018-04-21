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
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
public class Weather extends Fragment {

    private DrawerLayout drawer_layout;
    private RelativeLayout rl_header;
    private Context context;
    private FragmentManager fm;
    private View rootView;
    private WebView web_view;
    private ProgressDialog mProgressDialog, universalProgressLoader;

    public Weather(Context context, DrawerLayout drawer_layout, RelativeLayout rl_header, FragmentManager fm) {
        this.drawer_layout = drawer_layout;
        this.rl_header = rl_header;
        this.context = context;
        this.fm = fm;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        CustomHeaderWithRelative.setOuter(getActivity(), drawer_layout, rl_header, "WEATHER");
        init();
        listner();
        return rootView;
    }

    private void init() {
        web_view = rootView.findViewById(R.id.web_view);
        getWeather();
//        startWebView("https://www.accuweather.com/en/in/ahmedabad/202438/current-weather/202438");
    }

    private void getWeather() {
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
                    response = WebInterface.getInstance().doGet(Const.WEATHER);
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
                        String url = jsonObject.getString("data");
                        startWebView(url);
                    }
                } catch (Exception e) {

                }
            }
        }.execute();
    }

    private void listner() {

    }

    private void startWebView(String url) {
        //Create new webview Client to show progress dialog
        //When opening a url or click on link
        web_view.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            //Show loader on url load
            /*public void onLoadResource (WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
            }
            public void onPageFinished(WebView view, String url) {
                try{
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }*/

        });
        // Javascript inabled on webview
        web_view.getSettings().setLoadsImagesAutomatically(true);
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        web_view.getSettings().setUseWideViewPort(true);
//        web_view.getSettings().setJavaScriptEnabled(true);
        // Other webview options
        /*
        web_view.getSettings().setLoadWithOverviewMode(true);
        web_view.getSettings().setUseWideViewPort(true);
        web_view.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        web_view.setScrollbarFadingEnabled(false);
        web_view.getSettings().setBuiltInZoomControls(true);
        */

        /*
         String summary = "<html><body>You scored <b>192</b> points.</body></html>";
         web_view.loadData(summary, "text/html", null);
         */
        //Load url in webview
        web_view.loadUrl(url);


    }

}
