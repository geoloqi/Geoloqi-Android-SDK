package com.geoloqi.android.sample.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.geoloqi.android.sample.R;
import com.geoloqi.android.sdk.LQBuild;
import com.geoloqi.android.sdk.LQSession;
import com.geoloqi.android.sdk.LQSharedPreferences;
import com.geoloqi.android.sdk.LQTracker;
import com.geoloqi.android.sdk.LQTracker.LQTrackerProfile;
import com.geoloqi.android.sdk.service.LQService;
import com.geoloqi.android.sdk.service.LQService.LQBinder;

/**
 * <p>This activity class is used to expose location tracking
 * preferences to a user.</p>
 * 
 * @author Tristan Waddington
 */
public class SettingsActivity extends PreferenceActivity implements OnPreferenceChangeListener,
        OnPreferenceClickListener {
    public static final String TAG = "SettingsActivity";

    /** An instance of the default SharedPreferences. */
    private SharedPreferences mPreferences;
    private LQService mService;
    private boolean mBound;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        
        // Get a shared preferences instance
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        
        // Set any preference listeners
        Preference preference = findPreference(getString(R.string.pref_key_tracker_profile));
        if (preference != null) {
            preference.setOnPreferenceChangeListener(this);
        }
        
        preference = findPreference(getString(R.string.pref_key_account_username));
        if (preference != null) {
            preference.setOnPreferenceClickListener(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        
        if (mPreferences != null) {
            Preference preference = null;
            
            // Display the device ID
            preference = findPreference(getString(R.string.pref_key_device_id));
            if (preference != null) {
                preference.setSummary(LQSharedPreferences.getDeviceUuid(this));
            }
            
            // Display the WiFi MAC address
            preference = findPreference(getString(R.string.pref_key_device_mac));
            if (preference != null) {
                preference.setSummary(LQSharedPreferences.getMacAddress(this));
            }
            
            // Display the account username
            preference = findPreference(getString(R.string.pref_key_account_username));
            if (preference != null) {
                preference.setSummary(LQSharedPreferences.getSessionUsername(this));
            }
            
            // Display the SDK version
            preference = findPreference(getString(R.string.pref_key_sdk_version));
            if (preference != null) {
                preference.setSummary(LQBuild.LQ_SDK_VERSION);
            }
            
            // Display the SDK build
            preference = findPreference(getString(R.string.pref_key_sdk_build));
            if (preference != null) {
                preference.setSummary(LQBuild.LQ_SDK_BUILD);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        
        // Bind to the tracking service so we can call public methods on it
        Intent intent = new Intent(this, LQService.class);
        bindService(intent, mConnection, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        
        // Unbind from LQService
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        if (key != null) {
            if (key.equals(getString(R.string.pref_key_tracker_profile))) {
                // Check to see if the tracker can switch to the requested profile
                if (mBound && mService != null) {
                    final LQTracker tracker = mService.getTracker();
                    if (tracker != null) {
                        int ordinal = Integer.parseInt((String) newValue);
                        final LQTrackerProfile newProfile = LQTrackerProfile.values()[ordinal];
                        if (!tracker.setProfile(newProfile)) {
                            // Cannot switch to the indicated profile, don't update preferences!
                            Toast.makeText(this, String.format("Unable to switch to profile %s.",
                                            newProfile), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        final String key = preference.getKey();
        if (key.equals(getString(R.string.pref_key_account_username))) {
            LQSession session = mService.getSession();
            if (session != null) {
                if (session.isAnonymous()) {
                    // Start log-in Activity
                    startActivity(new Intent(this, AuthActivity.class));
                }
            }
            return true;
        }
        return false;
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                // We've bound to LocalService, cast the IBinder and get LocalService instance.
                LQBinder binder = (LQBinder) service;
                mService = binder.getService();
                mBound = true;
            } catch (ClassCastException e) {
                // Pass
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };
}
