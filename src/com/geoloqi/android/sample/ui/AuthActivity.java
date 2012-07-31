package com.geoloqi.android.sample.ui;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.geoloqi.android.sample.R;
import com.geoloqi.android.sdk.LQException;
import com.geoloqi.android.sdk.LQSession;
import com.geoloqi.android.sdk.LQSession.OnRunApiRequestListener;
import com.geoloqi.android.sdk.LQSharedPreferences;
import com.geoloqi.android.sdk.LQTracker;
import com.geoloqi.android.sdk.service.LQService;
import com.geoloqi.android.sdk.service.LQService.LQBinder;

/**
 * <p>This activity class is used to demonstrate how a user
 * can be authenticated with the tracking service.</p>
 * 
 * @author Tristan Waddington
 */
public class AuthActivity extends Activity implements OnClickListener {
    public static final String TAG = "AuthActivity";
    
    private Handler mHandler = new Handler();
    private LQService mService;
    private boolean mBound;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.auth);
        
        Button submit = (Button) findViewById(R.id.submit_button);
        if (submit != null) {
            submit.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.submit_button:
            if (mService != null) {
                // Get the username and password from the form fields
                String username = ((EditText) findViewById(R.id.username)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();
                
                // Reset push token
                LQSharedPreferences.removePushToken(this);
                
                // Authenticate the session
                LQSession.requestSession(username, password, new OnRunApiRequestListener() {
                    @Override
                    public void onSuccess(LQSession session, JSONObject json,
                            Header[] headers) {
                        // Swap out the tracker session with our fresh one
                        LQTracker tracker = mService.getTracker();
                        if (tracker != null) {
                            // Update the saved session
                            mService.setSavedSession(session);
                            
                            // Update the tracker with the new session
                            tracker.setSession(session);
                            
                            // Finish the activity
                            finish();
                        }
                    }
                    @Override
                    public void onFailure(LQSession session, LQException e) {
                        // An error occurred!
                        Toast.makeText(AuthActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onComplete(LQSession session, JSONObject json,
                            Header[] headers, StatusLine status) {
                        // The request was successful, but returned a non-200 response!
                        Toast.makeText(AuthActivity.this, String.format("Server returned a %s response!",
                                        status.getStatusCode()), Toast.LENGTH_LONG).show();
                    }
                }, mHandler, this);
            }
            break;
        }
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
