package com.android.launcher3;

import android.content.Context;

import com.android.launcher3.theme.table.UsageTable;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

public class UsedAppsTool {

    private List<AppInfo> mApps = null;

    public UsedAppsTool(List<AppInfo> apps, Context c) {
        mApps = apps;
    }


    public AppInfo getApp(String pkg) {
        for (AppInfo info : mApps) {
            // Add the section to the cache
            if (info.componentName != null && info.componentName.getPackageName().equals(pkg)) {
                return info;
            }
        }
        return null;
    }


    public void saveUsage(UsageTable t) {
        try {
            find();
            UsageTable ut = LauncherAppState.getDbManager().selector(UsageTable.class).where("pkg", "=", t.pkg).findFirst();
            if (ut == null) {
                LauncherAppState.getDbManager().save(t);
            } else {
                ut.best += t.best;
                LauncherAppState.getDbManager().saveOrUpdate(ut);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public List<UsageTable> find() {
        try {
            return LauncherAppState.getDbManager().selector(UsageTable.class).orderBy("best", true).limit(5).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;

    }


    public abstract static class ItemInfoMatcher {

        public abstract boolean matche(ItemInfo info, String pkg);

        public static ItemInfoMatcher ofPackage(
                final String packageName) {
            return new ItemInfoMatcher() {
                public boolean matche(ItemInfo info, String pkg) {
                    return packageName.equals(pkg);
                }
            };
        }


        public AppInfo filterItemInfos(List<AppInfo> apps) {

            AppInfo fts;
            for (AppInfo info : apps) {
                if (info != null && info.getIntent() != null && info.getIntent().getComponent() != null && matche(info, info.getIntent().getComponent().getPackageName())) {
                    return info;
                }
            }
            return null;
        }

    }

    public List<AppInfo> ft(List<UsageTable> list) {
        List<AppInfo> result = new ArrayList<>();
        if (mApps != null && list != null) {
            for (UsageTable u : list) {
                ItemInfoMatcher match = ItemInfoMatcher.ofPackage(u.pkg);
                AppInfo temps = match.filterItemInfos(mApps);
                mApps.remove(temps);
                result.add(temps);
            }
        }
        return result;
    }


}
