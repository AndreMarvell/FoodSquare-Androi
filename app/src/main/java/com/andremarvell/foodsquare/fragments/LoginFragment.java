package com.andremarvell.foodsquare.fragments;



import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;
import com.andremarvell.foodsquare.R;
import com.andremarvell.foodsquare.webservices.user.Connexion;
import com.andremarvell.foodsquare.webservices.user.Inscription;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class LoginFragment extends SherlockFragment {

    // Pour la connexion normale

    private static final String TAG = "FBLoginFragment";
    EditText identifiant;

    // Connexion Facebook
    EditText password;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_splash_login, container, false);


        // Connexion normale

        identifiant = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);

        identifiant.setText("marvell.ikounga@gmail.com");
        password.setText("jolianejtm");

        ((Button)view.findViewById(R.id.connexion)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getSherlockActivity().getCurrentFocus()!=null){
                    InputMethodManager inputManager =
                            (InputMethodManager) getSherlockActivity().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            getSherlockActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }

                if(
                    isEmailValid(identifiant.getText().toString()) &&
                            !password.getText().toString().equals("")
                  ){
                    (new Connexion(getSherlockActivity())).execute(
                            identifiant.getText().toString(),
                            password.getText().toString()
                    );
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
                    builder.setMessage("Merci de renseigner correctement tous les champs")
                            .setPositiveButton("Ok",null);
                    AlertDialog dialog1 = builder.create();
                    dialog1.show();
                }


            }
        });
        ((Button)view.findViewById(R.id.inscription)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getSherlockActivity().getCurrentFocus()!=null){
                    InputMethodManager inputManager =
                            (InputMethodManager) getSherlockActivity().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            getSherlockActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }

                if(
                    isEmailValid(identifiant.getText().toString()) &&
                    !password.getText().toString().equals("")
                  ){
                    (new Inscription(getSherlockActivity())).execute(
                            identifiant.getText().toString(),
                            password.getText().toString()
                    );
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
                    builder.setMessage("Merci de renseigner correctement tous les champs")
                            .setPositiveButton("Ok",null);
                    AlertDialog dialog1 = builder.create();
                    dialog1.show();
                }
            }
        });


        return view;
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


}
