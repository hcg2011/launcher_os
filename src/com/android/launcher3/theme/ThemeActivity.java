package com.android.launcher3.theme;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.launcher3.R;
import com.android.launcher3.theme.db.DbTools;
import com.android.launcher3.theme.table.ThemeTable;

import java.util.List;

/**
 * Created by prize on 2018/1/23.
 */

public class ThemeActivity extends Activity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.theme_activity);
        initViews();
        LoadThemeTask t = new LoadThemeTask();
        t.execute();

    }

    class LoadThemeTask extends AsyncTask<Void, Void, List<ThemeTable>> {

        @Override
        protected List<ThemeTable> doInBackground(Void... params) {
            return DbTools.loadTheme();
        }

        @Override
        protected void onPostExecute(List<ThemeTable> themeTables) {

            super.onPostExecute(themeTables);
            fillData(themeTables);
        }
    }

    public void fillData(List<ThemeTable> themes) {

        if (mRecyclerView != null) {
            // 初始化適配器
            ThemeAdapter mRecyclerAdapter = new ThemeAdapter(this, themes);
            // 设置适配器
            mRecyclerView.setAdapter(mRecyclerAdapter);
        }
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.demo_recyclerView);
        // 设置布局显示方式，这里我使用都是垂直方式——LinearLayoutManager.VERTICAL
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false));
        // 设置添加删除item的时候的动画效果
    }
}
