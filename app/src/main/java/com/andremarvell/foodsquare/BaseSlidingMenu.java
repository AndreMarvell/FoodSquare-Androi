package com.andremarvell.foodsquare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.andremarvell.foodsquare.adapter.AutocompleteRestaurantAdapter;
import com.andremarvell.foodsquare.adapter.SlidingMenuAdapter;
import com.andremarvell.foodsquare.fragments.ProfilFragment;
import com.andremarvell.foodsquare.fragments.RestaurantGeolocalise;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Marvell
 */
public class BaseSlidingMenu extends SherlockFragmentActivity {

    protected SlidingMenu sMenuLeft;
    protected boolean layoutIsLoaded;

    SearchView search;

    Bundle bdle;

    AutocompleteRestaurantAdapter adapter;
    AutoCompleteTextView autoComplete;
    private Timer timer;
    private TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            if(isOnline()){
                stopCheckingConnexion();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadActivity();
                    }
                });
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bdle = savedInstanceState;

        layoutIsLoaded = false;

        //On configure le slidingMenu Gauche
        sMenuLeft = new SlidingMenu(this);
        sMenuLeft.setMode(SlidingMenu.LEFT);
        sMenuLeft.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        sMenuLeft.setShadowWidthRes(R.dimen.shadow_width);
        sMenuLeft.setShadowDrawable(R.drawable.shadow);
        sMenuLeft.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sMenuLeft.setFadeDegree(0.35f);
        sMenuLeft.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        sMenuLeft.setMenu(R.layout.activity_menu);



        // On configure l'actionBarSherlock
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowTitleEnabled(false);
        bar.setDisplayShowCustomEnabled(true);
        bar.setCustomView(R.layout.custom_actionbar);

        ((TextView) bar.getCustomView().findViewById(R.id.actionBarTitle)).setText(R.string.app_name);

        this.loadActivity();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            sMenuLeft.toggle();
            return true;
        }else if (id == R.id.action_geolocalise) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            if(!(fragmentManager.findFragmentById(R.id.container) instanceof RestaurantGeolocalise)){
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, RestaurantGeolocalise.newInstance())
                        .commit();
                return false;
            }

        }else if (id == R.id.action_search) {
            autoComplete.setAdapter(adapter);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.home, menu);

        initialiseSearchBar(menu);

        return true;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public void toggleSlidingMenu() {
        sMenuLeft.toggle();
    }

    public void startCheckingConnexion() {
        if(timer != null) {
            return;
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 5000, 10000);
    }

    public void stopCheckingConnexion() {
        timer.cancel();
        timer = null;
    }

    public void loadActivity(){
        if(!isOnline()){
            layoutIsLoaded = false;
            setContentView(R.layout.activity_offline);

            sMenuLeft.getMenu().findViewById(R.id.imageViewConnexionLostLoader).setVisibility(View.VISIBLE);
            sMenuLeft.getMenu().findViewById(R.id.textViewConnexionLostMessage).setVisibility(View.VISIBLE);
            startCheckingConnexion();

        }else {

            setContentView(R.layout.fragment_container);
            if (bdle == null) {
                try{
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, RestaurantGeolocalise.newInstance())
                            .commitAllowingStateLoss();
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }
            }
            layoutIsLoaded = true;

            ((ListView)sMenuLeft.getMenu().findViewById(R.id.menu_listview)).setAdapter(new SlidingMenuAdapter(this));

            ((ImageView) sMenuLeft.getMenu().findViewById(R.id.profil)).setImageResource(getAndroidAvatarDrawable(FoodSquareApplication.USER.getPhoto(), getPackageName()));
            ((TextView) sMenuLeft.getMenu().findViewById(R.id.nom)).setText(FoodSquareApplication.USER.getPrenom() + " " + FoodSquareApplication.USER.getNom());
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        try {
            ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.actionBarTitle)).setText(title);
        } catch (Exception e) {
            System.err.println("Exception Sur le titre du fragment "+title+": "+ e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public int getAndroidAvatarDrawable(String pDrawableName, String packageName){
        String uri = "drawable/"+pDrawableName;
        int resourceId = getResources().getIdentifier(uri, null, getPackageName());
        if(resourceId==0){
            return R.drawable.nophoto;
        } else {
            return resourceId;
        }
    }

    public void initialiseSearchBar(Menu menu){
        /*
        *
        * On configure la searchview ici
        * Couleur Texte
        * Couleur placeholder
        * Attachement de l'autocomplete adapter
        * */
        search = (SearchView) menu.findItem(R.id.action_search).getActionView();

        //int searchSrcTextId = getResources().getIdentifier("actionbarsherlock:id/abs__search_src_text", null, null);
        autoComplete = (AutoCompleteTextView) search.findViewById(R.id.abs__search_src_text);
        autoComplete.setTextColor(Color.BLACK);
        autoComplete.setHintTextColor(Color.GRAY);
        autoComplete.setBackgroundColor(Color.WHITE);
        autoComplete.setBackgroundResource(android.R.drawable.editbox_background_normal);

        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(autoComplete, 0); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {}
        //((ImageView)search.findViewById(R.id.abs__search_close_btn)).setBackgroundColor(Color.WHITE);

        search.setQueryHint("Rechercher un restaurant");
        adapter = new AutocompleteRestaurantAdapter(this);
        //On affecte cette liste d'autocompletion à notre objet d'autocompletion
        autoComplete.setAdapter(adapter);
    }

    public void focusSearchview(){
        if(search!=null)
            search.setIconified(false);
    }

    public void disconnect(){
        SharedPreferences sharedPref = getSharedPreferences("UserPersist",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("user").commit();
        FoodSquareApplication.USER = null;

        Intent i = new Intent(this, SplashScreen.class);
        i.putExtra("disconnect", true);
        startActivity(i);
        finish();
    }



}
