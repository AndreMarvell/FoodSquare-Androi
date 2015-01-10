package com.andremarvell.foodsquare.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andremarvell.foodsquare.BaseSlidingMenu;
import com.andremarvell.foodsquare.FoodSquareApplication;
import com.andremarvell.foodsquare.R;
import com.andremarvell.foodsquare.classe.Commentaire;
import com.andremarvell.foodsquare.classe.Restaurant;
import com.andremarvell.foodsquare.fragments.RestaurantFragment;
import com.andremarvell.foodsquare.webservices.restaurant.GeolocalisedRestaurant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by stagiaire on 02/06/14.
 */
public class RestaurantCommentAdapter extends ArrayAdapter<Commentaire> {

    Context context;


    public RestaurantCommentAdapter(Context context, int resourceId, List<Commentaire> comments) {

        super(context, resourceId, comments);
        this.context = context;

    }

    /*private view holder class*/
    private class ViewHolder {
        TextView titre;
        TextView comment;
        TextView date;
        ImageView icon;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final Commentaire obj = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.restaurant_comment_item, null);
            holder = new ViewHolder();
            holder.titre = (TextView) convertView.findViewById(R.id.title);
            holder.comment = (TextView) convertView.findViewById(R.id.comment);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();


        holder.titre.setText(obj.getCommenter().getPrenom()+" "+obj.getCommenter().getNom());
        holder.comment.setText(obj.getCommentaire());
        holder.icon.setImageResource(((BaseSlidingMenu)context).getAndroidAvatarDrawable(obj.getCommenter().getPhoto(), context.getPackageName()));
        holder.date.setText(obj.getDate());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*FragmentManager fragmentManager = ((BaseSlidingMenu)context).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, new RestaurantFragment().newInstance(obj))
                        .commit();*/

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





}
