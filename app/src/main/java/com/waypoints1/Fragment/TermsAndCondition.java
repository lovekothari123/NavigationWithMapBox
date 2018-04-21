package com.waypoints1.Fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waypoints1.Helper.CustomHeaderWithRelative;
import com.waypoints1.R;
import com.waypoints1.utility.Const;
import com.waypoints1.utility.Prefs;
import com.waypoints1.utility.Utils;
import com.waypoints1.utility.WebInterface;


import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class TermsAndCondition extends Fragment implements View.OnClickListener {

    private DrawerLayout drawer_layout;
    private RelativeLayout rl_header;
    private Context context;
    private FragmentManager fm;
    private View rootView;
    private ImageView iv_menu;
    private TextView tv_title,tv_terms;
    private NestedScrollView scroll_view;
    private Button btn_terms_agree,btn_warning_agree,btn_call;
    private LinearLayout ll_warning;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private ProgressDialog mProgressDialog, universalProgressLoader;

    public TermsAndCondition(Context context, DrawerLayout drawer_layout, RelativeLayout rl_header, FragmentManager fm) {
        this.drawer_layout = drawer_layout;
        this.rl_header = rl_header;
        this.context = context;
        this.fm = fm;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.activity_terms_and_condition, container, false);
        CustomHeaderWithRelative.setOuter(getActivity(), drawer_layout, rl_header, "WEATHER");
        init();
        listner();
        return rootView;
    }

    private void init()
    {

        iv_menu=rl_header.findViewById(R.id.iv_menu);
        btn_call=rl_header.findViewById(R.id.btn_call);
        tv_title=rl_header.findViewById(R.id.tv_title);
        scroll_view=rootView.findViewById(R.id.scroll_view);
        tv_terms=rootView.findViewById(R.id.tv_terms);
        btn_terms_agree=rootView.findViewById(R.id.btn_terms_agree);
        ll_warning=rootView.findViewById(R.id.ll_warning);
        btn_warning_agree=rootView.findViewById(R.id.btn_warning_agree);
        ll_warning.setVisibility(View.GONE);
        btn_call.setVisibility(View.GONE);


        iv_menu.setVisibility(View.GONE);
        tv_title.setText("TERMS & CONDITIONS");

        btn_warning_agree.setText("I UNDERSTAND & AGREE");

        getTermsAndCondition();
    }

    private void getTermsAndCondition() {
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
                    response = WebInterface.getInstance().doGet(Const.TERMS_AND_CONDITION);
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
                        String terms = jsonObject.getString("data");
                        tv_terms.setText(terms);
                    }
                } catch (Exception e) {

                }
            }
        }.execute();
    }

    private void listner() {

        btn_terms_agree.setOnClickListener(this);
        btn_warning_agree.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_terms_agree:

                tv_title.setText("WARNING!");
                scroll_view.setVisibility(View.GONE);
                ll_warning.setVisibility(View.VISIBLE);

                break;

            case R.id.btn_warning_agree:

                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Prefs.getPrefInstance().setValue(context, Const.WARNING_STATUS,"1");
                        pushFragment(new SmartStops(context, drawer_layout, rl_header, fm), "smart stop", false);
                    }
                },300);

                break;
        }
    }

    private void pushFragment(Fragment fragment, String tag, boolean addToBackStack) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, fragment, tag);
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}
