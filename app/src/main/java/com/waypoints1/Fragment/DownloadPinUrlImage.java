package com.waypoints1.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.waypoints1.Model.CategoryModel;
import com.waypoints1.Model.CategoryModelPath;
import com.waypoints1.NavigationMap.OfflineImagePathModel;
import com.waypoints1.R;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by love on 3/12/2018.
 */

class DownloadPinUrlImage extends AsyncTask<Object, Object, Object> {
     String categoty_image_pin;
    String categoty_image_pin1;
     String imagename_;
    private Bitmap bitmap ;
    private FileOutputStream fos;
    SharedPreferences pref;
     Context context;
     SharedPreferences.Editor editor;
     ArrayList<OfflineImagePathModel> OfflienPath;
     ArrayList<CategoryModelPath> categoryModelsPath;


    Realm  realm;
     int id;

     String tag;


    public DownloadPinUrlImage(String categoty_image_pin, Context context, String filename, int id, Realm realm) {
        Log.d("myImage","DownloadPinUrlImage Const Call");

        this.categoty_image_pin= categoty_image_pin;
        this.categoty_image_pin1= categoty_image_pin;
        Log.d("myImage", "categoty_image_pin==urlLink=>" + categoty_image_pin);
        this.context=context;
        this.imagename_=filename;
        this.realm=realm;
        this.id=id;
         pref = context.getApplicationContext().getSharedPreferences(context.getString(R.string.SharedPrefranceKey), Context.MODE_PRIVATE);
        editor = pref.edit();
    }


    public DownloadPinUrlImage(int id,Context context, String image, Realm realm, String category, String name) {
        Log.d("Categorty","DownloadPinUrlImage==call"+image+"===>carrr===>"+category+"====name==>"+name);

        this.context=context;
        this.categoty_image_pin1 = image;
        Log.d("Categorty","category_images_Fronm_catoigery"+categoty_image_pin1);
        this.realm = realm;
        this.tag = category;
        Log.d("Categorty","Catory_TAG++"+tag);
        this.imagename_=name;
        Log.d("Categorty","imagename_"+imagename_);
        this.id=id;


    }

    @Override
    protected Object doInBackground(Object... objects) {
        try {
            URL url = new URL(categoty_image_pin1);
            URLConnection conn = url.openConnection();
            Log.e("myImage","PAth===Image URl=>"+url);

            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            bitmap.setHasAlpha(true);
        } catch (Exception ex) {
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        if(!checkifImageExists(imagename_))
        {
//            view.setImageBitmap(bitmap);
            saveToSdCard(bitmap, imagename_);

        }

    }

    public  boolean checkifImageExists(String imagename)
    {
        Bitmap b = null ;
        File file = getImage("/"+imagename+".png");
        String path = file.getAbsolutePath();

        if (path != null)
            b = BitmapFactory.decodeFile(path);

        if(b == null ||  b.equals(""))
        {
            return false ;
        }
        return true ;
    }

    public  String saveToSdCard(Bitmap bitmap, String filename) {


        String stored = null;

        File sdcard = Environment.getExternalStorageDirectory() ;

        File folder = new File(sdcard.getAbsoluteFile(), ".smartStop");//the dot makes this directory hidden to the user
        folder.mkdir();

        File file = new File(folder.getAbsoluteFile(), filename + ".png") ;


        if (file.exists())
            return stored ;

        try {
            FileOutputStream out = new FileOutputStream(file);

            bitmap.setHasAlpha(true);


            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

            out.flush();
            out.close();
            stored = "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stored;
    }


    public  File getImage(String imagename) {




        File mediaImage = null;
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root);

            if (!myDir.exists())
                return null;

            mediaImage = new File(myDir.getPath() + "/.smartStop/"+imagename);
//            mediaImage = new File(myDir.getPath() + "/.smartStop/"+"ID="+id+imagename);
            Log.d("mytag","media.getPath===>"+mediaImage.getPath());
            Log.d("myTag","TAG ===Download_pin_____________FIRDT____=>"+tag);

            if(tag == null){
                editor.putString(context.getString(R.string.SharedPrefranceAllPAth), mediaImage.getPath());
                editor.commit();

            }



                if (realm.isInTransaction()) {

                } else {
                    realm.beginTransaction();
                }

                Log.d("myTag","TAG ===Download_pin=>"+tag);


                if (tag==null){

                    Log.d("myTag","TAG null===Download_pin=>"+tag);

                    OfflienPath = new ArrayList<>();
                    Log.d("myImage", "Tag Equal nai h");


                    OfflienPath.add(new OfflineImagePathModel(mediaImage.getPath()));

                    Log.d("myImage", "Image {PAth}}}}}==>" + OfflienPath.size());
                    Log.d("myImage", "Image {PAth}}}}}==>" + mediaImage.getPath());


                    SharedPreferences pref = context.getApplicationContext().getSharedPreferences(context.getString(R.string.SharedPrefranceKey), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(context.getString(R.string.SharedPrefranceAllPAth), mediaImage.getPath());
                    editor.commit();

                    for (int i = 0; i < OfflienPath.size(); i++) {

                        OfflineImagePathModel imageObj = realm.createObject(OfflineImagePathModel.class);

                        imageObj.setOfflineImagePath(OfflienPath.get(i).getOfflineImagePath());

                        Log.d("myImage", "Image PAth===========else==a===>" + imageObj.getOfflineImagePath());
                        Log.d("myImage", "Image PAth===========else==s===>" + OfflienPath.size());
                    }

                }
                      if(tag != null){


                if(tag == "category"){

                    Log.d("myTag","TAG not null ===Download_pin=>"+tag);
                    Log.d("myImage", "Tag is equal");

                    categoryModelsPath = new ArrayList<>();

                    categoryModelsPath.add(new CategoryModelPath(mediaImage.getPath()));

                    for(int i =0 ; i<categoryModelsPath.size();i++){

                        CategoryModel obj  = realm.createObject(CategoryModel.class);
                        obj.setImage(categoryModelsPath.get(i).getCatogeoryPath());

                        Log.d("Categorty", "Downloads==Image PAth==Categorty==>" + categoryModelsPath.get(i).getCatogeoryPath());
                        Log.d("Categorty", "Downloads_Image PAth==size3==>" + categoryModelsPath.size());



                    }


                }
                }




        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }




        return mediaImage;
    }






}
