package com.andremarvell.foodsquare.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;
import com.andremarvell.foodsquare.BaseSlidingMenu;
import com.andremarvell.foodsquare.R;
import com.andremarvell.foodsquare.FoodSquareApplication;
import com.andremarvell.foodsquare.SplashScreen;
import com.andremarvell.foodsquare.webservices.user.Update;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UpdateFragment extends SherlockFragment implements SwipeRefreshLayout.OnRefreshListener {


    EditText nom;
    EditText prenom;
    EditText email;

    String avatar = "nophoto";

    Button valider;


    public UpdateFragment() {
        // Required empty public constructor

    }

    /**
     * Fragment gérant l'ecran d'inscription
     *
     * @return Une nouvelle instance fragment de type Home.
     */
    public static UpdateFragment newInstance() {
        UpdateFragment fragment = new UpdateFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView;
        if(getSherlockActivity() instanceof SplashScreen)
            rootView = inflater.inflate(R.layout.activity_inscription, container, false);
        else
            rootView = inflater.inflate(R.layout.activity_update, container, false);


        nom = (EditText) rootView.findViewById(R.id.nomInscription);
        prenom = (EditText) rootView.findViewById(R.id.prenomInscription);
        email = (EditText) rootView.findViewById(R.id.emailInscription);

        valider = (Button) rootView.findViewById(R.id.valider);
        email.setEnabled(false);

        email.setText(FoodSquareApplication.USER.getEmail());

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                majUser();
            }
        });

        // On configure la selection de l'avatar
        (rootView.findViewById(R.id.avatar1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ImageView)rootView.findViewById(R.id.avatar2)).setVisibility(View.GONE);
                ((ImageView)rootView.findViewById(R.id.avatar3)).setVisibility(View.GONE);
                ((ImageView)rootView.findViewById(R.id.avatar4)).setVisibility(View.GONE);
                ((ImageView)rootView.findViewById(R.id.avatar5)).setVisibility(View.GONE);
                avatar = "avatar1";
            }
        });
        (rootView.findViewById(R.id.avatar2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ImageView)rootView.findViewById(R.id.avatar1)).setVisibility(View.GONE);
                ((ImageView)rootView.findViewById(R.id.avatar3)).setVisibility(View.GONE);
                ((ImageView)rootView.findViewById(R.id.avatar4)).setVisibility(View.GONE);
                ((ImageView)rootView.findViewById(R.id.avatar5)).setVisibility(View.GONE);
                avatar = "avatar2";
            }
        });
        (rootView.findViewById(R.id.avatar3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ImageView)rootView.findViewById(R.id.avatar1)).setVisibility(View.GONE);
                ((ImageView)rootView.findViewById(R.id.avatar2)).setVisibility(View.GONE);
                ((ImageView)rootView.findViewById(R.id.avatar4)).setVisibility(View.GONE);
                ((ImageView)rootView.findViewById(R.id.avatar5)).setVisibility(View.GONE);
                avatar = "avatar3";
            }
        });
        (rootView.findViewById(R.id.avatar4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ImageView)rootView.findViewById(R.id.avatar1)).setVisibility(View.GONE);
                ((ImageView)rootView.findViewById(R.id.avatar2)).setVisibility(View.GONE);
                ((ImageView)rootView.findViewById(R.id.avatar3)).setVisibility(View.GONE);
                ((ImageView)rootView.findViewById(R.id.avatar5)).setVisibility(View.GONE);
                avatar = "avatar4";
            }
        });
        (rootView.findViewById(R.id.avatar5)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ImageView)rootView.findViewById(R.id.avatar1)).setVisibility(View.GONE);
                ((ImageView)rootView.findViewById(R.id.avatar2)).setVisibility(View.GONE);
                ((ImageView)rootView.findViewById(R.id.avatar3)).setVisibility(View.GONE);
                ((ImageView)rootView.findViewById(R.id.avatar4)).setVisibility(View.GONE);
                avatar = "avatar5";
            }
        });







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

    }

    public boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public void majUser(){

            if(getSherlockActivity().getCurrentFocus()!=null){
                InputMethodManager inputManager =
                        (InputMethodManager) getSherlockActivity().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        getSherlockActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }

            if(

                    !nom.getText().toString().equals("") &&
                    !prenom.getText().toString().equals("") &&
                    isEmailValid(email.getText().toString())

              ){

                FoodSquareApplication.USER.setNom(((BaseSlidingMenu)getSherlockActivity()).removeAccent(nom.getText().toString()));
                FoodSquareApplication.USER.setPrenom(((BaseSlidingMenu)getSherlockActivity()).removeAccent(prenom.getText().toString()));
                FoodSquareApplication.USER.setEmail(email.getText().toString());
                FoodSquareApplication.USER.setPhoto(avatar);

                (new Update(getSherlockActivity())).execute();
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
                builder.setMessage("Merci de renseigner correctement tous les champs")
                        .setPositiveButton("Ok",null);
                AlertDialog dialog1 = builder.create();
                dialog1.show();
            }

    }


}

