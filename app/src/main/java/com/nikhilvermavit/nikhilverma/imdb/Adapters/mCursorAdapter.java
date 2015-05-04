package com.nikhilvermavit.nikhilverma.imdb.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nikhilvermavit.nikhilverma.imdb.Activites.MainActivity;
import com.nikhilvermavit.nikhilverma.imdb.R;
import com.nikhilvermavit.nikhilverma.imdb.sqlite.DataSource;
import com.nikhilvermavit.nikhilverma.imdb.sqlite.SqliteHelper;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by Nikhil Verma on 1/18/2015.
 */
public class mCursorAdapter extends CursorAdapter {
    public static long url_id;
    static boolean show_error;
    static TextView error;
    boolean bub = true;
    private ListView listView;
    private Context context;

    public mCursorAdapter(Context context, Cursor c, ListView listView, TextView error1) {
        super(context, c, 0);
        context = context;
        this.error = error1;
        this.listView = listView;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_mag, parent, false);
        ViewHolder vh = new ViewHolder();
        vh.iv = (ImageView) v.findViewById(R.id.search_list_image);
        vh.titletv = (TextView) v.findViewById(R.id.title);
        vh.pbci = (ProgressBarCircularIndeterminate) v.findViewById(R.id.progressBar_list_row);
        vh.ratetv = (TextView) v.findViewById(R.id.rating);
        vh.yeartv = (TextView) v.findViewById(R.id.year_search);
        vh.imageButton = (ImageButton) v.findViewById(R.id.imageButton_delete);
        vh.ll = (RelativeLayout) v.findViewById(R.id.main_rel);
        vh.errorinlist = (TextView) v.findViewById(R.id.error_inlist);
        v.setTag(vh);
        return v;
    }

    @Override
    public void bindView(final View convertView, final Context context, final Cursor cursor) {
        final int position = cursor.getPosition();
        int lastPosition = -1;

        final ViewHolder vh = (ViewHolder) convertView.getTag();
        final ImageView ivf = vh.iv;
        url_id = cursor.getLong(cursor.getColumnIndexOrThrow(SqliteHelper.COLUMN_IDs));
        final String url = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.COLUMN_IMAGE_URL));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.COLUMN_TITLE));
        String year = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.COLUMN_YEAR));
        String rating = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.COLUMN_RATING));
        final RelativeLayout lel = vh.ll;
        final View covertVieww = convertView;
        //  final Dialog dialog = new Dialog(context, " Delete Confirmation ", " Do You Want to Delete " + "\t " + title + "?");
        vh.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog.show();
                //  ButtonFlat acceptButton = dialog.getButtonAccept();

                convertView.animate().setDuration(350)
                        .alpha(0)
                        .translationX(1080)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                deleteFromDatabase(url_id, cursor.getCount());
                                cursor.requery();

                                notifyDataSetChanged();
                            }
                        });


            }
        });
        vh.pbci.setVisibility(View.VISIBLE);
        Picasso.with(context)
                .load(url)
                .resize(200, 300)
                .error(R.drawable.images)
                .into(vh.iv, new Callback() {
                    @Override
                    public void onSuccess() {
                        vh.pbci.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        vh.pbci.setVisibility(View.GONE);
                    }
                });
        vh.ratetv.setText(rating);
        vh.titletv.setText(title);
        vh.yeartv.setText("\t|\t" + year);
        vh.ratetv.setTypeface(Typeface.createFromAsset(context.getAssets(), "hyper.ttf"));
        vh.yeartv.setTypeface(Typeface.createFromAsset(context.getAssets(), "formal_regular.ttf"));
        vh.titletv.setTypeface(Typeface.createFromAsset(context.getAssets(), "bol.TTF"));
        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.bottom_from_top);
        convertView.startAnimation(animation);
    }

    private void deleteFromDatabase(final long url_id, final long counts) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataSource ds = new DataSource(MainActivity.context);
                ds.deleteFromId(url_id);

            }
        }).start();
        try {
            if (counts == 1) {
                listView.setVisibility(View.INVISIBLE);
                error.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class ViewHolder {
        TextView titletv, ratetv, yeartv, errorinlist;
        ImageView iv;
        ImageButton imageButton;
        ProgressBarCircularIndeterminate pbci;
        RelativeLayout ll;

    }
}


