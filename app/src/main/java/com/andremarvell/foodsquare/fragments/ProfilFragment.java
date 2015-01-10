package com.andremarvell.foodsquare.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.andremarvell.foodsquare.BaseSlidingMenu;
import com.andremarvell.foodsquare.R;
import com.andremarvell.foodsquare.FoodSquareApplication;


public class ProfilFragment extends SherlockFragment implements SwipeRefreshLayout.OnRefreshListener {





    SwipeRefreshLayout swipeLayout;
    View rootView;
    TextView nom;
    TextView mail;
    TextView score;
    TextView note;
    TextView comment;
    TextView lastConnect;


    public ProfilFragment() {
        // Required empty public constructor

    }

    /**
     * Fragment gérant l'ecran d'accueil
     *
     * @return Une nouvelle instance fragment de type Home.
     */
    public static ProfilFragment newInstance() {
        ProfilFragment fragment = new ProfilFragment();
        Bundle args = new Bundle();
/*        args.putString(ARG_DATE, dateProg);
        args.putInt(ARG_REUNION_NUMBER, reunionNumber);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //On change le titre de la nav bar
        getSherlockActivity().setTitle(R.string.app_name);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(rootView == null) {
            rootView = inflater.inflate(R.layout.activity_home, container, false);

            swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
            swipeLayout.setOnRefreshListener(this);

            // On charge le User
            ((TextView)rootView.findViewById(R.id.nom)).setText(FoodSquareApplication.USER.getPrenom()+" "+FoodSquareApplication.USER.getNom());
            ((ImageView) rootView.findViewById(R.id.photo)).setImageResource(((BaseSlidingMenu)getSherlockActivity()).getAndroidAvatarDrawable(FoodSquareApplication.USER.getPhoto(), getSherlockActivity().getPackageName()));

            nom = (TextView) rootView.findViewById(R.id.nom);
            comment = (TextView) rootView.findViewById(R.id.comments);
            note = (TextView) rootView.findViewById(R.id.notes);
            mail = (TextView) rootView.findViewById(R.id.mail);
            score = (TextView) rootView.findViewById(R.id.score);
            lastConnect= (TextView) rootView.findViewById(R.id.last);;
        }else{
            ((ViewGroup)rootView.getParent()).removeView(rootView);
        }

        init();



        return rootView;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //On change le titre de la nav bar
        getSherlockActivity().setTitle(R.string.app_name);

    }

    @Override
    public void onRefresh() {


        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeLayout.setRefreshing(false);

            }
        }, 3000);
    }

    public void init(){

        nom.setText(FoodSquareApplication.USER.getPrenom()+" "+FoodSquareApplication.USER.getNom());
        score.setText(FoodSquareApplication.USER.getScore());
        comment.setText(FoodSquareApplication.USER.getNbComment());
        note.setText(FoodSquareApplication.USER.getNbNotes());
        mail.setText(FoodSquareApplication.USER.getEmail());
        lastConnect.setText(FoodSquareApplication.USER.getLastConnection());

    }


}

