package com.andremarvell.foodsquare.webservices;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;


import com.andremarvell.foodsquare.BaseSlidingMenu;
import com.andremarvell.foodsquare.FoodSquareApplication;
import com.andremarvell.foodsquare.R;
import com.andremarvell.foodsquare.SplashScreen;
import com.andremarvell.foodsquare.adapter.FavorisAdapter;
import com.andremarvell.foodsquare.classe.Restaurant;
import com.andremarvell.foodsquare.webservices.restaurant.RestaurantService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by stagiaire on 11/07/14.
 */
public class FavorisService extends AsyncTask<String, Void, List> {

    Context context;
    FavorisAdapter adapter;
    ProgressDialog progressDialog;
    String action;
    Restaurant restaurant;
    boolean doublon = false;

    protected List doInBackground(String... params) {
        action = params[0];
        if(action.equals("add"))
            return addRestaurant(restaurant);
        else if(action.equals("delete"))
            return deleteRestaurant();
        else
            return getRestaurant();


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(!(context instanceof SplashScreen)){
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


    }

    public FavorisService(Context context, FavorisAdapter adapter){
        this.context = context;
        this.adapter = adapter;
    }

    public FavorisService(Context context, Restaurant s){
        this.context = context;
        this.restaurant = s;
    }

    public FavorisService(Context context){
        this.context = context;
    }

    protected void onPostExecute(List result) {

        if(!(context instanceof SplashScreen))
            progressDialog.dismiss();

        if(action.equals("add")){
            if(doublon){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Ce restaurant est déjà dans vos favoris")
                        .setPositiveButton("Ok",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Le restaurant a été ajoutée aux favoris")
                        .setPositiveButton("Ok",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        }
        else if(action.equals("delete")){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Le restaurant a bien été supprimé de vos favoris")
                    .setPositiveButton("Ok",null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else{
            if(result != null){
                if(context instanceof SplashScreen){
                    FoodSquareApplication.restaurantsFavoris = (List<Restaurant>) result;
                }else{
                    adapter.addAll(result);

                    if(result.size()==0 && !((BaseSlidingMenu)context).isOnline()){
                        String s = "Problème de connexion Internet, veuillez réessayer ultérieurement.";

                        Toast t = Toast.makeText(context.getApplicationContext(), s, Toast.LENGTH_LONG);
                        TextView v = (TextView) t.getView().findViewById(android.R.id.message);
                        if( v != null) v.setGravity(Gravity.CENTER);
                        t.show();
                    }
                }

            }else{
                if(context instanceof SplashScreen){
                    FoodSquareApplication.restaurantsFavoris = (List<Restaurant>) result;
                }
            }
        }



    }

    public List getRestaurant()
    {
        List<Restaurant> restaurants = null ;

        ObjectInputStream in = null;

        int maxTries = 3;
        for (int count = 0; count < maxTries; count++){
            try {


                in = new ObjectInputStream(new FileInputStream(new File(context.getCacheDir(), "cachedRestaurantFile.ser")));
                restaurants = (List<Restaurant>) in.readObject();
                count = maxTries;
                in.close();



            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return restaurants;

    }

    public List addRestaurant(Restaurant s)
    {
        List<Restaurant> restaurants = null ;

        int maxTries = 3;
        for (int count = 0; count < maxTries; count++){
            try {


                restaurants = (List<Restaurant>) getRestaurant();
                if(restaurants!=null && restaurants.size()>0){
                    Iterator it = restaurants.iterator();
                    while(it.hasNext()){
                        Restaurant res = (Restaurant) it.next();
                        if(res.getId().equals(s.getId())){
                            doublon=true;
                            break;
                        }
                    }
                    if(!doublon)
                        restaurants.add(0,s);
                }else{
                    restaurants = new ArrayList<Restaurant>();
                    restaurants.add(0,s);
                }

                ObjectOutput out = new ObjectOutputStream(new FileOutputStream(new File(context.getCacheDir(),"cachedRestaurantFile.ser")));
                out.writeObject( restaurants );
                FoodSquareApplication.restaurantsFavoris = restaurants;
                count = maxTries;
                out.close();




            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;

    }

    public List deleteRestaurant()
    {
        List<Restaurant> restaurants = null ;

        int maxTries = 3;
        for (int count = 0; count < maxTries; count++){
            try {


                restaurants = (List<Restaurant>) getRestaurant();
                Iterator it = restaurants.iterator();
                boolean isRemoved = false;
                while (it.hasNext() && !isRemoved){
                    Restaurant r =(Restaurant) it.next();
                    if(r.getId().equals(restaurant.getId())){
                        restaurants.remove(r);

                    }
                }

                ObjectOutput out = new ObjectOutputStream(new FileOutputStream(new File(context.getCacheDir(),"cachedRestaurantFile.ser")));
                out.writeObject( restaurants );
                FoodSquareApplication.restaurantsFavoris = restaurants;
                count = maxTries;
                out.close();




            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return restaurants;

    }
}
