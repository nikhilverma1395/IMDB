package com.nikhilvermavit.nikhilverma.imdb.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

/**
 * Created by Nikhil Verma on 1/16/2015.
 */
public class BlurBuilder {
    static int radius;

    public BlurBuilder(final int radius) {
        this.radius = radius;
    }

    public  Bitmap BlurImage(View view) {
        return BlurImage(getScreenshot(view), view.getContext());
    }

                    public  Bitmap BlurImage(Bitmap input, Context ctx) {
                        try {
                            RenderScript rsScript = RenderScript.create(ctx);
                            Allocation alloc = Allocation.createFromBitmap(rsScript, input);

                           // ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rsScript, Element.U8_4(rsScript));
                            ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rsScript, Element.U8_4(rsScript));
                            blur.setRadius(radius);
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

    private  Bitmap getScreenshot(View view) {
        Bitmap bmp = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        view.draw(c);
        return bmp;
    }

}
