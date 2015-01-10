package com.andremarvell.foodsquare.webservices.restaurant;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.andremarvell.foodsquare.BaseSlidingMenu;
import com.andremarvell.foodsquare.FoodSquareApplication;
import com.andremarvell.foodsquare.R;
import com.andremarvell.foodsquare.adapter.GeolocalisedRestaurantAdapter;
import com.andremarvell.foodsquare.classe.Commentaire;
import com.andremarvell.foodsquare.classe.Photo;
import com.andremarvell.foodsquare.classe.Restaurant;
import com.andremarvell.foodsquare.classe.User;
import com.andremarvell.foodsquare.fragments.RestaurantFragment;
import com.andremarvell.foodsquare.fragments.RestaurantGeolocalise;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Marvell.
 */
public class RestaurantService extends AsyncTask<String, Void, Restaurant> {

    private String URL = "restaurants";
    private String TAG = "Webservice Restaurant";

    Context context;



    ProgressDialog progressDialog;

    protected Restaurant doInBackground(String... params) {
        return getRestaurant(params[0]);
    }

    public RestaurantService(Context context){
        this.context = context;
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

    protected void onPostExecute(Restaurant result) {

        progressDialog.dismiss();
        if(result==null){
            if(((BaseSlidingMenu)context).getSupportFragmentManager().findFragmentById(R.id.container) instanceof RestaurantFragment){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Ce restaurant n'est plus disponible")
                        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((BaseSlidingMenu)context).onBackPressed();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        }else{
            if(((BaseSlidingMenu)context).getSupportFragmentManager().findFragmentById(R.id.container) instanceof RestaurantFragment){
                Log.d(TAG,result.toString());
                ((RestaurantFragment)((BaseSlidingMenu)context).getSupportFragmentManager().findFragmentById(R.id.container)).setRestaurant(result);
            }
        }

    }

    public Restaurant getRestaurant(String id) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(FoodSquareApplication.getWebServiceUrl()+URL);

        Restaurant restaurant = null;

        try {

            JSONObject jsonParams = new JSONObject();
            jsonParams.put("id", id);
            jsonParams.put("token", FoodSquareApplication.USER.getToken());

            StringEntity se = new StringEntity(jsonParams.toString());

            se.setContentType("application/json;charset=UTF-8");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
            post.setEntity(se);

            HttpResponse response = httpClient.execute(post);
            StatusLine statusLine = response.getStatusLine();

            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String line = reader.readLine();
                JSONObject obj = new JSONObject(line);
                restaurant = new Restaurant(
                        obj.getJSONObject("restaurant").getString("id"),
                        obj.getJSONObject("restaurant").getString("google_id"),
                        obj.getJSONObject("restaurant").getString("adresse"),
                        obj.getJSONObject("restaurant").getString("longitude"),
                        obj.getJSONObject("restaurant").getString("latitude"),
                        obj.getJSONObject("restaurant").getDouble("google_rate"),
                        obj.getJSONObject("restaurant").getString("photo"),
                        obj.getJSONObject("restaurant").getString("nom"),
                        obj.getJSONObject("restaurant").getJSONObject("comments").getInt("num_comment"),
                        obj.getJSONObject("restaurant").getJSONObject("rate").getDouble("average")
                );
                restaurant.setUserRater(obj.getBoolean("is_rater"));
                if(restaurant.isUserRater())
                    restaurant.setUserRate(obj.getInt("rate_user"));


                JSONArray comments = obj.getJSONArray("comments");
                for (int i = 0; i < comments.length(); i++) {
                    JSONObject c = comments.getJSONObject(i);
                    restaurant.addComment(new Commentaire(
                            c.getString("id"),
                            c.getString("comment"),
                            "restaurant" + restaurant.getId(),
                            c.getString("date"),
                            new User(c.getJSONObject("commenter"))
                    ));
                }

                JSONArray photos = obj.getJSONObject("restaurant").getJSONArray("gallerie");
                for (int i = 0; i < photos.length(); i++) {
                    JSONObject p = photos.getJSONObject(i);
                    restaurant.addPhoto(new Photo(
                            p.getString("id"),
                            p.getString("url"),
                            p.getString("miniature"),
                            null
                    ));
                }

                inputStream.close();
            } else {
                Log.d(TAG,statusLine.toString()+" Url : "+FoodSquareApplication.getWebServiceUrl()+URL);
            }
        } catch (Exception e) {
            String err = (e.getMessage()==null)?"Le chargement du restaurant a échoué":e.getMessage();
            Log.d(TAG,err);
        }


        return restaurant;
    }



}
