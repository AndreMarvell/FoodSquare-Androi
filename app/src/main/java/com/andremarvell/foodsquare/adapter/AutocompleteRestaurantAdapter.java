package com.andremarvell.foodsquare.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.andremarvell.foodsquare.BaseSlidingMenu;
import com.andremarvell.foodsquare.R;
import com.andremarvell.foodsquare.classe.Restaurant;
import com.andremarvell.foodsquare.fragments.RestaurantFragment;
import com.andremarvell.foodsquare.webservices.restaurant.GeolocalisedRestaurant;
import com.andremarvell.foodsquare.webservices.restaurant.SearchRestaurantService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by stagiaire on 02/06/14.
 */
public class AutocompleteRestaurantAdapter extends ArrayAdapter<Restaurant> implements Filterable{

    Context context;
    List<Restaurant> items;

    String text="";
    ProgressDialog progressDialog;



    public AutocompleteRestaurantAdapter(Context context) {

        super(context, R.layout.geolocalised_restaurant_item, R.id.title);
        this.context = context;
        items = new ArrayList<Restaurant>();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            progressDialog = new ProgressDialog(context, R.style.CustomDialog);
            progressDialog.setProgressStyle(android.R.attr.indeterminateProgressStyle);
        }else{
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Chargement en cours...");
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }


    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    @Override
    public int getCount() {

        return items.size();
    }

    @Override
    public Restaurant getItem(int index) {
        return items.get(index);
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView titre;
        RatingBar note;
        TextView localisation;
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
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        convertView.setBackgroundColor(Color.WHITE);

        if(obj.getId().equals("0")){
            holder.titre.setText(obj.getNom());
            holder.note.setVisibility(View.GONE);
            holder.localisation.setText("");
            holder.icon.setVisibility(View.GONE);
        }else{
            holder.titre.setText(obj.getNom());
            holder.note.setVisibility(View.VISIBLE);
            holder.note.setRating(obj.getNote().floatValue());
            holder.localisation.setText(obj.getAdresse());
            holder.icon.setVisibility(View.VISIBLE);
        }


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

    private Filter filter = new Filter() {
        @Override
        protected Filter.FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            Integer page =0;
            Integer row = 1;

            if (constraint != null) {
                List<Restaurant> new_suggestions = new ArrayList<Restaurant>();
                text = constraint.toString();
                if(isOnline()){
                    new_suggestions = SearchRestaurantService.simpleSearch(text);

                    if(new_suggestions.size()==0){
                        new_suggestions.add(new Restaurant("0","Aucun restaurant trouvé"));
                    }
                }else{
                    new_suggestions.add(new Restaurant("0","Aucune connexion internet"));
                }

                filterResults.values = new_suggestions;
                filterResults.count = new_suggestions.size();

            }
            // do some other stuff

            return filterResults;
        }



        @Override
        protected void publishResults(CharSequence contraint, FilterResults results) {
            if (results != null && results.count != 0) {
                List<Restaurant> lst = (List<Restaurant>)results.values;
                List<Restaurant> itemsList = new ArrayList<Restaurant>(lst);
                items =itemsList;
                notifyDataSetChanged();
            } else {

                notifyDataSetInvalidated();
            }

        }
    };

    public String getText() {
        return text;
    }



}
