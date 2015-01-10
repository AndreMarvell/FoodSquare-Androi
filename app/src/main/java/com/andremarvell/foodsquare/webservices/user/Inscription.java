package com.andremarvell.foodsquare.webservices.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragment;
import com.andremarvell.foodsquare.R;
import com.andremarvell.foodsquare.FoodSquareApplication;
import com.andremarvell.foodsquare.SplashScreen;
import com.andremarvell.foodsquare.classe.User;
import com.andremarvell.foodsquare.fragments.UpdateFragment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by Marvell.
 */
public class Inscription extends AsyncTask<String, Void, User> {

    private String URL = "users/inscriptions";
    private String TAG = "Webservice Inscription";

    Context context;
    String email;


    ProgressDialog progressDialog;

    protected User doInBackground(String... params) {
        email = params[0];
        return signin(params[0], params[1]);
    }

    public Inscription(Context context){
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

    protected void onPostExecute(User result) {

        progressDialog.dismiss();
        if(result==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Erreur lors de la tentative d'inscription")
                    .setPositiveButton("Ok",null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{

            FoodSquareApplication.USER = result;

            Intent i = new Intent(context, UpdateFragment.class);
            SherlockFragment fragment = new UpdateFragment();

            FragmentManager fragmentManager = ((SplashScreen) context).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .commit();

        }

    }

    public User signin(String login, String pass) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(FoodSquareApplication.getWebServiceUrl()+URL);

        User user = null;

        try {

            JSONObject jsonParams = new JSONObject();
            jsonParams.put("email", login);
            jsonParams.put("password", pass);

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

                user = new User(obj.getJSONObject("me"));
                user.setToken(obj.getString("token"));
                Log.d(TAG,statusLine.toString()+" Response : "+line);



                inputStream.close();
            } else {
                Log.d(TAG,statusLine.toString()+" Url : "+FoodSquareApplication.getWebServiceUrl()+URL);
            }
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }

        return user;
    }



}
