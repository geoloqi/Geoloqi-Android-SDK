package com.geoloqi.android.sample.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.geoloqi.android.sample.R;
import com.geoloqi.android.sample.receiver.SampleReceiver;
import com.geoloqi.android.sdk.service.LQService;
import com.geoloqi.android.sdk.service.LQService.LQBinder;
import com.geoloqi.android.sdk.ui.LQSettingsActivity;

/**
 * <p>...</p>
 * 
 * @author Tristan Waddington
 */
public class LauncherActivity extends Activity implements View.OnClickListener,
        SampleReceiver.OnLocationChangedListener {
    public static final String TAG = "LauncherActivity";
    public static final String LOCALHOST = "10.0.2.2";
    public static final int LOCALHOST_UDP_PORT = 43333;
    
    private LQService mService;
    private boolean mBound;
    private SampleReceiver mLocationReceiver = new SampleReceiver();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Set our on click listeners
        findViewById(R.id.home_button_1).setOnClickListener(this);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Bind to LQService so we can call methods on it.
        // TODO: Define Intent ACTIONS for starting the service.
        //       - START_WITH_ANON_ACCOUNT
        //       - START_WITH_USER_ACCOUNT
        Intent intent = new Intent(this, LQService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        
        // Wire up the sample location receiver
        final IntentFilter filter = new IntentFilter();
        filter.addAction(SampleReceiver.ACTION_LOCATION_CHANGED);
        registerReceiver(mLocationReceiver, filter);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        
        // Unbind from LQService
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        
        // Unregister our location receiver
        unregisterReceiver(mLocationReceiver);
    }
    
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
        case R.id.home_button_1:
            Log.d(TAG, "Clicked!");
            /*
            try {
                // This debug code sends a byte to the local UDP server.
                byte[] message = new byte [80];
                DatagramPacket packet = new DatagramPacket(message, 0, message.length);
                DatagramSocket socket = new DatagramSocket();
                socket.connect(new InetSocketAddress(LOCALHOST, LOCALHOST_UDP_PORT));
                // TODO: The production server will return a 32-bit unix timestamp if the request was received.
                // TODO: The key here are asynchronous requests:
                //         - Send a packet and exit.
                //         - Another thread should receive responses.
                socket.send(packet);
                
                // Get response synchronously
                DatagramPacket receivePacket = new DatagramPacket(new byte[32], 32);
                socket.receive(receivePacket);
                byte[] response = receivePacket.getData();
                Log.d(TAG, "Response length: "+response.length);
                long value = 0;
                for (int i = 0; i < response.length; i++) {
                   value += (response[i] & 0xff) << (8 * i);
                }
                Log.d(TAG, "Response: "+value);
                
                // Close connection
                socket.close();
            } catch (Exception e) {
                Log.e(TAG, "RuntimeException!", e);
            }
            */
            break;
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case R.id.settings:
            startActivity(new Intent(this, LQSettingsActivity.class));
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

    @Override
    public void onLocationChanged(Location location) {
        TextView latitudeView = (TextView) findViewById(R.id.location_lat);
        TextView longitudeView = (TextView) findViewById(R.id.location_long);
        
        latitudeView.setText(Double.toString(location.getLatitude()));
        longitudeView.setText(Double.toString(location.getLongitude()));
    }
}
