package com.nikhilvermavit.nikhilverma.imdb.parser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.nikhilvermavit.nikhilverma.imdb.Views.RoundedTransformation;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Nikhil Verma on 1/18/2015.
 */


public class ImageBitmap extends AsyncTask<String, Void, Bitmap> {
    ImageView str;
    Bitmap im = null;

    public ImageBitmap(ImageView s) {
        str = s;
    }

    @Override
    protected Bitmap doInBackground(String[] params) {
        try {
            im = BitmapFactory.decodeStream(new URL(params[0]).openConnection().getInputStream());
            if (str != null)
                im = new RoundedTransformation(20, 1).transform(im);
            im = Bitmap.createScaledBitmap(im, 300, 300, false);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return im;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (str != null)
            str.setImageBitmap(bitmap);
    }

    public Bitmap getIm() {
        return im;
    }
}
