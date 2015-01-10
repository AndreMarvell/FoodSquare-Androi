package com.andremarvell.foodsquare.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.andremarvell.foodsquare.R;
import com.andremarvell.foodsquare.classe.Photo;
import com.andremarvell.foodsquare.assets.imageloader.ImageLoader;

public class FullScreenImageAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<Photo> photos;
    private LayoutInflater inflater;

    // constructor
    public FullScreenImageAdapter(Activity activity,
                                  ArrayList<Photo> imagePaths) {
        this._activity = activity;
        this.photos = imagePaths;
    }

    @Override
    public int getCount() {
        return this.photos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgDisplay;
        ImageView btnClose;

        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.restaurant_gallerie_fullscreen, container,
                false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.restoImage);
        btnClose = (ImageView) viewLayout.findViewById(R.id.closebutton);

        (new ImageLoader(_activity)).DisplayImage(photos.get(position).getUrl(),imgDisplay);

        // close button click event
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _activity.finish();
            }
        });

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
