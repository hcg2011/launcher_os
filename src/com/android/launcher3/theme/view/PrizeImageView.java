package com.android.launcher3.theme.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.android.launcher3.Launcher;
import com.android.launcher3.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by prize on 2018/1/24.
 */

public class PrizeImageView extends ImageView {
    public PrizeImageView(Context context) {
        super(context);
        init();
    }

    public PrizeImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PrizeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PrizeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void loadImage(String path) {
        AsyncLoad t = new AsyncLoad();
        t.execute(path);
    }


    @Override
    protected void drawableStateChanged() {
        setFilter();

        super.drawableStateChanged();
    }


    private final RectF roundRect = new RectF();
    private float rect_adius = 3.5f;
    private final Paint maskPaint = new Paint();
    private final Paint zonePaint = new Paint();

    private void init() {
        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //
        zonePaint.setAntiAlias(true);
        zonePaint.setColor(Color.WHITE);
        //
        float density = getResources().getDisplayMetrics().density;
        rect_adius = rect_adius * density;
    }



    public void setRectAdius(float adius) {
        rect_adius = adius;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int w = getWidth();
        int h = getHeight();
        roundRect.set(0, 0, w, h);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawRoundRect(roundRect, rect_adius, rect_adius, zonePaint);
        //
        canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        canvas.restore();
    }


    /**
     * �����˾�
     */
    private void setFilter() {

        //�Ȼ�ȡ���õ�srcͼƬ
        Drawable drawable = this.getDrawable();
        if (drawable != null) {
            //�����˾�
            if (isPressed()) {
                drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            } else {
                drawable.setColorFilter(null);
            }
        }
        this.invalidate();
    }


    static void drawSelect(Canvas canvas, View icon, int w, int h) {
        if (icon.isSelected()) {
            Resources res = icon.getContext().getResources();
            Drawable unreadBgNinePatchDrawable = (Drawable) res
                    .getDrawable(R.drawable.in_use);
            int unreadBgWidth = w;
            int unreadBgHeight = h;
            Rect unreadBgBounds = new Rect(0, 0, unreadBgWidth, unreadBgHeight);
            unreadBgNinePatchDrawable.setBounds(unreadBgBounds);

            int unreadMarginTop = unreadBgWidth/4;
            int unreadMarginRight = unreadBgWidth/4;
            int unreadBgPosX = icon.getScrollX() + icon.getWidth()
                    - unreadBgWidth - unreadMarginRight;
            int unreadBgPosY = icon.getScrollY() + unreadMarginTop;

            canvas.save();
            canvas.translate(unreadBgPosX, unreadBgPosY);

            unreadBgNinePatchDrawable.draw(canvas);
            canvas.restore();
        }
    }


    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        Drawable unreadBgNinePatchDrawable = (Drawable) getContext()
                .getDrawable(R.drawable.in_use);
        int w = unreadBgNinePatchDrawable.getIntrinsicWidth();
        int h = unreadBgNinePatchDrawable.getIntrinsicHeight();
        drawSelect(c, this, w, h);
    }


    class AsyncLoad extends AsyncTask<String, Void, Bitmap> {


        public Bitmap coverToBitmap(String iconPreviewPath) {
            FileInputStream is = null;
            Bitmap bitmap1 = null;
            try {
                is = new FileInputStream(new File(iconPreviewPath));

                bitmap1 = BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != is)
                        is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return bitmap1;
        }


        @Override
        protected Bitmap doInBackground(String... path) {
            String resulut = path[0];
            Bitmap b = coverToBitmap(resulut);
            return b;
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result != null) {
                setImageBitmap(result);
            }
        }

    }


}
