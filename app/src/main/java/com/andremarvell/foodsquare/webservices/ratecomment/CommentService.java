package com.andremarvell.foodsquare.webservices.ratecomment;

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
import com.andremarvell.foodsquare.fragments.RestaurantFragment;
import com.andremarvell.foodsquare.webservices.restaurant.RestaurantService;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;


/**
 * Created by Marvell.
 */
public class CommentService extends AsyncTask<String, Void, Boolean> {

    private String URL = "comments";
    private String TAG = "Webservice Commentaire Restaurant";

    Context context;

    private String restaurantId;



    ProgressDialog progressDialog;

    protected Boolean doInBackground(String... params) {
        return rateRestaurant(params[0]);
    }

    public CommentService(Context context, String restaurantId){
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
                ((RestaurantFragment)((BaseSlidingMenu)context).getSupportFragmentManager().findFragmentById(R.id.container)).hideDialogComment();
            }
            new RestaurantService(context).execute(restaurantId);
        }

    }

    public Boolean rateRestaurant(String comment) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(FoodSquareApplication.getWebServiceUrl()+URL);

        try {

            JSONObject jsonParams = new JSONObject();
            jsonParams.put("comment", comment);
            jsonParams.put("thread", "restaurant"+restaurantId);
            jsonParams.put("commenter", FoodSquareApplication.USER.getId());
            jsonParams.put("token", FoodSquareApplication.USER.getToken());

            StringEntity se = new StringEntity(jsonParams.toString());

            se.setContentType("application/json;charset=UTF-8");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
            post.setEntity(se);

            HttpResponse response = httpClient.execute(post);
            StatusLine statusLine = response.getStatusLine();

            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) {
                return true;

            } else {

                Log.d(TAG,statusLine.toString()+" Url : "+FoodSquareApplication.getWebServiceUrl()+URL);
                return false;
            }
        } catch (Exception e) {
            String err = (e.getMessage()==null)?"Le chargement du restaurant a échoué":e.getMessage();
            Log.d(TAG,err);
        }

        return false;

    }



}
