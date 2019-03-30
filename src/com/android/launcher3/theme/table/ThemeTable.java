package com.android.launcher3.theme.table;

import java.io.Serializable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 主题
 *
 * @author zhouerlong
 */
@SuppressWarnings("serial")
@Table(name = ThemeTable.table_name)

public class ThemeTable implements ITable, Serializable {

    public static final String table_name = "t_theme_table";

    /**
     * id
     */
    @Column(name = "themeId", isId = true)
    public String themeId;
    /**
     * name
     */
    @Column(name = "name")
    public String name;
    /**
     * 主题URl
     */
    @Column(name = "themeUrl")
    public String themeUrl;
    /**
     * iconPath
     */
    @Column(name = "iconPath")
    public String iconPath;
    /**
     * preview Path
     */
    @Column(name = "previewPath")
    public String previewPath;
    /**
     * 主题路径
     */
    @Column(name = "themePath")
    public String themePath;
    /**
     * isSelected
     */
    @Column(name = "isSelected")
    public int isSelected;

    @Override
    public String getTableName() {
        return table_name;
    }
}
