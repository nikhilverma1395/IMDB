package com.example.nikhilverma.imdb.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nikhilverma.imdb.Models.List_Model;
import com.example.nikhilverma.imdb.R;
import com.example.nikhilverma.imdb.Views.BlurBuilder;
import com.example.nikhilverma.imdb.Views.RoundedTransformation;
import com.example.nikhilverma.imdb.parser.ImageBitmap;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Nikhil Verma on 07-01-2015.
 */
public class ListAdapter extends BaseAdapter {
    private static List<List_Model> movieItems;
    private Context activity;
    private LayoutInflater inflater;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

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
        } else
            vh = (ViewHolder) convertView.getTag();
        final ViewHolder holder = vh;
        ImageBitmap ib = new ImageBitmap(vh.iv);
        final ImageView ivf = vh.iv;
        final LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.lllistback);
        final View covertVieww = convertView;

        Picasso.with(activity)
                .load(movieItems.get(position).getURL())
                .resize(300, 300)
                .transform(new RoundedTransformation(40, 1))
                .error(R.drawable.images)
                .into(vh.iv, new Callback() {
                    @Override
                    public void onSuccess() {

                        ivf.buildDrawingCache(true);
                        Bitmap bitmap = ivf.getDrawingCache(true);
                        BitmapDrawable drawable = (BitmapDrawable) ivf.getDrawable();

                        Bitmap gt = drawable.getBitmap();
                        //ll.setBackground(gd);

                        try {
                            ll.setBackground(new BitmapDrawable(new BlurBuilder(12).BlurImage(gt, activity)));
                        } catch (Exception w) {
                            w.printStackTrace();
                        }
                    }

                    @Override
                    public void onError() {
                        BitmapDrawable bd = (BitmapDrawable) activity.getResources().getDrawable(R.drawable.back_gradient);
                        Bitmap blurred = new BlurBuilder(11).BlurImage(bd.getBitmap(), activity);
                        blurred = new RoundedTransformation(20, 0).transform(blurred);
                        ll.setBackground(new BitmapDrawable(blurred));
                    }
                });
        vh.rate.setText(movieItems.get(position).getRating());
        vh.title.setText(movieItems.get(position).getTitle());
        vh.year.setText(movieItems.get(position).getYear());
        vh.rate.setTypeface(Typeface.createFromAsset(activity.getAssets(), "hyper.ttf"));
        vh.year.setTypeface(Typeface.createFromAsset(activity.getAssets(), "formal_regular.ttf"));
        vh.title.setTypeface(Typeface.createFromAsset(activity.getAssets(), "bol.TTF"));
        // GradientDrawable gd = null;
        //gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{
        //      Colors[((int) movieItems.get(position).getTitle().charAt(0)) % 24],
        //    Colors[((int) movieItems.get(position).getTitle().charAt(movieItems.get(position).getTitle().length() - 1)) % 24]});
        // gd.setCornerRadius(18f);
        return convertView;
    }

    public static class ViewHolder {
        ImageView iv;
        TextView title, rate, year;
    }
}
