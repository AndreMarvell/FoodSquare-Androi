package com.andremarvell.foodsquare.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.andremarvell.foodsquare.R;
import com.andremarvell.foodsquare.adapter.FavorisAdapter;


public class FavorisFragment extends SherlockFragment  implements SwipeRefreshLayout.OnRefreshListener{

    View _rootView;

    ListView listView;
    SwipeRefreshLayout swipeLayout;

    FavorisAdapter adapter;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return Une nouvelle instance fragment de type  Favoris.
     */
    public static FavorisFragment newInstance() {
        FavorisFragment fragment = new FavorisFragment();
        return fragment;

    }
    public FavorisFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //On change le titre de la nav bar
        getSherlockActivity().setTitle("Favoris");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(_rootView == null){
            _rootView = inflater.inflate(R.layout.restaurant_favoris, container, false);

            swipeLayout = (SwipeRefreshLayout) _rootView.findViewById(R.id.swipe_container);
            swipeLayout.setOnRefreshListener(this);

            listView = (ListView) _rootView.findViewById(R.id.listview);
            adapter = new FavorisAdapter(getSherlockActivity(),R.layout.geolocalised_restaurant_item);
            listView.setAdapter(adapter);

        }else{
            ((ViewGroup)_rootView.getParent()).removeView(_rootView);
        }




        return _rootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    @Override
    public void onResume() {
        super.onResume();
        //On change le titre de la nav bar
        getSherlockActivity().setTitle("Favoris");

    }

    @Override
    public void onRefresh() {

        adapter = new FavorisAdapter(getSherlockActivity(),R.layout.geolocalised_restaurant_item);
        listView.setAdapter(adapter);

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeLayout.setRefreshing(false);

            }
        }, 1500);
    }

}
