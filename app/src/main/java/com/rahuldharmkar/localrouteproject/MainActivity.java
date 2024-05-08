package com.rahuldharmkar.localrouteproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    private WiFiDirectService wifiDirectService;
    private boolean isServiceBound = false;

    private TextView textView;
    private Button button;
    private int value = 100; // Initial value



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        // Set onClickListener for the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update value and update TextView
                value--;
                updateTextView();
                // Send updated value to other devices
                if (isServiceBound) {
                    wifiDirectService.sendData(String.valueOf(value));
                }
            }
        });

    }

    // Method to update the TextView
    private void updateTextView() {
        textView.setText(String.valueOf(value));
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to WiFiDirectService
        Intent intent = new Intent(this, WiFiDirectService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (isServiceBound) {
            unbindService(serviceConnection);
            isServiceBound = false;
        }
    }

    // Define the callback for service binding
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WiFiDirectService.LocalBinder binder = (WiFiDirectService.LocalBinder) service;
            wifiDirectService = binder.getService();
            isServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBound = false;
        }
    };


}