package com.android.launcher3.theme.table;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 主题
 *
 * @author zhouerlong
 */
@SuppressWarnings("serial")
@Table(name = UsageTable.table_name)

public class UsageTable implements ITable, Serializable {

    public static final String table_name = "t_usage_table";

    /**
     * name
     */
    @Column(name = "pkg", isId = true)
    public String pkg;
    /**
     * 主题URl
     */
    @Column(name = "best")
    public int best;


    @Override
    public String getTableName() {
        return table_name;
    }
}
