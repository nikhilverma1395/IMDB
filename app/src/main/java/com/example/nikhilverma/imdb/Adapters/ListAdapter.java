package com.example.nikhilverma.imdb.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nikhilverma.imdb.Models.List_Model;
import com.example.nikhilverma.imdb.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Nikhil Verma on 07-01-2015.
 */
public class ListAdapter extends BaseAdapter {
    private Context activity;
    private LayoutInflater inflater;
    private static List<List_Model> movieItems;

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
            vh.year= (TextView) convertView.findViewById(R.id.year_search);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Picasso.with(activity)
                .load(movieItems.get(position).getURL())
                .error(R.drawable.images)
                .into(vh.iv);
        vh.rate.setText(movieItems.get(position).getRating());
        vh.title.setText(movieItems.get(position).getTitle());
        vh.year.setText(movieItems.get(position).getYear());
        return convertView;
    }

    public static class ViewHolder {
        ImageView iv;
        TextView title, rate , year;
    }
}
