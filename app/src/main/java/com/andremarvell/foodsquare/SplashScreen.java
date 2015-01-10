package com.andremarvell.foodsquare;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.andremarvell.foodsquare.classe.GPSTracker;
import com.andremarvell.foodsquare.fragments.LoginFragment;


public class SplashScreen extends SherlockFragmentActivity {

    private LoginFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On cree l'objet GPSTracker pour recuperer les coordonnées gps du user
        GPSTracker gps = new GPSTracker(SplashScreen.this);

        // On verifie si le gps est activé
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            FoodSquareApplication.setCurrentLocation(latitude,longitude);

        }

        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            mainFragment = new LoginFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, mainFragment)
                    .commit();
        } else {
            // Or set the fragment from restored state info
            mainFragment = (LoginFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
