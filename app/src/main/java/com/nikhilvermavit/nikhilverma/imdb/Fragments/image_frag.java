package com.nikhilvermavit.nikhilverma.imdb.Fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ScaleImageView;
import com.nikhilvermavit.nikhilverma.imdb.R;
import com.nikhilvermavit.nikhilverma.imdb.Views.BlurBuilder;
import com.nikhilvermavit.nikhilverma.imdb.Views.RoundedTransformation;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Nikhil Verma on 1/16/2015.
 */
public class image_frag extends Fragment {
    private static String urel;
    Bitmap bitmap;
    Drawable drawable;
    LinearLayout linearLayout;
    ScaleImageView scaleImageView;
    Bitmap image = null;
    String title;
    String rating;

    public image_frag() {
    }

    public image_frag(String fre, String t, String r) {
        this.urel = fre;

        title = t;
        rating = r;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.full_image_frag, container, false);
        final Activity activity = getActivity();
        Bitmap im = null;
        scaleImageView = (ScaleImageView) v.findViewById(R.id.image_frag);
        try
        {
            im = BitmapFactory.decodeStream(new URL(urel).openConnection().getInputStream());
            im = new RoundedTransformation(20, 1).transform(im);
            scaleImageView.setImageBitmap(im);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap tempbg = BitmapFactory.decodeResource(getResources(), android.R.color.transparent);
        Bitmap final_Bitmap = new BlurBuilder(23).BlurImage(im, getActivity());
        TextView tv = (TextView) v.findViewById(R.id.full_title);
        TextView tv1 = (TextView) v.findViewById(R.id.full_rating);
        tv.setText(title);
        tv1.setText(rating);
      //  Bitmap nmp = Bitmap.createScaledBitmap(final_Bitmap, 120, 120, false);
        v.findViewById(R.id.back_bitch).setBackground(new BitmapDrawable(final_Bitmap));
        return v;
    }
}