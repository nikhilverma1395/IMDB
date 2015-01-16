package com.example.nikhilverma.imdb.Views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

/**
 * Created by Nikhil Verma on 1/16/2015.
 */
public class BlurBuilder {
    private static final float BITMAP_SCALE = 0.4f;
    private static final float BITMAP_RADIUS = 7.5f;

    public static Bitmap blur(View view) {
        return blur(view.getContext(), getScreenshot(view));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blur(Context ctx, Bitmap bmp) {

        //int width = Math.round(bmp.getWidth() * BITMAP_SCALE);
        //int height = Math.round(bmp.getHeight() * BITMAP_SCALE);
        //Bitmap inpB = Bitmap.createScaledBitmap(bmp, width, height, false);
        Bitmap opB = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
        RenderScript rs = RenderScript.create(ctx);
        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.A_8(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, bmp);
        Allocation tmpOut = Allocation.createFromBitmap(rs, opB);
        intrinsicBlur.setRadius(BITMAP_RADIUS);
        intrinsicBlur.setInput(tmpIn);
        intrinsicBlur.forEach(tmpOut);
        tmpOut.copyTo(opB);
        bmp.recycle();
        rs.destroy();
        return opB;
    }

    private static Bitmap getScreenshot(View view) {
        Bitmap bmp = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        view.draw(c);
        return bmp;
    }

}
