package com.andremarvell.foodsquare.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.andremarvell.foodsquare.BaseSlidingMenu;
import com.andremarvell.foodsquare.FoodSquareApplication;
import com.andremarvell.foodsquare.R;
import com.andremarvell.foodsquare.adapter.FavorisAdapter;
import com.andremarvell.foodsquare.classe.GPSTracker;


public class WelcomeFragment extends SherlockFragment{

    private static int SPLASH_TIME_OUT = 2000;
    View _rootView;
    private ImageView logo;



    public WelcomeFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return Une nouvelle instance fragment de type  Favoris.
     */
    public static WelcomeFragment newInstance() {
        WelcomeFragment fragment = new WelcomeFragment();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(_rootView == null){
            _rootView = inflater.inflate(R.layout.activity_splash_screen, container, false);

            logo = (ImageView) _rootView.findViewById(R.id.icon);
            logo.setVisibility(View.INVISIBLE);

        }else{
            ((ViewGroup)_rootView.getParent()).removeView(_rootView);
        }

        continueLoadingSplash();

        return _rootView;
    }

    public void continueLoadingSplash(){


        logo.setVisibility(View.VISIBLE);
        final Animation outtoRight = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_PARENT,+0.5f,
                TranslateAnimation.RELATIVE_TO_PARENT,0.0f,
                TranslateAnimation.RELATIVE_TO_PARENT,0.0f,
                TranslateAnimation.RELATIVE_TO_PARENT,0.0f);
        outtoRight.setDuration(1500);
        outtoRight.setFillAfter(true);
        outtoRight.setInterpolator(new DecelerateInterpolator());


        // On cree l'objet GPSTracker pour recuperer les coordonnées gps du user
        GPSTracker gps = new GPSTracker(getSherlockActivity());
        // On verifie si le gps est activé
        if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            FoodSquareApplication.setCurrentLocation(latitude, longitude);
        }

        logo.startAnimation(outtoRight);
        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {

                if(FoodSquareApplication.getUSER(getSherlockActivity())){
                    // On cree l'intent et on lance l'activé
                    Intent i = new Intent(getSherlockActivity(), BaseSlidingMenu.class);
                    startActivity(i);
                    // On clos l'activity SplashScreen
                    getSherlockActivity().finish();
                }else{
                    getSherlockActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .add(android.R.id.content, new LoginFragment())
                            .commit();
                }



            }
        }, SPLASH_TIME_OUT);
    }


}
