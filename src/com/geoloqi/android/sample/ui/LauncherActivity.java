package com.geoloqi.android.sample.ui;

import com.geoloqi.android.sample.R;
import com.geoloqi.android.sdk.LQException;
import com.geoloqi.android.sdk.LQRequest;
import com.geoloqi.android.sdk.LQSession;
import com.geoloqi.android.sdk.LQSession.OnCreateAnonymousUserAccountListener;
import com.geoloqi.android.sdk.LQTracker;
import com.geoloqi.android.sdk.LQTracker.LQTrackerProfile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class LauncherActivity extends Activity {
    public static final String TAG = "LauncherActivity";
    public static final String LOCALHOST = "10.0.2.2";
    public static final int LOCALHOST_UDP_PORT = 43333;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final LQTracker tracker = LQTracker.getInstance();
        
        if (tracker.getSession() == null) {
            LQSession.createAnonymousUserAccount(new OnCreateAnonymousUserAccountListener() {
                @Override
                public void onCreateAnonymousUserAccount(LQSession session,
                        LQRequest request, LQException e) {
                    if (session != null) {
                        tracker.setSession(session);
                        tracker.setProfile(LQTrackerProfile.LOGGING);
                    } else {
                        Log.e(TAG, e.getMessage());
                    }
                }
            });
        }
    }
}
