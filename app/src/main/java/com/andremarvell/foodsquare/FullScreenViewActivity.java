package com.andremarvell.foodsquare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.andremarvell.foodsquare.adapter.FullScreenImageAdapter;
import com.andremarvell.foodsquare.classe.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marvell on 31/12/14.
 */
public class FullScreenViewActivity extends Activity {

    private ArrayList<Photo> photos = new ArrayList<Photo>();

    private PagerAdapter mPagerAdapter;

    private ViewPager mPager;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen_view);

        // On recupere l'intent
        Intent i = getIntent();

        // loading all image paths from SD card
        photos =(ArrayList<Photo>) i.getSerializableExtra("photos");

        // Selected image id
        int position = i.getExtras().getInt("position");

        mPagerAdapter = new FullScreenImageAdapter(this, photos);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(i.getExtras().getInt("position"));

    }

}

