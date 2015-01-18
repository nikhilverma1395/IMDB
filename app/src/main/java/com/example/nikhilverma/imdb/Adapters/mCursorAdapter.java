package com.example.nikhilverma.imdb.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nikhilverma.imdb.R;
import com.example.nikhilverma.imdb.Views.BlurBuilder;
import com.example.nikhilverma.imdb.Views.RoundedTransformation;
import com.example.nikhilverma.imdb.sqlite.SqliteHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by Nikhil Verma on 1/18/2015.
 */
public class mCursorAdapter extends CursorAdapter {
    public mCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.search_list_row, parent, false);

    }

    @Override
    public void bindView(final View convertView, final Context context, Cursor cursor) {
        ImageView iv;
        TextView titletv, ratetv, yeartv;
        iv = (ImageView) convertView.findViewById(R.id.search_list_image);
        titletv = (TextView) convertView.findViewById(R.id.title);
        ratetv = (TextView) convertView.findViewById(R.id.rating);
        yeartv = (TextView) convertView.findViewById(R.id.year_search);
        final ImageView ivf = iv;
        final LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.lllistback);
        String url = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.COLUMN_IMAGE_URL));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.COLUMN_TITLE));
        String year = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.COLUMN_YEAR));
        String rating = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.COLUMN_RATING));
        final View covertVieww = convertView;
        Picasso.with(context)
                .load(url)
                .resize(300, 300)
                .transform(new RoundedTransformation(40, 1))
                .error(R.drawable.images)
                .into(iv, new Callback() {
                    @Override
                    public void onSuccess() {

                        ivf.buildDrawingCache(true);
                        Bitmap bitmap = ivf.getDrawingCache(true);
                        BitmapDrawable drawable = (BitmapDrawable) ivf.getDrawable();

                        Bitmap gt = drawable.getBitmap();
                        //ll.setBackground(gd);

                        try {
                            ll.setBackground(new BitmapDrawable(new BlurBuilder(12).BlurImage(gt, context)));
                        } catch (Exception w) {
                            w.printStackTrace();
                        }
                    }

                    @Override
                    public void onError() {
                        BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.drawable.back_gradient);
                        Bitmap blurred = new BlurBuilder(11).BlurImage(bd.getBitmap(), context);
                        blurred = new RoundedTransformation(20, 0).transform(blurred);
                        ll.setBackground(new BitmapDrawable(blurred));
                    }
                });
        ratetv.setText(rating);
        titletv.setText(title);
        yeartv.setText(year);
        ratetv.setTypeface(Typeface.createFromAsset(context.getAssets(), "hyper.ttf"));
        yeartv.setTypeface(Typeface.createFromAsset(context.getAssets(), "formal_regular.ttf"));
        titletv.setTypeface(Typeface.createFromAsset(context.getAssets(), "bol.TTF"));
        // GradientDrawable gd = null;
        //gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{
        //      Colors[((int) movieItems.get(position).getTitle().charAt(0)) % 24],
        //    Colors[((int) movieItems.get(position).getTitle().charAt(movieItems.get(position).getTitle().length() - 1)) % 24]});
        // gd.setCornerRadius(18f);

    }


}

