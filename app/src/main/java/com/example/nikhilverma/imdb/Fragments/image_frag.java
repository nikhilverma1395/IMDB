package com.example.nikhilverma.imdb.Fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ScaleImageView;
import com.example.nikhilverma.imdb.R;
import com.example.nikhilverma.imdb.Views.BlurBuilder;
import com.example.nikhilverma.imdb.Views.RoundedTransformation;

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
        try {
            im = BitmapFactory.decodeStream(new URL(urel).openConnection().getInputStream());
            im = new RoundedTransformation(20, 1).transform(im);
            scaleImageView.setImageBitmap(im);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap tempbg = BitmapFactory.decodeResource(getResources(), android.R.color.transparent);
        Bitmap final_Bitmap = BlurImage(im);
        TextView tv = (TextView) v.findViewById(R.id.full_title);
        TextView tv1 = (TextView) v.findViewById(R.id.full_rating);
        tv.setText(title);
        tv1.setText(rating);
        v.findViewById(R.id.back_bitch).setBackground(new BitmapDrawable(final_Bitmap));
        final View content = activity.findViewById(android.R.id.content).getRootView();
        if (content.getWidth() > 0) {
            Bitmap image = BlurBuilder.blur(content);
            //  v.setBackground(new BitmapDrawable(activity.getResources(), image));
        } else {
            content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Bitmap image = BlurBuilder.blur(content);
                    //    v.setBackground(new BitmapDrawable(activity.getResources(), image));
                }
            });
        }
        return v;
    }

    Bitmap BlurImage(Bitmap input) {
        try {
            RenderScript rsScript = RenderScript.create(getActivity());
            Allocation alloc = Allocation.createFromBitmap(rsScript, input);

            ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rsScript, Element.U8_4(rsScript));
            blur.setRadius(21);
            blur.setInput(alloc);

            Bitmap result = Bitmap.createBitmap(input.getWidth(), input.getHeight(), Bitmap.Config.ARGB_8888);
            Allocation outAlloc = Allocation.createFromBitmap(rsScript, result);

            blur.forEach(outAlloc);
            outAlloc.copyTo(result);

            rsScript.destroy();
            return result;
        } catch (Exception e) {
            // TODO: handle exception
            return input;
        }

    }
}
