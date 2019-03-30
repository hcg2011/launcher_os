package com.android.launcher3.theme.db;

import android.content.pm.LauncherApps;

import com.android.launcher3.LauncherAppState;
import com.android.launcher3.theme.table.ThemeTable;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by prize on 2018/1/23.
 */

public class DbTools {


   static  public void addOrUpdate(List<ThemeTable> entry) {
        try {
            LauncherAppState.getDbManager().delete(ThemeTable.class);
            LauncherAppState.getDbManager().save(entry);

        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    static public void updateDb(final ThemeTable t) {
        t.isSelected=1;
        ThemeTable src = new ThemeTable();
        src.isSelected=0;
        WhereBuilder b = WhereBuilder.b("isSelected", "=", 1);
        try {
            LauncherAppState.getDbManager().update(ThemeTable.class,b,new KeyValue("isSelected",0));
            LauncherAppState.getDbManager().update(t,"isSelected");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public static List<ThemeTable> loadTheme() {
        List<ThemeTable> themes = null;
        try {
            themes = LauncherAppState.getDbManager().findAll(ThemeTable.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return themes;
    }


}
