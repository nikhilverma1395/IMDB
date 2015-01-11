package com.example.nikhilverma.imdb.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nikhilverma.imdb.Models.List_Model;
import com.example.nikhilverma.imdb.R;
import com.example.nikhilverma.imdb.Views.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Nikhil Verma on 07-01-2015.
 */
public class ListAdapter extends BaseAdapter {
    private Context activity;
    private LayoutInflater inflater;
    private static List<List_Model> movieItems;
    private int Colors[] = {0xE690A4AE, 0xE6FF6E40, 0xE6BDBDBD, 0xE6FFCCBC, 0xE6BCAAA4, 0xE6FFAB40,
            0xE6FFE57F, 0xE6FFA000, 0xE6FFEB3B, 0xE6FFB74D, 0xE669F0AE, 0xE6CCFF90, 0xE6EEFF41, 0xE69CCC65, 0xE6E6EE9C, 0xE6004D40, 0xE60277BD, 0xE600ACC1, 0xE6009688
            , 0xE62962FF, 0xE63F51B5, 0xE6F44336, 0xE6BA68C8, 0xE6D81B60};

    public ListAdapter(Context search, List<List_Model> arraylist) {
        this.activity = search;
        this.movieItems = arraylist;
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int position) {
        return movieItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.search_list_row, parent, false);
            vh = new ViewHolder();
            vh.iv = (ImageView) convertView.findViewById(R.id.search_list_image);
            vh.title = (TextView) convertView.findViewById(R.id.title);
            vh.rate = (TextView) convertView.findViewById(R.id.rating);
            vh.year = (TextView) convertView.findViewById(R.id.year_search);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Picasso.with(activity)
                .load(movieItems.get(position).getURL())
                .transform(new RoundedTransformation(20,2))
                .error(R.drawable.images)
                .into(vh.iv);
        vh.rate.setText(movieItems.get(position).getRating());
        vh.title.setText(movieItems.get(position).getTitle());
        vh.year.setText(movieItems.get(position).getYear());
        vh.rate.setTypeface(Typeface.createFromAsset(activity.getAssets(), "hyper.ttf"));
        vh.year.setTypeface(Typeface.createFromAsset(activity.getAssets(), "formal_regular.ttf"));
        vh.title.setTypeface(Typeface.createFromAsset(activity.getAssets(), "bol.TTF"));
        try {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{
                    Colors[((int) movieItems.get(position).getTitle().charAt(0)) % 24],
                    Colors[((int) movieItems.get(position).getTitle().charAt(movieItems.get(position).getTitle().length() - 1)) % 24]});
            convertView.findViewById(R.id.bottomline).setBackgroundColor(Colors[((int) movieItems.get(position).getTitle().charAt(movieItems.get(position).getTitle().length() / 2)) % 24]);
            convertView.findViewById(R.id.lllistback).setBackground(gd);
            gd.setCornerRadius(18f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public static class ViewHolder {
        ImageView iv;
        TextView title, rate, year;
    }
}
