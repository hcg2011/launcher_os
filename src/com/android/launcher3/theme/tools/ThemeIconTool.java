package com.android.launcher3.theme.tools;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

		//prize add by zhouerlong 20180521
import com.android.launcher3.FastBitmapDrawable;
import com.android.launcher3.LauncherAppState;
		//prize add by zhouerlong 20180521
import com.android.launcher3.R;
import com.android.launcher3.theme.bean.OverIconBean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import com.android.launcher3.Utilities;/*prize-default theme-YZHD-20180316-begin*/
/**
 * Created by prize on 2018/1/24.
 */

public class ThemeIconTool {

    private List<String> mPkgsName;
    private List<OverIconBean> mOvers;

    private List<String> mClassName;

    private List<String> mIconsName;
    private Bitmap mBottom;
    public Resources mResources;
	/*prize-default theme-YZHD-20180316-begin*/
//    public static String default_thme = "/sdcard/config/theme/default/default.jar";
	public static String default_thme = Utilities.getSystemProperty("ro.prize_default_theme_path","system/media/config/theme/default/default.jar");
	/*prize-default theme-YZHD-20180316-end*/
    private Bitmap mMask;

		//prize add by zhouerlong 20180521
    public static ThemeIconTool mInstance;

    public static ThemeIconTool getInstance() {
        if (mInstance == null) {
            mInstance = new ThemeIconTool();
        }
        return mInstance;
    }
		//prize add by zhouerlong 20180521


    public Resources getResourse(Context context, String themePath) {
        Resources s = null;
        try {
            AssetManager asm = AssetManager.class.newInstance();
            AssetManager.class.getMethod("addAssetPath", String.class).invoke(asm, themePath);
            Resources res = context.getResources();
            s = new Resources(asm, res.getDisplayMetrics(), res.getConfiguration());
            SharedPreferences sp = context.getSharedPreferences("CalendarIcon", Context.MODE_PRIVATE);
            sp.edit().putString("last", themePath).commit();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return s;
    }

    static public Bitmap drawableToBitmap(Drawable drawable) {
		//prize add by huhuan,swipe crash, 20181029-start
		if(drawable == null){
			return null;
		}
		//prize add by huhuan,swipe crash, 20181029-end
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
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


	//add by zhouerlong 20181120

    public Drawable getGoogleIcon(Context context,Drawable icon,ComponentName com) {
        String pkg = com.getPackageName();
        String cls = com.getClassName();
        //
        if(pkg!=null&&pkg.equals("com.android.vending")&&cls!=null&&cls.equals("com.android.vending.AssetBrowserActivity")) {
            icon = context.getDrawable(R.drawable.store);
        }
        //
        if(pkg!=null&&pkg.equals("com.google.android.apps.docs")&&cls!=null&&cls.equals("com.google.android.apps.docs.app.NewMainProxyActivity")) {
            icon = context.getDrawable(R.drawable.yp);
        }
        //
        if(pkg!=null&&pkg.equals("com.google.android.apps.maps")&&cls!=null&&cls.equals("com.google.android.maps.MapsActivity")) {
            icon = context.getDrawable(R.drawable.map);
        }
        //
        if(pkg!=null&&pkg.equals("com.google.android.apps.messaging")&&cls!=null&&cls.equals("com.google.android.apps.messaging.ui.ConversationListActivity")) {
            icon = context.getDrawable(R.drawable.mms_google);
        }
        //
        if(pkg!=null&&pkg.equals("com.google.android.apps.photos")&&cls!=null&&cls.equals("com.google.android.apps.photos.home.HomeActivity")) {
            icon = context.getDrawable(R.drawable.pt);
        }
        //Duo
        if(pkg!=null&&pkg.equals("com.google.android.apps.tachyon")&&cls!=null&&cls.equals("com.google.android.apps.tachyon.MainActivity")) {
            icon = context.getDrawable(R.drawable.duo);
        }
        //google
        if(pkg!=null&&pkg.equals("com.google.android.calendar")&&cls!=null&&cls.equals("com.android.calendar.AllInOneActivity")) {
            icon = context.getDrawable(R.drawable.calendar);
        }


        //google
        if(pkg!=null&&pkg.equals("com.google.android.music")&&cls!=null&&cls.equals("com.android.music.activitymanagement.TopLevelActivity")) {
            icon = context.getDrawable(R.drawable.music_google);
        }


        //google
        if(pkg!=null&&pkg.equals("com.google.android.videos")&&cls!=null&&cls.equals("com.google.android.youtube.videos.EntryPoint")) {
            icon = context.getDrawable(R.drawable.video_google);
        }


        //google  Play
        if(pkg!=null&&pkg.equals("com.google.android.youtube")&&cls!=null&&cls.equals("com.google.android.youtube.app.honeycomb.Shell$HomeActivity")) {
            icon = context.getDrawable(R.drawable.youtube);
        }

        //Chrome

        if(pkg!=null&&pkg.equals("com.google.android.googlequicksearchbox")&&cls!=null&&cls.equals("com.google.android.googlequicksearchbox.SearchActivity")) {
            icon = context.getDrawable(R.drawable.google);
        }



        //Chrome

        if(pkg!=null&&pkg.equals("com.google.android.gm")&&cls!=null&&cls.equals("com.google.android.gm.ConversationListActivityGmail")) {
            icon = context.getDrawable(R.drawable.gmail);
        }

        if(pkg!=null&&pkg.equals("com.android.chrome")&&cls!=null&&cls.equals("com.google.android.apps.chrome.Main")) {
            icon = context.getDrawable(R.drawable.chrome);
        }
        return icon;
    }

    public Drawable bitmapToDrawable(Bitmap bmp) {
        return new BitmapDrawable(bmp);
    }
	
	//add by zhouerlong 20181120
    public Drawable getThemeIcon(ComponentName componentName, Drawable icon, Context context) {

        String pkg =componentName.getPackageName();

        if(!LauncherAppState.isDisableAllApps()) {
            return icon;
        }
        if(!FindTools.exists()) {
            return icon;
        }
        boolean isGoogle= pkg.contains("google")||pkg.contains("com.android.vending")|| pkg.contains("com.android.chrome");

        String path = getPath(context);
        String cls=componentName.getClassName();
       /* if(isGoogle&&!path.contains("default.jar")) {
            return icon;
        }*/
		//prize add by zhouerlong 20180521


	//add by zhouerlong 20181120
        icon = getGoogleIcon(context,icon,componentName);

        Bitmap b = null;
        try {
            b = drawableToBitmap(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
	//add by zhouerlong 20181120
        return bitmapToDrawable(getThemeIcon(componentName, b, context));
    }




    public Bitmap getThemeIcon(ComponentName componentName, Bitmap icon, Context context) {

        Bitmap bitmap = icon;
        if (context == null) {
            return null;
        }
		//prize add by zhouerlong 20180521
        String path = getPath(context);
		//prize add by zhouerlong 20180521
        if (!isInserticon(context, path, componentName)) {


            if (bitmap != null) {
                bitmap = ImageUtils.getMaskIcon(icon, mMask, mBottom);
            }
        } else {
            bitmap = getThemeIcon(context, path, componentName);
        }

        System.gc();

        return bitmap;
    }

    public void init() {

    }

    public boolean isInserticon(Context applicationContext, String themePath,
                                ComponentName comp) {

		/*if (!themePath.contains(FindDefaultResoures.DEFALUT_THEME_PATH)) {
            return false;
		}*/
        SharedPreferences sp = applicationContext.getSharedPreferences(
                "CalendarIcon", Context.MODE_PRIVATE);
        String lastThemePath = sp.getString("last", "");

        if (mResources == null) {
            mResources = getResourse(applicationContext, themePath);

            mBottom = getBottomIcon(applicationContext);
            mMask = getMaskIcon(applicationContext);
        }
        String iconName = getIconName(applicationContext, themePath, comp);
        InputStream instr = null;
        if (iconName != null) {
            try {
                if (mResources != null)
                    instr = mResources.getAssets().open(
                            "theme/icon/" + iconName + ".png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instr != null;
    }


		//prize add by zhouerlong 20180521
    public static  String getPath(Context context) {
        return PrefTools.getString("theme_path", default_thme, context);
    }
		//prize add by zhouerlong 20180521

    public static void setPath(Context context, String path) {
        PrefTools.putString("theme_path",path,context);
    }


    public String getIconName(Context applicationContext, String themePath, ComponentName comp) {

        mPkgsName = DefaultConfig.sOverIconpkgs;
        mClassName = DefaultConfig.sOverIconclss;
        mIconsName = DefaultConfig.sOverIcons;

        mOvers = DefaultConfig.sIconBeans;

        if (mResources == null) {
            mResources = getResourse(applicationContext, themePath);
            mBottom = getBottomIcon(applicationContext);
            mMask = getMaskIcon(applicationContext);

        }
        String iconName = null;
        String pkg;
        String cls;
        String coms = null;
		//add by zhouerlong 20180127
        if(comp==null) {
            return null;
        }
		//add by zhouerlong 20180127
        pkg = comp.getPackageName().toLowerCase();
        cls = comp.getClassName().toLowerCase();

        coms = pkg + ";" + cls;


        //add by zhouerlong update icon custom not custom

        boolean isContains = mClassName.contains(coms);
        if (isContains) {
            int i = mClassName.indexOf(coms);
            if (i <= 0) {
                i = mPkgsName.indexOf(pkg);
            }
            if (i >= 0) {
                iconName = mOvers.get(i).name;
            } else {
                Log.i("zhouerlong", "-----------pkg:::::" + pkg);
            }
            //add by zhouerlong update icon custom not custom
        }
        return iconName;
    }
    //prize add by huhuan,Get interface Icon,20181009-start
    public String getAppMainActivity(Context context,String packageName){

        Intent intent =new  Intent(Intent.ACTION_MAIN,null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setPackage(packageName);
        List<ResolveInfo> packageInfos = context.getApplicationContext().getPackageManager().queryIntentActivities(intent,0);
        if(packageInfos.size()==1){
            return packageInfos.get(0).activityInfo.name;
        }else {
            return packageName;
        }
     }
    //prize add by huhuan,Get interface Icon,20181009-end

    public Bitmap getThemeIcon(Context applicationContext, String themePath,
                               ComponentName comp) {

		/*if (!themePath.contains(FindDefaultResoures.DEFALUT_THEME_PATH)) {
            return null;
		}*/
        SharedPreferences sp = applicationContext.getSharedPreferences(
                "CalendarIcon", Context.MODE_PRIVATE);
        String lastThemePath = sp.getString("last", "");

        if (mResources == null) {
            mResources = getResourse(applicationContext, themePath);
            mBottom = getBottomIcon(applicationContext);
            mMask = getMaskIcon(applicationContext);
        }
        String iconName = getIconName(applicationContext, themePath, comp);
        InputStream instr = null;
        Bitmap rettemp = null;
        if (iconName != null) {
            try {
                if (mResources != null)
                    instr = mResources.getAssets().open(
                            "theme/icon/" + iconName + ".png");
                if (instr != null) {

                    rettemp = BitmapFactory.decodeStream(instr);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rettemp;

    }
    public Bitmap getFolderIcon(Context applicationContext) {
        SharedPreferences sp = applicationContext.getSharedPreferences("CalendarIcon", Context.MODE_PRIVATE);
        String lastThemePath = sp.getString("last", "");
        /*if(mResources == null || !themePath.equals(lastThemePath)){
            initResourse(applicationContext,themePath);
		}*/

        String themePath = PrefTools.getString("theme_path", default_thme, applicationContext);
        if (mResources == null) {
            mResources = getResourse(applicationContext, themePath);
            mBottom = getBottomIcon(applicationContext);
            mMask = getMaskIcon(applicationContext);
        }
        InputStream instr = null;
        Bitmap rettemp = null;
        try {
            if (mResources != null)
                instr = mResources.getAssets().open("theme/icon/icon_folder.png");
            if (instr != null) {
                rettemp = BitmapFactory.decodeStream(instr);
                instr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rettemp;
    }

    public Bitmap getBottomIcon(Context applicationContext) {
        SharedPreferences sp = applicationContext.getSharedPreferences("CalendarIcon", Context.MODE_PRIVATE);
        String lastThemePath = sp.getString("last", "");
        /*if(mResources == null || !themePath.equals(lastThemePath)){
            initResourse(applicationContext,themePath);
		}*/

        String themePath = PrefTools.getString("theme_path", default_thme, applicationContext);
        if (mResources == null) {
            mResources = getResourse(applicationContext, themePath);
            mBottom = getBottomIcon(applicationContext);
            mMask = getMaskIcon(applicationContext);
        }
        InputStream instr = null;
        Bitmap rettemp = null;
        try {
            if (mResources != null)
                instr = mResources.getAssets().open("theme/filter/bottom_layer.png");
            if (instr != null) {
                rettemp = BitmapFactory.decodeStream(instr);
                instr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rettemp;
    }


    public void setWallpaper(final Context c) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is = getWallpaper(c);
                if (is != null) {
                    android.app.WallpaperManager wallpaperManager = android.app.WallpaperManager
                            .getInstance(c);
                    try {
                        wallpaperManager.setStream(is);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }
        });
        t.start();
    }

    public InputStream getWallpaper(Context applicationContext) {
        SharedPreferences sp = applicationContext.getSharedPreferences("CalendarIcon", Context.MODE_PRIVATE);
        String lastThemePath = sp.getString("last", "");
        /*if(mResources == null || !themePath.equals(lastThemePath)){
            initResourse(applicationContext,themePath);
		}*/

        String themePath = PrefTools.getString("theme_path", default_thme, applicationContext);
        if (mResources == null) {
            mResources = getResourse(applicationContext, themePath);
            mBottom = getBottomIcon(applicationContext);
            mMask = getMaskIcon(applicationContext);
        }
        InputStream instr = null;
        Bitmap rettemp = null;
        try {
            if (mResources != null)
                instr = mResources.getAssets().open("theme/wallpaper/default_wallpaper"+".jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instr;
    }
    public Drawable getDeskIconToDrawble(Context applicationContext,String name) {
        Bitmap icon = getDeskIcon(applicationContext, name);
        if(icon ==null) {
            return null;
        }
        Drawable drawable = new FastBitmapDrawable(icon);
        return  drawable;
    }

    public Bitmap getDeskIcon(Context applicationContext,String name) {
        SharedPreferences sp = applicationContext.getSharedPreferences("CalendarIcon", Context.MODE_PRIVATE);
        String lastThemePath = sp.getString("last", "");
        /*if(mResources == null || !themePath.equals(lastThemePath)){
            initResourse(applicationContext,themePath);
		}*/

        //prize add by zhouerlong 20180521
        String themePath = getPath(applicationContext);
        //prize add by zhouerlong 20180521
        if (mResources == null) {
            mResources = getResourse(applicationContext, themePath);
            mBottom = getBottomIcon(applicationContext);
            mMask = getMaskIcon(applicationContext);
        }
        InputStream instr = null;
        Bitmap rettemp = null;
        try {
            if (mResources != null)
                instr = mResources.getAssets().open("theme/icon/dynamicicon/deskclock/"+name+".png");
            if (instr != null) {
                rettemp = BitmapFactory.decodeStream(instr);
                instr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rettemp;
    }


    public Bitmap getCalendarIcon(Context applicationContext,String iconName) {
        SharedPreferences sp = applicationContext.getSharedPreferences("CalendarIcon", Context.MODE_PRIVATE);
        String lastThemePath = sp.getString("last", "");
        /*if(mResources == null || !themePath.equals(lastThemePath)){
            initResourse(applicationContext,themePath);
		}*/

        //prize add by zhouerlong 20180521
        String themePath = getPath(applicationContext);
        //prize add by zhouerlong 20180521
        if (mResources == null) {
            mResources = getResourse(applicationContext, themePath);
            mBottom = getBottomIcon(applicationContext);
            mMask = getMaskIcon(applicationContext);
        }
        InputStream instr = null;
        Bitmap rettemp = null;
        try {
            if (mResources != null)

                instr = mResources.getAssets().open("theme/icon/dynamicicon/calendar/"+iconName+".png");            if (instr != null) {
                rettemp = BitmapFactory.decodeStream(instr);
                instr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rettemp;
    }


    public Bitmap getMaskIcon(Context applicationContext) {
        SharedPreferences sp = applicationContext.getSharedPreferences("CalendarIcon", Context.MODE_PRIVATE);
        String lastThemePath = sp.getString("last", "");
        /*if(mResources == null || !themePath.equals(lastThemePath)){
            initResourse(applicationContext,themePath);
		}*/

		//prize add by zhouerlong 20180521
        String themePath = getPath(applicationContext);
		//prize add by zhouerlong 20180521
        if (mResources == null) {
            mResources = getResourse(applicationContext, themePath);
            mBottom = getBottomIcon(applicationContext);
            mMask = getMaskIcon(applicationContext);
        }
        InputStream instr = null;
        Bitmap rettemp = null;
        try {
            if (mResources != null)
                instr = mResources.getAssets().open("theme/filter/mask.png");
            if (instr != null) {
                rettemp = BitmapFactory.decodeStream(instr);
                instr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rettemp;
    }
}
