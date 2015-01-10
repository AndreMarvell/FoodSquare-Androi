package com.andremarvell.foodsquare.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.andremarvell.foodsquare.BaseSlidingMenu;
import com.andremarvell.foodsquare.R;
import com.andremarvell.foodsquare.fragments.FavorisFragment;
import com.andremarvell.foodsquare.fragments.ProfilFragment;
import com.andremarvell.foodsquare.fragments.RestaurantGeolocalise;
import com.andremarvell.foodsquare.fragments.UpdateFragment;
import com.andremarvell.foodsquare.webservices.user.Update;

/**
 * Created by ikounga_marvel on 12/09/2014.
 */
public class SlidingMenuAdapter extends BaseAdapter {
    private Context context;
    private final Integer[] iconIds = {
            R.drawable.menu_search,
            R.drawable.menu_profil,
            R.drawable.menu_proxy,
            R.drawable.menu_fav,
            R.drawable.menu_settings,
            R.drawable.menu_logout,
    };
    private final String[] iconTitles = {
        "Rechercher un restaurant","Mon profil","Restaurants à proximité","Restaurants Favoris","Mon Compte","Se déconnecter"
    };

    public SlidingMenuAdapter(Context context) {
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.menu_item, parent,false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);

        } else
            holder = (ViewHolder) convertView.getTag();

        holder.title.setText(iconTitles[position]);
        holder.icon.setImageResource(iconIds[position]);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((BaseSlidingMenu)context).toggleSlidingMenu();

                if(position!=0){
                    FragmentManager fragmentManager = ((BaseSlidingMenu)context).getSupportFragmentManager();
                    SherlockFragment f;
                    if(position==1)
                        f = ProfilFragment.newInstance();
                    else if(position==2)
                        f = RestaurantGeolocalise.newInstance();
                    else if(position==3)
                        f = FavorisFragment.newInstance();
                    else if(position==4)
                        f = UpdateFragment.newInstance();
                    else if(position==5)
                        f = RestaurantGeolocalise.newInstance();
                    else
                        f = null;

                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.container, f)
                            .commit();
                }else{
                    ((BaseSlidingMenu)context).focusSearchview();
                }


            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView title;
        ImageView icon;
    }

    @Override
    public int getCount() {
        return iconIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



}
