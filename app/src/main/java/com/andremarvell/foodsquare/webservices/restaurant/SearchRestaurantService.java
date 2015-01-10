package com.andremarvell.foodsquare.webservices.restaurant;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.andremarvell.foodsquare.BaseSlidingMenu;
import com.andremarvell.foodsquare.FoodSquareApplication;
import com.andremarvell.foodsquare.R;
import com.andremarvell.foodsquare.adapter.AutocompleteRestaurantAdapter;
import com.andremarvell.foodsquare.adapter.GeolocalisedRestaurantAdapter;
import com.andremarvell.foodsquare.classe.Restaurant;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Marvell.
 */
public class SearchRestaurantService extends AsyncTask<String, Void, List> {

    private static  String URL = "restaurants/searches";
    private static  String TAG = "Webservice recherche de Restaurant ";

    Context context;
    AutocompleteRestaurantAdapter adapter;



    ProgressDialog progressDialog;

    protected List doInBackground(String... params) {
        return null;
    }

    public SearchRestaurantService(Context context, AutocompleteRestaurantAdapter adapter){
        this.context = context;
        this.adapter = adapter;
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

    protected void onPostExecute(List result) {

        progressDialog.dismiss();
        List<Restaurant> list = (ArrayList<Restaurant>) result;
        if(list==null || list.size()==0){
            if(((BaseSlidingMenu)context).getSupportFragmentManager().findFragmentById(R.id.container) instanceof RestaurantGeolocalise){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Aucun restaurant trouvé")
                        .setPositiveButton("Ok",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        }
        adapter.addAll(list);

    }

    public static List simpleSearch(String name) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(FoodSquareApplication.getWebServiceUrl()+URL);

        List<Restaurant> restaurants = new ArrayList<Restaurant>();

        try {

            JSONObject jsonParams = new JSONObject();
            jsonParams.put("name", name);
            jsonParams.put("token", FoodSquareApplication.USER.getToken());
            Log.d(TAG," Json : "+jsonParams.toString());
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
                //Log.d(TAG,statusLine.toString()+" Response : "+line);
                JSONArray restos = new JSONArray(line);
                for (int i = 0; i < restos.length(); i++) {
                    JSONObject obj = restos.getJSONObject(i);

                    Restaurant r = new Restaurant(
                            obj.getString("id"),
                            obj.getString("google_id"),
                            obj.getString("adresse"),
                            obj.getString("longitude"),
                            obj.getString("latitude"),
                            obj.getDouble("google_rate"),
                            obj.getString("photo"),
                            obj.getString("nom"),
                            obj.getJSONObject("comments").getInt("num_comment"),
                            obj.getJSONObject("rate").getDouble("average")
                    );
                    restaurants.add(r);
                }

                inputStream.close();
            } else {
                Log.d(TAG,statusLine.toString()+" Url : "+FoodSquareApplication.getWebServiceUrl()+URL);
            }
        } catch (Exception e) {
            String err = (e.getMessage()==null)?"Le chargement des restaurants a échoué":e.getMessage();
            Log.d(TAG,err);
        }

        return restaurants;
    }



}
