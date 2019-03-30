
package com.android.launcher3;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.format.Time;
import android.util.AttributeSet;
import android.widget.RemoteViews.RemoteView;

import com.android.launcher3.dragndrop.DragView;
import com.android.launcher3.theme.tools.ThemeIconTool;
import com.android.launcher3.views.DoubleShadowBubbleTextView;

import java.util.Calendar;

//import com.android.launcher3.FastBitmapDrawable;
//import com.android.launcher3.graphics.DrawableFactory;

/**
 * This widget display an analogic clock with two hands for hours and minutes.
 */

@RemoteView
public class PrizeDragView extends DragView {
    private int mDatas;
    private int mYearDay;
    private int mPrevDatas = -1;
    private int mWeeks;
    private boolean isCalendarResourceOK;
    private MyCount mCounter;

    private Time mCalendar;

    private Bitmap mHourHand;
    private Bitmap mMinuteHand;
    private Bitmap mSecondHand;
    private Bitmap mDial;
    private Bitmap mDecade;//十位
    private Bitmap mUnit;//个位
    private Bitmap mCalendarBg;
    private Bitmap mBWeek;

    /**
     * 时钟信息
     */
    private ComponentName mDeskClockComponentName = new ComponentName(
            "com.android.deskclock", "com.android.deskclock.DeskClock");
    private ComponentName mCalendarComponentName = new ComponentName("com.android.calendar",
            "com.android.calendar.AllInOneActivity");

    private ComponentName mCalendarComponentName1 = new ComponentName("com.google.android.calendar",
            "com.android.calendar.AllInOneActivity");

    /**
     * 指定为时钟icon
     */
    private boolean isDeskClockView = false;
    private boolean isCalendarView = false;
    private boolean isResourceOK = false;

    private boolean mAttached;

    private final Handler mHandler = new Handler();
    private float mMinutes;
    private float mHour;
    private boolean mChanged;

    Context mContext;

    private RectF dialRect = new RectF();
    private RectF hourRect = new RectF();
    private RectF minRect = new RectF();
    private RectF secRect = new RectF();


    public PrizeDragView(Launcher mLauncher, Bitmap b, int registrationX, int registrationY, float initialDragViewScale, float dragViewScaleOnDrop, float scaleDps) {
        super(mLauncher, b, registrationX, registrationY, initialDragViewScale, dragViewScaleOnDrop, scaleDps);
    }

    public void onTick() {

        if (isDeskClockView) {
            mCalendar.setToNow();

            int hour = mCalendar.hour;
            int minute = mCalendar.minute;
            int second = mCalendar.second;
            Calendar Cld = Calendar.getInstance();
            int mi = Cld.get(Calendar.MILLISECOND) + 1000 * second;

            onTimeChanged();
            mSecond = 0.006f * mi;
            mSeconds = true;
            PrizeDragView.this.invalidate();
        }

        if (isCalendarView) {
            mCalendar.setToNow();
            int data = mCalendar.monthDay;

            int yearDay = mCalendar.yearDay;
            if (mDatas != data || (mDatas == data && yearDay != mYearDay)) {
                mDatas = data;
                mYearDay = yearDay;
                PrizeDragView.this.invalidate();
            }
        }


    }

    public void clear() {
        if (mDial != null) {
//			mDial.recycle();
            mDial = null;
        }
        if (mHourHand != null) {
//			mHourHand.recycle();
            mHourHand = null;
        }
        if (mMinuteHand != null) {
//			mMinuteHand.recycle();
            mMinuteHand = null;
        }
        if (mSecondHand != null) {
//			mSecondHand.recycle();
            mSecondHand = null;
        }

        System.gc();
    }

    public void updateDeskcomponent() {
        clear();
        updateClockBg();

        isResourceOK = mHourHand != null && mMinuteHand != null
                && mDial != null/* && mSecondHand != null*/;
        isCalendarResourceOK = mDecade != null && mUnit != null
                && mCalendarBg != null && mBWeek != null;
    }

    public void updateClockBg() {
        Calendar c = Calendar.getInstance();
        int h = c.get(Calendar.HOUR_OF_DAY);
        mDial = ThemeIconTool.getInstance().getDeskIcon(getContext(), "clock");
        final Drawable bg = ThemeIconTool.getInstance().getDeskIconToDrawble(getContext(), "bg_clock");

        mHourHand = ThemeIconTool.getInstance().getDeskIcon(getContext(), "hour");

        mMinuteHand = ThemeIconTool.getInstance().getDeskIcon(getContext(), "min");

        mSecondHand = ThemeIconTool.getInstance().getDeskIcon(getContext(), "sec");

    }


    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    /**
     * 判断此app是否为无效的app
     *
     * @param pm
     * @param cn
     * @return
     */
    private boolean isValidPackageComponent(PackageManager pm, ComponentName cn) {
        if (cn == null) {
            return false;
        }

        try {
            // Skip if the application is disabled
            PackageInfo pi = pm.getPackageInfo(cn.getPackageName(), 0);
            if (!pi.applicationInfo.enabled) {
                return false;
            }

            // Check the activity
            return (pm.getActivityInfo(cn, 0) != null);
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /*
     * (non-Javadoc) 1.监听时间注册 2.只监听deskClock 应用icon
     *
     * @see com.android.launcher3.BubbleTextView#onAttachedToWindow()
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ItemInfo info = mInfo;
        if (info == null || info.getIntent() == null) {
            return;
        }
        ComponentName cn = info.getIntent().getComponent();
        PackageManager pm = this.getContext().getPackageManager();
        boolean isVaild = isValidPackageComponent(pm, cn);
        if (cn != null && isVaild
                && cn.equals(mDeskClockComponentName)) {

            isDeskClockView = true;

            mCalendar = new Time();
            mCounter = new MyCount(10000, 1000);

            updateDeskcomponent();
            if (!mAttached) {
                mAttached = true;
            }
            mCalendar = new Time();
            onTimeChanged();
            mCounter.start();
        } else {
            isDeskClockView = false;
        }
        if (cn != null && cn.equals(mCalendarComponentName) || cn != null && cn.equals(mCalendarComponentName1)) {
            isCalendarView = true;
        } else {
            isCalendarView = false;
        }
        if(isCalendarView) {

            if (!mAttached) {
                mAttached = true;
            }
            mCalendar = new Time();

            mChanged = true;
            mCounter = new MyCount(10000, 1000);
            mCounter.start();
            mCalendarBg = ThemeIconTool.getInstance().getCalendarIcon(getContext(), "calendar");
        }
    }

    /*
     * (non-Javadoc) 注销
     *
     * @see com.android.launcher3.BubbleTextView#onDetachedFromWindow()
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (isDeskClockView) {

            if (mAttached) {
                mCounter.cancel();
                mAttached = false;
            }
        }

        if (isCalendarView) {
            {

                if (mAttached) {
                    mCounter.cancel();
                    mAttached = false;
                }
            }

        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (isDeskClockView) {
            mChanged = true;
        }
        if(isCalendarView) {

            mChanged = true;
        }
    }

    public Bitmap getData(int decade) {
        Bitmap b = ThemeIconTool.getInstance().getCalendarIcon(this.getContext(), String.valueOf(decade));

        return b;
    }

    public void OnDrawCalendarView(Canvas canvas) {

        ItemInfo info = mInfo;
        if (info == null || info.getIntent() == null) {
            return;
        }
        ComponentName cn = info.getIntent().getComponent();
        if (cn != null && cn.equals(mCalendarComponentName) || cn != null && cn.equals(mCalendarComponentName1)) {
            isCalendarView = true;
        }
        Paint p = new Paint();

        p.setAntiAlias(true);
        p.setDither(true);
        p.setFilterBitmap(true);

        Launcher l = (Launcher) getContext();
        DeviceProfile grid = l.getDeviceProfile();

        if (isCalendarView) {

            boolean changed = mChanged;
            if (changed) {
                mChanged = false;
            }

            int decade = mDatas / 10;
            int unit = mDatas % 10;

            mDecade = getData(decade);
            mUnit = getData(unit);
            int count = 1;
            if (decade > 0) {
                count = 2;
            }


            boolean redeay = mUnit != null && mDecade != null && mCalendarBg != null;
            if (!redeay) {
                return;
            }
        int availableWidth = grid.iconSizePx;
        int availableHeight = grid.iconSizePx;

        int w = mCalendarBg.getWidth();
        int h = mCalendarBg.getHeight();
        float scaled=1f;
        if (availableWidth < w || availableHeight < h) {
            scaled= Math.min((float) availableWidth / (float) w,
                    (float) availableHeight / (float) h);
        }
            int childWidth = (int) (mUnit.getWidth());
            if (count > 1) {
                childWidth = mDecade.getWidth();
            }
            int ps = 0;//(int) (-childWidth * 0.7f);/*日历双数字间距*/
            int vw = this.getWidth();
            int vh = this.getHeight();

            float x = this.getWidth() / 2f;
            float y = this.getHeight() / 2;

            int posX = this.getScrollX();
            int posY = this.getScrollY();
          /*  int w = mBWeek.getWidth();
            int h = mBWeek.getHeight();*/

            int left1 = childWidth * count + ps * (count - 1);
            int decadeLeft = (int) (vw / 2f - left1 / 2f) + 0
                    * (childWidth + ps);
            int decadeTop = (int) y - mDecade.getHeight() / 2;
            int unitLeft = (int) (vw / 2f - left1 / 2f) + 1
                    * (childWidth + ps);
            int unittop = (int) y - mDecade.getHeight() / 2;
            Rect decadeRect = new Rect(decadeLeft, decadeTop, decadeLeft
                    + childWidth, mDecade.getHeight() + decadeTop);
            Rect unitRect = new Rect(decadeLeft, unittop, decadeLeft
                    + childWidth, mUnit.getHeight() + unittop);


         /*   if (childWidth < w || childWidth < h) {
                scaled = true;
                float scale = Math.min((float) childWidth / (float) w,
                        (float) childWidth / (float) h);
                canvas.save();
                canvas.scale(scale, scale, x, y);
            }*/

            canvas.save();
            canvas.translate(posX, posY);
            if (mCalendarBg != null) {
                int bw = mCalendarBg.getWidth();
                int bh = mCalendarBg.getHeight();

                RectF bgRect = new RectF();
                bgRect.set(x - (bw / 2f), y-bh/2, x + (bw / 2f), y-bh/2
                        + bh);

                canvas.scale(scaled, scaled, x, y);
                canvas.drawBitmap(mCalendarBg, null, bgRect, null);
              /*  final Drawable bg = getResources().getDrawable(R.drawable.calendars);
                setBg(bg);*/
            }

            if(mDatas==0) {
                return;
            }
            if (count > 1) {
                decadeRect = new Rect(decadeLeft, decadeTop, decadeLeft
                        + childWidth, mDecade.getHeight() + decadeTop);
                unitRect = new Rect(unitLeft, unittop, unitLeft + childWidth,
                        mUnit.getHeight() + unittop);
                canvas.drawBitmap(mDecade, null, decadeRect, p);
            }
            canvas.drawBitmap(mUnit, null, unitRect, p);
            if (mBWeek != null) {

                float y1 = this.getHeight() / 2f + this.getPaddingTop() / 2f;
                int top1 = (int) (y1 - mCalendarBg.getHeight() / 2);
                int left = (this.getWidth() - mBWeek.getWidth()) / 2;
                Rect weekRect = new Rect(left, top1,
                        left + mBWeek.getWidth(), top1 +
                        mBWeek.getHeight());
                canvas.drawBitmap(mBWeek, null, weekRect, p);
            }

            canvas.restore();
        }
        //add by zhouerlong weatcher add
        //add by zhouerlong prizeTheme add
    }

    public void OnDrawDeskClockView(Canvas canvas) {
        ItemInfo info = mInfo;
        if (info == null || info.getIntent() == null) {
            return;
        }
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        ComponentName cn = info.getIntent().getComponent();
        PackageManager pm = this.getContext().getPackageManager();
        Paint ps = new Paint();
        ps.setAntiAlias(true);
        ps.setDither(true);
        ps.setFilterBitmap(true);
     	if (cn != null && cn.equals(mDeskClockComponentName)) {
     		isDeskClockView = true;
          //   counter.start();
         }
        if (isDeskClockView && isResourceOK) {
            boolean changed = mChanged;
            if (changed) {
                mChanged = false;
            }

            boolean redeay = mHourHand != null && mMinuteHand != null
                    && mDial != null /*&& mSecondHand != null*/;
            if (!redeay) {
                return;
            }


            boolean seconds = mSeconds;
          /*  if (seconds) {
                mSeconds = false;
            }*/

            Launcher l = (Launcher) getContext();
            DeviceProfile grid = l.getDeviceProfile();

            int availableWidth = grid.iconSizePx;
            int availableHeight = grid.iconSizePx;

            float x = this.getWidth() / 2f;
            float y = this.getHeight() / 2f;
        /*	if(Launcher.scale == 3){
                 x = this.getWidth() / 2f -1f;
				 y = this.getCompoundPaddingTop() / 2f + this.getPaddingTop() / 2f+4f;
        }*/

            int posX = this.getScrollX();
            int posY = this.getScrollY();

            final Bitmap dial = mDial;
            if (dial == null) {
                return;
            }
            int w = dial.getWidth();
            int h = dial.getHeight();

            boolean scaled = false;
            float scale = 1f;
            if (availableWidth < w || availableHeight < h) {
                scaled = true;
                scale = Math.min((float) availableWidth / (float) w,
                        (float) availableHeight / (float) h);
            }

            if (changed) {
                dialRect.set(x - (w / 2f), y - (h / 2f), x + (w / 2f), y
                        + (h / 2f));
            }
            canvas.save();
            canvas.translate(posX, posY);
            canvas.scale(scale, scale, x, y);
            if (dialRect != null)
                canvas.drawBitmap(dial, null, dialRect, ps);


//-------------------------------------------------------------------------------------------------
            canvas.save();

            canvas.rotate(mSecond, x, y);
            if (mSecondHand == null || mSecondHand.isRecycled()) {
//				return;
            } else {
                if (seconds) {
                    w = mSecondHand.getWidth();
                    h = mSecondHand.getHeight();
                    secRect.set(x - (w / 2f), y - (h / 2f), x + (w / 2f), y
                            + (h / 2f));

                }
                canvas.scale(scale, scale, x, y);
                canvas.drawBitmap(mSecondHand, null, secRect, ps);
                canvas.restore();

//-------------------------------------------------------------------------------------------------
                canvas.save();
                canvas.scale(scale, scale, x, y);
                canvas.rotate(mHour / 12.0f * 360.0f, x, y);
            }
            final Bitmap hourHand = mHourHand;
            if (hourHand == null || hourHand.isRecycled()) {
                return;
            }
            if (changed) {
                w = hourHand.getWidth();
                h = hourHand.getHeight();
                hourRect.set(x - (w / 2f), y - (h / 2f), x + (w / 2f), y
                        + (h / 2f));

                updateClockBg();
            }
            canvas.drawBitmap(hourHand, null, hourRect, ps);
            canvas.restore();
//-------------------------------------------------------------------------------------------------

            canvas.save();
            canvas.scale(scale, scale, x, y);
            canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);
            final Bitmap minuteHand = mMinuteHand;
            if (minuteHand == null || mMinuteHand.isRecycled()) {
                return;
            }
            if (changed) {
                w = minuteHand.getWidth();
                h = minuteHand.getHeight();
                minRect.set(x - (w / 2f), y - (h / 2f), x + (w / 2f), y
                        + (h / 2f));
            }
            canvas.drawBitmap(minuteHand, null, minRect, ps);
            canvas.restore();
//-------------------------------------------------------------------------------------------------


        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        OnDrawDeskClockView(canvas);
        OnDrawCalendarView(canvas);

    }

    boolean mSeconds = false;
    float mSecond = 0;

    private void onTimeChanged() {
        mCalendar.setToNow();

        int hour = mCalendar.hour;
        int minute = mCalendar.minute;
        int second = mCalendar.second;
        mDatas = mCalendar.monthDay;
        mWeeks = mCalendar.weekDay;

        mMinutes = minute + second / 60.0f;
        mHour = hour + mMinutes / 60.0f;
        mChanged = true;
    }


    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            if(isDeskClockView) {
                mCounter.start();
            }
            if(isCalendarView) {
                mCounter.start();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (isDeskClockView) {
                PrizeDragView.this.onTick();
            }
            if (isCalendarView) {
                PrizeDragView.this.onTick();
            }
        }
    }
}
