package com.android.launcher3;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RadioGroup;

import com.android.launcher3.theme.tools.PrefTools;
import com.android.launcher3.views.PrizeRadioGroup;

public class ScreenStyleActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_style);

        PrizeRadioGroup rg = this.findViewById(R.id.rg);
        int id = PrefTools.getBoolean("screen_style",false,ScreenStyleActivity.this)?R.id.r1:R.id.r2;
        rg.check(id);

        rg.setOnCheckedChangeListener(new PrizeRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(PrizeRadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.r1:

                        PrefTools.putBoolean("screen_style",true,ScreenStyleActivity.this);
                        LauncherAppState.setmIsDisableAllApps(true);
                        break;
                    case R.id.r2:
                        PrefTools.putBoolean("screen_style",false,ScreenStyleActivity.this);
                        LauncherAppState.setmIsDisableAllApps(false);
                        break;
                }
            }
        });
    }
}
