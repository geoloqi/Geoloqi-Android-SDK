package com.geoloqi.android.sample.receiver;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.geoloqi.android.sdk.LQTracker.LQTrackerProfile;
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
    public void onTrackerProfileChanged(Context context,
                    LQTrackerProfile oldProfile, LQTrackerProfile newProfile) {
        try {
            OnTrackerProfileChangedListener listener = (OnTrackerProfileChangedListener) context;
            listener.onTrackerProfileChanged(oldProfile, newProfile);
        } catch (ClassCastException e) {
            // The broadcast receiver is running with a Context that
            // does not implement OnTrackerProfileChangedListener. If your activity
            // has implemented the interface, then this generally means
            // that the receiver is running in a global context and
            // is not bound to any particular activity.
        }
    }

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

    @Override
    public void onLocationUploaded(Context context, int count) {
        try {
            OnLocationUploadedListener listener = (OnLocationUploadedListener) context;
            listener.onLocationUploaded(count);
        } catch (ClassCastException e) {
            // The broadcast receiver is running with a Context that
            // does not implement OnLocationUploadedListener. If your activity
            // has implemented the interface, then this generally means
            // that the receiver is running in a global context and
            // is not bound to any particular activity.
        }
    }

    @Override
    public void onPushMessageReceived(Context context, Bundle data) {
        Toast.makeText(context,
                "New push message received!", Toast.LENGTH_LONG).show();
        
        // Dump the message payload to the console
        Log.d(TAG, "Push message payload:");
        for (String key : data.keySet()) {
            Log.d(TAG, String.format("%s: %s", key, data.get(key)));
        }
    }

    public interface OnTrackerProfileChangedListener {
        public void onTrackerProfileChanged(LQTrackerProfile oldProfile,
                        LQTrackerProfile newProfile);
    }

    public interface OnLocationChangedListener {
        public void onLocationChanged(Location location);
    }

    public interface OnLocationUploadedListener {
        public void onLocationUploaded(int count);
    }
}
