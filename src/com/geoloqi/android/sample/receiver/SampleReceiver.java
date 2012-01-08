package com.geoloqi.android.sample.receiver;

import android.content.Context;
import android.location.Location;

import com.geoloqi.android.sdk.receiver.LQBroadcastReceiver;

/**
 * <p>This is a sample BroadcastReceiver implementation that
 * extends from {@link LQBroadcastReceiver}. This implementation
 * is designed to highlight how to consume broadcast intents and
 * take action on the messages received.</p>
 * 
 * @author Tristan Waddington
 */
public class SampleReceiver extends LQBroadcastReceiver {
    public static final String TAG = "SampleReceiver";
    
    @Override
    public void onLocationChanged(Context context, Location location) {
        try {
            OnLocationChangedListener listener = (OnLocationChangedListener) context;
            listener.onLocationChanged(location);
        } catch (ClassCastException e) {
            // The broadcast receiver is running with a Context that
            // does not implement OnLocationChangedListener. If your activity
            // has implemented the interface, then this generally means
            // that the receiver is running in a global context and
            // is not bound to any particular activity.
        }
    }
    
    public interface OnLocationChangedListener {
        public void onLocationChanged(Location location);
    }
}
