package com.andremarvell.foodsquare;

import com.andremarvell.foodsquare.classe.Restaurant;
import com.andremarvell.foodsquare.classe.User;
import com.google.android.gms.maps.model.LatLng;

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

    @Override
    public void onCreate() {
        super.onCreate();
    }


}