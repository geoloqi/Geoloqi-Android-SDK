package com.geoloqi.android.sample.receiver;

import android.location.Location;
import android.util.Log;

import com.geoloqi.android.sdk.receiver.LQBroadcastReceiver;

public class SampleReceiver extends LQBroadcastReceiver {
    public static final String TAG = "SampleReceiver";
    
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Received a new location!");
        Log.d(TAG, location.toString());
    }
}
