package com.waypoints1.Fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import com.waypoints1.Faq.DatumFaques;
import com.waypoints1.Faq.FaqAdpter;
import com.waypoints1.Faq.FaqDetails;
import com.waypoints1.Faq.FaqDetailsModel;
import com.waypoints1.Faq.FaqTitle;
import com.waypoints1.Faq.FaqTitleAdapter;
import com.waypoints1.Faq.Faques;
import com.waypoints1.Helper.CustomHeaderWithRelative;
import com.waypoints1.Model.OfflineFaqDetails;
import com.waypoints1.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class FAQ extends Fragment{

    private DrawerLayout drawer_layout;
    private RelativeLayout rl_header;
    private Context context;
    private FragmentManager fm;
    private View rootView;
    private RecyclerView rv_faq;
    private FaqTitleAdapter mAdapter;
    FaqDetails movie_one;
    FaqTitle molvie_category_one;
    private ProgressDialog mProgressDialog, universalProgressLoader;
    ArrayList<FaqTitle> faqTitles = new ArrayList<>();
    ArrayList<FaqDetails> faqDetails = new ArrayList<>();
    ArrayList<OfflineFaqDetails> OfflineFaqTitles = new ArrayList<>();

    Realm realmFAQ;


//    RecyclerView recyclerView;
    FaqAdpter madapter;
    Retrofit retrofitfaq;
    List<DatumFaques> datalist=new ArrayList<>();

    DatumFaques datumFaques = null;


    public FAQ(Context context, DrawerLayout drawer_layout, RelativeLayout rl_header, FragmentManager fm) {
        this.drawer_layout = drawer_layout;
        this.rl_header = rl_header;
        this.context = context;
        this.fm = fm;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RealmConfiguration  faqConf = new RealmConfiguration.Builder()
                .name("faqConf")
                .build();
        realmFAQ = Realm.getInstance(faqConf);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_faq, container, false);
        CustomHeaderWithRelative.setOuter(getActivity(), drawer_layout, rl_header, "FAQ");
              init();

//TODO Checking Internet Connection


        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            final boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnected();
            if (isConnected) {

                restclient();
                apiclient();


            } else {

                ConnectionCheck();
                Log.d("FAQ","CONNECTIONCHECK_CALL_OFFLINE");


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO NETEWORK CHECKING

        return rootView;
    }

    private void ConnectionCheck() {
        Log.d("FAQ","CONNECTIONCHECK_CALL_OFFLINE_METHOD_CALL");


        //TODO Checking Internet Connection

        final RealmResults<DatumFaques> results = realmFAQ.where(DatumFaques.class).findAll();
        results.load();

        Log.d("FAQ", "OFFLINE_DATUMDAQUES_SIZE" + results.size());

        for (int i = 0; i < results.size(); i++) {

//            datalist.add(new DatumFaques(results.get(i).getId(), results.get(i).getQuestion(), results.get(i).getAnswer()));
            Log.d("FAQ", "OFFLINE_INTO_FOR LOOP=====>" + results.get(i).getQuestion());
            Log.d("FAQ", "OFFLINE_INTO_FOR LOOP=====>" + results.get(i).getId());

        }
        Log.d("FAQ", "Offline_dataList++++++>" + datalist.size());
        if (datalist != null) {
            rv_faq.setLayoutManager(new LinearLayoutManager(context));
//            madapter = new FaqAdpter(datalist, getContext().getApplicationContext());
            madapter = new FaqAdpter(results, getContext().getApplicationContext());
            rv_faq.setAdapter(madapter);
        }

            //TODO NETEWORK CHECKING

    }

    private void init()
    {
        rv_faq=rootView.findViewById(R.id.recyclerviewfaqs);
        rv_faq.setLayoutManager(new LinearLayoutManager(context));
    }


    private void restclient() {


        HttpLoggingInterceptor object=new HttpLoggingInterceptor();
        object.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder okhht=new OkHttpClient.Builder();
        okhht.addInterceptor(object);
        retrofitfaq=new Retrofit.Builder().baseUrl("http://smartstops.ca/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhht.build()).build();

    }
    private void apiclient() {

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Its loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
        Faques fa=retrofitfaq.create(Faques.class);
        Call<FaqDetailsModel> call=fa.faqueslist();
        call.enqueue(new Callback<FaqDetailsModel>() {
            @Override
            public void onResponse(Call<FaqDetailsModel> call, Response<FaqDetailsModel> response) {
                progressDoalog.dismiss();

                datalist=response.body().getData();

               Log.d("FAQ","OnResponse_DataList_size=======>"+ datalist.size());

                for (int i = 0; i <datalist.size() ; i++) {

                    Log.d("FAQ","OnResponse_DataList_size====2===>"+ datalist.size());



//                    rv_faq.setLayoutManager(new LinearLayoutManager(context));
//                    madapter=new FaqAdpter(datalist,getContext().getApplicationContext());
//                    rv_faq.setAdapter(madapter);

                }
                InsertDataIntoRealMData();
            }

            @Override
            public void onFailure(Call<FaqDetailsModel> call, Throwable t) {
                progressDoalog.dismiss();
                t.printStackTrace();
            }
        });


    }

        //TODO Checking Internet Connection
        private void InsertDataIntoRealMData() {



            if (realmFAQ.isInTransaction()) {
                Log.d("DATA", "ONCREAT IF InTranscation");
            } else {
                Log.d("DATA", "ONCREAT IF else");
                realmFAQ.beginTransaction();

            }

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            final boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnected();
            if (isConnected) {
                Log.d("Network", "Connected");
                if (realmFAQ.isEmpty()) {
                    Log.d("RealM", "IS Empty==" + realmFAQ.isEmpty());
                } else {
                    Log.d("Network", "Connected====Delet ho gaya");

                    realmFAQ.deleteAll();

                }
            } else {
                //ToDo Network Error
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("SmartStop");
                builder.setMessage("No internet Connection");
                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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

//            String asd= "Store/0/.smartStop//10abc.png";
//            asd.split("//");
//            asd.split("/",0);


        for (int i = 0; i < datalist.size(); i++) {
            final DatumFaques obj = realmFAQ.createObject(DatumFaques.class);
            obj.setId(datalist.get(i).getId());
            Log.d("FAQ","Id==>"+obj.getId());
            obj.setQuestion(datalist.get(i).getQuestion());
            Log.d("FAQ","Question==>"+obj.getQuestion());
            obj.setAnswer(datalist.get(i).getAnswer());
            Log.d("FAQ","Answere==>"+obj.getAnswer());
        }

         realmFAQ.commitTransaction();
//            realmFAQ.close();

        if(realmFAQ.isEmpty()){

        }else {
            ConnectionCheck();
        }

    }


}