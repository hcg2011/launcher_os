package com.android.launcher3.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by prize
 * DATE 2018/3/15
 * TIME 11:15
 */

public class BitmapUtil {

    // 等比缩放图片
    public static Bitmap zoomImg(Bitmap bm, int newWidth , int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

}
