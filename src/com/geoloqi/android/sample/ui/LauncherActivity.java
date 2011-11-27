package com.geoloqi.android.sample.ui;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import com.geoloqi.android.sample.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LauncherActivity extends Activity implements View.OnClickListener {
    public static final String TAG = "LauncherActivity";
    public static final String LOCALHOST = "10.0.2.2";
    public static final int LOCALHOST_UDP_PORT = 43333;
    
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
            break;
        }
    }
}
