package com.andremarvell.foodsquare.webservices.restaurant;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.util.Log;


import com.andremarvell.foodsquare.BaseSlidingMenu;
import com.andremarvell.foodsquare.FoodSquareApplication;
import com.andremarvell.foodsquare.R;
import com.andremarvell.foodsquare.fragments.RestaurantFragment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ikounga_marvel on 11/09/2014.
 */
public class PhotoUploader extends AsyncTask<Bitmap, Void, Boolean> {

    private String URL = "restaurants/photos";
    private String TAG = "Webservice Upload photos Restaurant";

    Context context;

    private String restaurantId;



    ProgressDialog progressDialog;

    protected Boolean doInBackground(Bitmap... params) {
        if(params[0]==null)
            return false ;
        else return uploadFile(params[0]);
    }

    public PhotoUploader(Context context, String restaurantId){
        this.context = context;
        this.restaurantId = restaurantId;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            progressDialog = new ProgressDialog(context, R.style.CustomDialog);
            progressDialog.setProgressStyle(android.R.attr.indeterminateProgressStyle);
        }else{
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Chargement en cours...");
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        progressDialog.show();

    }

    protected void onPostExecute(Boolean result) {

        progressDialog.dismiss();
        if(!result){
            if(((BaseSlidingMenu)context).getSupportFragmentManager().findFragmentById(R.id.container) instanceof RestaurantFragment){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Votre demande n'a pas pu être prise en compte !")
                        .setPositiveButton("Ok",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        }else{
            if(((BaseSlidingMenu)context).getSupportFragmentManager().findFragmentById(R.id.container) instanceof RestaurantFragment){
                //((RestaurantFragment)((BaseSlidingMenu)context).getSupportFragmentManager().findFragmentById(R.id.container)).hideDialogRate();
            }
            new RestaurantService(context).execute(restaurantId);
        }

    }

    public boolean uploadFile(Bitmap bitmap ) {



        DefaultHttpClient httpclient = new DefaultHttpClient();

            int statusCode = -1;
            try {
                HttpPost httppost = new HttpPost(FoodSquareApplication.getWebServiceUrl()+URL); // server


                JSONObject jsonParams = new JSONObject();
                jsonParams.put("photo", getStringFromBitmap(bitmap));
                jsonParams.put("restaurant", restaurantId);
                jsonParams.put("token", FoodSquareApplication.USER.getToken());

                StringEntity se = new StringEntity(jsonParams.toString());

                se.setContentType("application/json;charset=UTF-8");
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                httppost.setEntity(se);

                Log.i(TAG, "request " + httppost.getRequestLine());
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();

                statusCode = statusLine.getStatusCode();

                if (statusCode == 200) {
                    Log.i(TAG, "response " + response.getStatusLine().toString());
                    HttpEntity entity = response.getEntity();
                    InputStream inputStream = entity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inputStream));
                    String line = reader.readLine();
                    Log.i(TAG, "response content" + line);
                    inputStream.close();
                }else {
                    Log.d(TAG,statusLine.toString()+" Url : "+FoodSquareApplication.getWebServiceUrl()+URL);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }



            if(statusCode!=200)
                return false;
            else
                return true;
    }

    private String getStringFromBitmap(Bitmap bitmapPicture) {
     /*
     * This functions converts Bitmap picture to a string which can be
     * JSONified.
     * */
        final int COMPRESSION_QUALITY = 75;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);



        if (byteArrayBitmapStream != null) {
            try {
                byteArrayBitmapStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return encodedImage;
    }





}
