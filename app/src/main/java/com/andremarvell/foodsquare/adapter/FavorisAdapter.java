package com.andremarvell.foodsquare.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.andremarvell.foodsquare.BaseSlidingMenu;
import com.andremarvell.foodsquare.R;
import com.andremarvell.foodsquare.classe.Restaurant;
import com.andremarvell.foodsquare.fragments.RestaurantFragment;
import com.andremarvell.foodsquare.webservices.FavorisService;

import java.util.ArrayList;

/**
 * Created by stagiaire on 02/06/14.
 */
public class FavorisAdapter extends ArrayAdapter<Restaurant> {

    Context context;


    public FavorisAdapter(Context context, int resourceId) {

        super(context, resourceId, new ArrayList<Restaurant>());
        this.context = context;
        (new FavorisService(context,this)).execute("get");
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView titre;
        RatingBar note;
        TextView localisation;
        ImageView icon;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        final Restaurant obj = getItem(position);

        obj.setFavoris(true);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.geolocalised_restaurant_item, null);
            holder = new ViewHolder();
            holder.titre = (TextView) convertView.findViewById(R.id.title);
            holder.note = (RatingBar) convertView.findViewById(R.id.ratingBar);
            holder.localisation = (TextView) convertView.findViewById(R.id.localisation);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();


        holder.titre.setText(obj.getNom());
        holder.note.setRating(obj.getNote().floatValue());
        holder.localisation.setText(obj.getAdresse());
        holder.icon.setVisibility(View.VISIBLE);


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

    public void deleteRestaurant(int  position) {
        (new FavorisService(context,this)).execute("delete",Integer.toString(position));


    }


}
