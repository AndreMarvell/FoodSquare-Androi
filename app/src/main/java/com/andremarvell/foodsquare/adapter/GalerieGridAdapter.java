package com.andremarvell.foodsquare.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.andremarvell.foodsquare.FullScreenViewActivity;
import com.andremarvell.foodsquare.R;
import com.andremarvell.foodsquare.classe.Photo;
import com.andremarvell.foodsquare.assets.imageloader.ImageLoader;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ikounga_marvel on 12/09/2014.
 */
public class GalerieGridAdapter extends BaseAdapter {
    private Context context;

    private List<Photo> items;

    public GalerieGridAdapter(Context context, List<Photo> items) {
        this.context = context;
        this.items = items;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = inflater.inflate(R.layout.gallerie_picture_item, null);

            holder.imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);

            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        (new ImageLoader(context)).DisplayImage(items.get(position).getMiniature(),holder.imageView);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, FullScreenViewActivity.class);
                i.putExtra("position", position);
                i.putExtra("photos", (Serializable) items);
                context.startActivity(i);

            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
    }



}
