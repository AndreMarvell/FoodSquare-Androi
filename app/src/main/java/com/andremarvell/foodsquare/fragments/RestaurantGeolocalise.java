package com.andremarvell.foodsquare.fragments;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.andremarvell.foodsquare.BaseSlidingMenu;
import com.andremarvell.foodsquare.FoodSquareApplication;
import com.andremarvell.foodsquare.R;
import com.andremarvell.foodsquare.adapter.GeolocalisedRestaurantAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.andremarvell.foodsquare.classe.Restaurant;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class RestaurantGeolocalise extends SherlockFragment {

    View _rootView;

    ListView listView;
    GoogleMap map;
    LatLng currentLocation;

    GeolocalisedRestaurantAdapter adapter;

    String[] typeName = {"Liste","Carte"};

    private Map<Marker, Restaurant> allMarkersMap;

    TabHost tabHost;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return Une nouvelle instance fragment de type  RestaurantGeolocalise.
     */
    public static RestaurantGeolocalise newInstance() {
        RestaurantGeolocalise fragment = new RestaurantGeolocalise();

        return fragment;

    }
    public RestaurantGeolocalise() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSherlockActivity().setTitle("À proximité");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(_rootView == null){
            _rootView = inflater.inflate(R.layout.fragment_geolocalised_restaurant, container, false);

            listView = (ListView) _rootView.findViewById(R.id.listView);


           map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment)).getMap();


        /*On configure les tab Liste et Carte*/
            tabHost = (TabHost) _rootView.findViewById(android.R.id.tabhost);
            tabHost.setup();

            final TabWidget tabWidget = tabHost.getTabWidget();
            final FrameLayout tabContent = tabHost.getTabContentView();
            tabHost.getTabWidget().setDividerDrawable(R.drawable.divider);

            // Get the original tab textviews and remove them from the viewgroup.
            TextView[] originalTextViews = new TextView[tabWidget.getTabCount()];
            for (int index = 0; index < tabWidget.getTabCount(); index++) {
                originalTextViews[index] = (TextView) tabWidget.getChildTabViewAt(index);

            }
            tabWidget.removeAllViews();

            // Ensure that all tab content childs are not visible at startup.
            for (int index = 0; index < tabContent.getChildCount(); index++) {
                tabContent.getChildAt(index).setVisibility(View.GONE);
            }

            // Create the tabspec based on the textview childs in the xml file.
            // Or create simple tabspec instances in any other way...
            for (int index = 0; index < originalTextViews.length; index++) {
                final TextView tabWidgetTextView = originalTextViews[index];
                final View tabContentView = tabContent.getChildAt(index);
                TabHost.TabSpec tabSpec = tabHost.newTabSpec((String) tabWidgetTextView.getTag());
                tabSpec.setContent(new TabHost.TabContentFactory() {
                    @Override
                    public View createTabContent(String tag) {
                        return tabContentView;
                    }
                });
                if (tabWidgetTextView.getBackground() == null) {
                    tabSpec.setIndicator(tabWidgetTextView.getText());
                } else {
                    tabSpec.setIndicator(tabWidgetTextView.getText(), tabWidgetTextView.getBackground());
                }
                tabHost.addTab(tabSpec);
            }


            tabHost.setCurrentTab(0);
            initialize(typeName[0]);

            tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
                @Override
                public void onTabChanged(String tag) {

                    initialize(tag);

                }});

            for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
            {
                TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    tv.setAllCaps(false);
                }
                tv.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.CENTER_VERTICAL);
                // En dessous de l'API 11 HoneyComb, les tab ont un height trop grand, d'ou le code suivant pour la diviser par 1.7
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    tabHost.getTabWidget().getChildAt(i).getLayoutParams().height /=1.7;
                }
            }
        }else{
            ((ViewGroup)_rootView.getParent()).removeView(_rootView);
        }




        return _rootView;
    }

    public void initialize(String tag){
        if(tag.equals("Liste")){
            if(adapter == null){
                adapter = new GeolocalisedRestaurantAdapter(getActivity(),
                        R.layout.geolocalised_restaurant_item
                );
                listView.setAdapter(adapter);
            }
        }
        else {

            if(adapter!=null){

                map.setMyLocationEnabled(true);
                if(FoodSquareApplication.currentLocation!=null){
                    currentLocation= FoodSquareApplication.currentLocation;
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));
                    map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).position(currentLocation));
                }else{
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(46.52863469527167,2.43896484375), 5));
                }

                map.clear();
                allMarkersMap = new HashMap<Marker, Restaurant>();
                Iterator it = adapter.getItems().iterator();
                while(it.hasNext()){
                    final Restaurant s =(Restaurant) it.next();
                    LatLng l = new LatLng(Double.parseDouble(s.getLatitude()), Double.parseDouble(s.getLongitude()));
                    MarkerOptions m = new MarkerOptions().title(s.getNom()).position(l);
                    Marker marker = map.addMarker(m);
                    allMarkersMap.put(marker,s);

                }

                map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {


                    ViewHolder holder;
                    // Use default InfoWindow frame
                    @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    // Defines the contents of the InfoWindow
                    @Override
                    public View getInfoContents(Marker arg0) {
                        Restaurant s =(Restaurant) allMarkersMap.get(arg0);

                        // Getting view from the layout file info_window_layout
                        View v = getSherlockActivity().getLayoutInflater().inflate(R.layout.maps_marker, null);

                        holder = new ViewHolder();
                        holder.titre = (TextView) v.findViewById(R.id.title);
                        holder.localisation = (TextView) v.findViewById(R.id.localisation);
                        holder.note = (TextView) v.findViewById(R.id.note);
                        holder.distance = (TextView) v.findViewById(R.id.distance);

                        holder.titre.setText(s.getNom());
                        holder.localisation.setText(s.getAdresse());
                        holder.note.setText("Note : "+s.getNote());
                        holder.distance.setText(s.getDistance());

                        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                Restaurant obj =(Restaurant) allMarkersMap.get(marker);
                                FragmentManager fragmentManager = ((BaseSlidingMenu)getSherlockActivity()).getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .addToBackStack(null)
                                        .replace(R.id.container, RestaurantFragment.newInstance(obj))
                                        .commit();
                            }
                        });


                        // Returning the view containing InfoWindow contents
                        return v;

                    }
                });



            }

        }
        setSelectedTabColor();


    }

    private class ViewHolder {
        TextView titre;
        TextView note;
        TextView localisation;
        TextView distance;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

        SupportMapFragment f = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment));
        if (f != null && getSherlockActivity() instanceof BaseSlidingMenu)
            getSherlockActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setSelectedTabColor() {
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(getSherlockActivity().getResources().getColor(R.color.tab_unselected));
            ((TextView)tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title)).setTextColor(getSherlockActivity().getResources().getColor(R.color.tab_text_unselected));
        }
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(getSherlockActivity().getResources().getColor(R.color.tab_selected));
        ((TextView)tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).findViewById(android.R.id.title)).setTextColor(getSherlockActivity().getResources().getColor(R.color.tab_text_selected));
    }

    @Override
    public void onResume() {
        super.onResume();
        getSherlockActivity().setTitle("À proximité");

    }

}
