package com.geoloqi.android.sample.receiver;

import android.content.Context;
import android.location.Location;

import com.geoloqi.android.sdk.receiver.LQBroadcastReceiver;

public class SampleReceiver extends LQBroadcastReceiver {
    public static final String TAG = "SampleReceiver";
    
    @Override
    public void onLocationChanged(Context context, Location location) {
        try {
            OnLocationChangedListener listener = (OnLocationChangedListener) context;
            listener.onLocationChanged(location);
        } catch(ClassCastException e) {
            // The broadcast receiver is running with a Context that
            // does not implement OnLocationChangedListener. This generally
            // means that the receiver is running in a global context and
            // is not bound to any particular activity.
        }
    }
    
    public interface OnLocationChangedListener {
        public void onLocationChanged(Location location);
    }
}
