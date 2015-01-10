package com.andremarvell.foodsquare.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.andremarvell.foodsquare.BaseSlidingMenu;
import com.andremarvell.foodsquare.R;
import com.andremarvell.foodsquare.classe.Restaurant;
import com.andremarvell.foodsquare.fragments.RestaurantFragment;
import com.andremarvell.foodsquare.fragments.RestaurantGeolocalise;
import com.andremarvell.foodsquare.webservices.restaurant.GeolocalisedRestaurant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by stagiaire on 02/06/14.
 */
public class GeolocalisedRestaurantAdapter extends ArrayAdapter<Restaurant> {

    Context context;
    List<Restaurant> items;



    public GeolocalisedRestaurantAdapter(Context context, int resourceId) {

        super(context, resourceId, new ArrayList<Restaurant>());
        this.context = context;
        items = new ArrayList<Restaurant>();

        (new GeolocalisedRestaurant(context,this)).execute();

    }

    /*private view holder class*/
    private class ViewHolder {
        TextView titre;
        RatingBar note;
        TextView localisation;
        TextView distance;
        ImageView icon;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final Restaurant obj = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.geolocalised_restaurant_item, null);
            holder = new ViewHolder();
            holder.titre = (TextView) convertView.findViewById(R.id.title);
            holder.note = (RatingBar) convertView.findViewById(R.id.ratingBar);
            holder.localisation = (TextView) convertView.findViewById(R.id.localisation);
            holder.distance = (TextView) convertView.findViewById(R.id.distance);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();


        holder.titre.setText(obj.getNom());
        holder.note.setRating(obj.getNote().floatValue());
        holder.localisation.setText(obj.getAdresse());
        holder.icon.setVisibility(View.VISIBLE);
        holder.distance.setText(obj.getDistance());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = ((BaseSlidingMenu)context).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, RestaurantFragment.newInstance(obj))
                        .commit();

            }
        });


        return convertView;
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    @Override
    public void addAll(Collection<? extends Restaurant> collection) {

        super.addAll(collection);
        if(collection!=null && collection.size()!=0){
            items = (ArrayList<Restaurant>)collection;
        }



    }

    public List<Restaurant> getItems() {
        return items;
    }

    public void setItems(List<Restaurant> items) {
        this.items = items;
    }



}
