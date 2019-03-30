package com.android.launcher3.theme.tools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.android.launcher3.Launcher;
import com.android.launcher3.Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by prize on 2018/1/24.
 */

public class ImageUtils {
    /**
     * 创建遮罩图片
     *
     * @param mask     遮罩图片 可以实现不同形状的图片
     * @return
     */
    //add by zhouerlong prizeTheme add
    public static Bitmap createMaskImage(Bitmap source, Bitmap mask, Bitmap bt) {

        if(mask ==null) {
            return source;
        }
        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(),
                Bitmap.Config.ARGB_8888);
        // 将遮罩层的图片放到画布中
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));// 叠加重复的部分，

        mCanvas.drawBitmap(source, mask.getWidth() / 2 - source.getWidth() / 2, mask.getHeight() / 2 - source.getHeight() / 2, null);

        mCanvas.drawBitmap(mask, 0,
                0, paint);
        paint.setXfermode(null);
		//prize add by zhouerlong 20180521
        if (bt != null) {

            Bitmap r = doodle(result, bt);
            return r;
        }
		//prize add by zhouerlong 20180521
        return result;
    }
//add by zhouerlong 20180126 
    static public Bitmap drawableToBitmap(Drawable drawable) {
//add by zhouerlong 20180201
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
//add by zhouerlong 20180201
        canvas.save();
        // canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        canvas.restore();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable b = (BitmapDrawable) drawable;
            if (b != null && b.getBitmap() != bitmap) {
                b.setCallback(null);
                b = null;
                System.gc();
            }
        }

        return bitmap;

    }
//add by zhouerlong 20180126

    public static int[] getBitmapCantPixel(Bitmap bit, int resize) {
        int width = bit.getWidth();
        int height = bit.getHeight();
        int pixels[] = new int[4];
        int offsetX = resize;
        int offsetY = resize;

        pixels[0] = bit.getPixel(offsetX, offsetY); // left & top
        pixels[1] = bit.getPixel(width - offsetX, offsetY); // right& top
        pixels[2] = bit.getPixel(offsetX, height - offsetY); // left& bottom
        pixels[3] = bit.getPixel(width - offsetX, height - offsetY); // right
        // &bottom
        // pixels[4] = bit.getPixel(offsetX, height/2);//center left

        return pixels;
    }

    final static int TRANSPARENT = 0x00;

public static boolean neddResizeIcon(Bitmap bit, int resize) {
    int pixels[] = getBitmapCantPixel(bit, resize);

    boolean need = (pixels[0] == TRANSPARENT && pixels[1] == TRANSPARENT
            && pixels[2] == TRANSPARENT && pixels[3] == TRANSPARENT) ? false
            : true;

    return need;
}

    public static Bitmap getMaskIcon(Bitmap src, Bitmap mask,Bitmap bottom) {

        int w = mask.getWidth();
        if(src.getWidth()>w) {
            src=resize(src, w, w);
        }
        boolean need= src.getWidth()>w*0.8f;
        boolean srcResize = neddResizeIcon(src, (int)(Launcher.scale));
        //add by zhouerlong 允许默认主题

      if(srcResize&&need) {
            src = resize(src, (int)(w*0.99f), (int)(w*0.99f));
        }
        //add by zhouerlong 允许默认主题
        final Bitmap result = ImageUtils.createMaskImage(src, mask,bottom);

        return result;
    }


    public static Bitmap resize(Bitmap bm, int w, int h) {
        Bitmap BitmapOrg = bm;

        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        Bitmap bg = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// modify by
        // zhouerlong
        if (resizedBitmap != null) {
            return ImageUtils.doodle(resizedBitmap, bg);
        }
        if(bm!=null&&resizedBitmap!= bm)
        {
            bm.recycle();
        }
        return resizedBitmap;
    }


    public static Bitmap doodle(Bitmap src, Bitmap bg) {
        Bitmap newb = Bitmap.createBitmap(bg.getWidth(), bg.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newb);
        canvas.drawBitmap(bg, 0, 0, null);

        canvas.drawBitmap(src, (bg.getWidth() - src.getWidth()) / 2,
                (bg.getHeight() - src.getHeight()) / 2, null);

        canvas.save();
        canvas.restore();
        /*if (src != null && src != newb) {
            src.recycle();
            src = null;

        }*/

        // src.recycle();
        // src = null;

        return newb;
    }
}
