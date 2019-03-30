package com.android.launcher3.theme.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Xml;

import com.android.launcher3.LauncherAppState;
import com.android.launcher3.theme.db.DbTools;
import com.android.launcher3.theme.table.ThemeTable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.theme;

/**
 * Created by prize on 2018/1/23.
 */

public class FindTools {
    Context mContext;

    public static String themePath = "system/media/config/theme/";//内置主题路径
    private LinkedHashMap<String, String> mDefault_config = null;

    public FindTools(Context mContext) {
        this.mContext = mContext;
    }


    public static boolean exists() {

        File t = new File(themePath);
        return LauncherAppState.isDisableAllApps()?t.exists():false;
    }

    public void loadTheme(Runnable theme) {

        boolean isloaded = PrefTools.getBoolean("theme",false,mContext);
            if(!exists()) {
                return;
            }
        if (!isloaded) {
            //add by zhouerlong prizeTheme add
            ThemeTask themeTask = new ThemeTask();
            themeTask.setThemeRunnable(theme);
            themeTask.execute(themePath);
            PrefTools.putBoolean("theme",true,mContext);
        }

    }


    public List<ThemeTable> findDefaultTheme(String path) {// 获取内置主题
        List<ThemeTable> themesList = new ArrayList<ThemeTable>();
        File themesFile = new File(path);

        if (!themesFile.exists() || !themesFile.isDirectory()) {// 不存在内置主题
            return null;
        }

        // 通过配置文件去读取对应的所有主题
        LinkedHashMap<String, String> namesAndIds = findConfig(path);
        if (namesAndIds == null || namesAndIds.size() == 0) {
            return null;// 没有配置内置主题
        }

        Iterator iter = namesAndIds.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();// 主题id
            Object val = entry.getValue();// 名字name
            /**prize add by bianxinhao 2017年3月23日15:21:35 begin*/
            if (parseToTheme(
                    path + String.valueOf(key),
                    String.valueOf(val)) == null) continue;
            /**prize add by bianxinhao 2017年3月23日15:21:35 end*/
            themesList.add(parseToTheme(
                    path + String.valueOf(key),
                    String.valueOf(val)));
        }
        return themesList;

    }


    private ThemeTable parseToTheme(String fileString, String name) {// 解析内置主题文件夹
        File themeFile = new File(fileString);

        if (!themeFile.exists() || !themeFile.isDirectory()) {// 不存在内置主题
            return null;
        }

        String[] filesName = themeFile.list();
        ThemeTable theme = new ThemeTable();// 新建一个主题对象
        HashMap<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < filesName.length; i++) {
            if (filesName[i].endsWith("zip")) {// 是主题包
                theme.themePath = fileString + "/" + filesName[i];
            } else if (filesName[i].contains("icon")) {// 小预览图
                theme.iconPath = fileString + "/" + filesName[i];
            } else if (filesName[i].contains("preview0")) {// 主题详情里的预览图
                map.put("preview0", fileString + "/" + filesName[i]);
            } else if (filesName[i].contains("preview1")) {// 主题详情里的预览图
                map.put("preview1", fileString + "/" + filesName[i]);
            } else if (filesName[i].contains("preview2")) {// 主题详情里的预览图
                map.put("preview2", fileString + "/" + filesName[i]);
            } else if (filesName[i].endsWith(".jar")) {
                theme.themePath = fileString + "/" + filesName[i];
            }
        }
        String strId = fileString.substring(fileString.lastIndexOf("/") + 1);
        theme.themeId = strId;
        List<String> themePreviews = new ArrayList<String>();
        if (null != map.get("preview0")) {
            themePreviews.add(map.get("preview0"));
        }
        if (null != map.get("preview1")) {
            themePreviews.add(map.get("preview1"));
        }
        if (null != map.get("preview2")) {
            themePreviews.add(map.get("preview2"));
        }
        theme.name = name;// 主题名字
        return theme;
    }


    private LinkedHashMap<String, String> findConfig(String path) {
        String configPath = path + "config.xml";
        File configFile = new File(configPath);
        if (!configFile.exists()) {
            return null;
        }
        InputStream is = null;
        String name = null;
        String id = null;
        LinkedHashMap<String, String> namesAndIds = new LinkedHashMap<>();
        try {
            is = new FileInputStream(configFile);
            XmlPullParser xpp = Xml.newPullParser();
            xpp.setInput(is, "UTF-8");
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    // 判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    // 判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if (xpp.getName().equals("item-info")) {
                            name = null;
                            id = null;
                        } else if (xpp.getName().equals("name")) {
                            eventType = xpp.next();
                            name = xpp.getText();
                            if (null != name && name.split(";").length > 1) {
                                name = name.split(";")[0];
                            }

                        } else if (xpp.getName().equals("id")) {
                            eventType = xpp.next();
                            id = xpp.getText();
                        }
                        break;
                    // 判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        if (xpp.getName().equals("item-info")) {
                            if (id != null) {
                                if (name == null) {
                                    name = "";
                                }
                                namesAndIds.put(id, name);
                            }
                        }
                        break;
                }
                // 进入下一个元素并触发相应事件
                eventType = xpp.next();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return namesAndIds;
    }


    class ThemeTask extends AsyncTask<String, Void, Void> {

        private Runnable themeRunnable;

        public Runnable getThemeRunnable() {
            return themeRunnable;
        }

        public void setThemeRunnable(Runnable themeRunnable) {
            this.themeRunnable = themeRunnable;
        }

        @Override
        protected Void doInBackground(String... params) {

            List<ThemeTable> mdefaultList = findDefaultTheme(params[0]);
            List<ThemeTable> tbs = new ArrayList();
            if (mdefaultList != null) {
                try {
/*prize-default theme-YZHD-20180316-begin*/
					//mdefaultList.get(0).isSelected=1;
					   for(ThemeTable t:mdefaultList) {
                        if(t.themePath.equals(ThemeIconTool.default_thme)) {
                            t.isSelected=1;
                        }else {
                            t.isSelected=0;
                        }
                    }
/*prize-default theme-YZHD-20180316-end*/
                    DbTools.addOrUpdate(mdefaultList);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            if(themeRunnable!=null) {
                themeRunnable.run();
            }
            super.onPostExecute(result);
        }

    }

}
