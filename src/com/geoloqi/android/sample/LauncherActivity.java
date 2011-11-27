package com.geoloqi.android.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LauncherActivity extends Activity implements View.OnClickListener {
    public static final String TAG = "LauncherActivity";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        findViewById(R.id.home_button_1).setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
        case R.id.home_button_1:
            Log.d(TAG, "Clicked!");
            break;
        }
    }
}
