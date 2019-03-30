package com.android.launcher3.theme.tools;

import android.content.ComponentName;
import android.util.Xml;

import com.android.launcher3.Launcher;
import com.android.launcher3.theme.bean.OverIconBean;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

//add by zhouerlong prizeTheme add
//add by zhouerlong prizeTheme add

public class DefaultConfig {
    public static String default_config = "/system/media/config/default_config/";//默认设置配置文件

    public static List<String> sOverIconpkgs = new ArrayList<>();
    public static List<String> sOverIconclss = new ArrayList<>();
    //add by zhouerlong prizeTheme add
    public static List<OverIconBean> sIconBeans = new ArrayList<>();
    //add by zhouerlong prizeTheme add
    public static List<String> sOverIcons = new ArrayList<>();


    public static LinkedHashMap<String, String> findOverIcons(String path) {
        String configPath = path;
        File configFile = new File(configPath);
        if (!configFile.exists()) {
            return null;
        }
        InputStream is = null;
        LinkedHashMap<String, String> configs = new LinkedHashMap<>();
        List<String> arrays = null;
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
                        if (xpp.getName().equals("string-array")) {
                            if (xpp.getAttributeValue(0).equals("overlay_icon_package")) {
                                arrays = sOverIconpkgs;
                            } else if (xpp.getAttributeValue(0).equals("overlay_icon_class")) {
                                arrays = sOverIconclss;
                            } else if (xpp.getAttributeValue(0).equals("overlay_icon_image")) {
                                arrays = sOverIcons;
                            }

                        } else if (xpp.getName().equals("item")) {
                            xpp.next();
                            try {

                                String item = xpp.getText();
                                String[] items = item.split(";");
                                //add by zhouerlong prizeTheme add
                                OverIconBean over = new OverIconBean();
                                over.pkg = items[0];
                                over.className = items[1];
                                over.name = items[2];
                                sIconBeans.add(over);
                                //add by zhouerlong prizeTheme add

                                sOverIconpkgs.add(items[0]);
                                sOverIconclss.add(items[0] + ";" + items[1]);
                                sOverIcons.add(items[2]);
                            } catch (Exception e) {
                                // TODO: handle exception
                            }


                        }
                        break;
                    // 判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
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

        return configs;
    }


}
