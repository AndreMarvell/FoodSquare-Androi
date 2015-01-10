package com.andremarvell.foodsquare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.andremarvell.foodsquare.fragments.LoginFragment;
import com.andremarvell.foodsquare.fragments.WelcomeFragment;


public class SplashScreen extends SherlockFragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SherlockFragment f;
        Intent i = getIntent();
        if(!i.getBooleanExtra("disconnect", false))
            f = new WelcomeFragment();
        else
            f = new LoginFragment();


        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, f )
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
