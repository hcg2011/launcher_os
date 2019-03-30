package com.android.launcher3.theme.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by prize on 2018/1/23.
 */

public class PrefTools {
    static String  themePref="themePref";
    public static String getString(String key, String defalut, Context context) {

        SharedPreferences sp = context.getSharedPreferences(
                themePref, Context.MODE_PRIVATE);
        return  sp.getString(key, defalut);
    }


    public static boolean getBoolean(String key,boolean defalut,Context context) {

        SharedPreferences sp = context.getSharedPreferences(
                themePref, Context.MODE_PRIVATE);
        return  sp.getBoolean(key, defalut);
    }

    public static void putString(String key,String value,Context context) {

        SharedPreferences sp = context.getSharedPreferences(
                themePref, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static void putBoolean(String key,boolean value,Context context) {

        SharedPreferences sp = context.getSharedPreferences(
                themePref, Context.MODE_PRIVATE);
				
//prize add by zhouerlong 20190116
        sp.edit().putBoolean(key, value).commit();
//prize add by zhouerlong 20190116
    }
}
