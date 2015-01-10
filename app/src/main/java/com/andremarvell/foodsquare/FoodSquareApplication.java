package com.andremarvell.foodsquare;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

import com.andremarvell.foodsquare.classe.Restaurant;
import com.andremarvell.foodsquare.classe.User;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by ikounga_marvel on 05/08/2014.
 */
public class FoodSquareApplication extends android.app.Application {

    public static final String IP_MARVELL_HOME = "http://192.168.1.45";
    public static final String IP_MARVELL_UTT = "http://10.25.1.60";
    public static final String HOTE = "http://foodsquare.ovh";
    public static User USER = null;
    public static boolean prod = false;
    public static boolean utt = false;
    public static List<Restaurant> restaurantsFavoris;
    public static String devAssetsImagesUrl = "/FoodSquare/web/bundles/foodsquarecommon/images/restaurants/";
    public static String devWebServiceUrl = "/FoodSquare/web/app_dev.php/api/";
    public static String webServiceUrl = "/api/";
    // Pour la localisation GPS
    public static LatLng currentLocation;

    public FoodSquareApplication() {
        super();
    }

    public static String getWebServiceUrl() {
        if(prod){
            return HOTE+webServiceUrl;
        }else{
            if(utt)
                return IP_MARVELL_UTT+devWebServiceUrl;
            else
                return IP_MARVELL_HOME+devWebServiceUrl;
        }
    }

    public static String getWebServiceImageUrl() {
        if(prod){
            return HOTE+devAssetsImagesUrl;
        }else{
            if(utt)
                return IP_MARVELL_UTT+devAssetsImagesUrl;
            else
                return IP_MARVELL_HOME+devAssetsImagesUrl;
        }
    }

    public static void setCurrentLocation(Double longitude, Double latitude) {
        FoodSquareApplication.currentLocation = new LatLng(longitude,latitude);
    }

    public static void setUSER(User USER, Activity context) {
        FoodSquareApplication.USER = USER;
        saveUser(context);
    }

    public static void saveUser(Activity context){

        SharedPreferences sharedPref = context.getSharedPreferences("UserPersist", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();


        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

        ObjectOutputStream objectOutput;
        try {
            objectOutput = new ObjectOutputStream(arrayOutputStream);
            objectOutput.writeObject(USER);
            byte[] data = arrayOutputStream.toByteArray();
            objectOutput.close();
            arrayOutputStream.close();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Base64OutputStream b64 = new Base64OutputStream(out, Base64.DEFAULT);
            b64.write(data);
            b64.close();
            out.close();

            editor.putString("user", new String(out.toByteArray()));
            editor.commit();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static boolean getUSER(Activity context){

        SharedPreferences sharedPref = context.getSharedPreferences("UserPersist", Context.MODE_PRIVATE);
        User savedUser = null;

        byte[] bytes = sharedPref.getString("user", "{}").getBytes();
        if (bytes.length == 0) {
            savedUser = null;
        }

        ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
        Base64InputStream base64InputStream = new Base64InputStream(byteArray, Base64.DEFAULT);
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(base64InputStream);
            savedUser = (User) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        USER = savedUser;
        if(savedUser==null)
            return false;
        else{
            if(savedUser.sessionExpired()){
                USER = null;
                return false;
            }else
                return true;
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}