package com.example.iram.controlcart.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.iram.controlcart.R;
import com.example.iram.controlcart.Util.BluetoothManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class ControlActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean statusLed=false, statusBuzzer=false;
    public static BluetoothManager bluetoothManager;
    private ImageView ibBuzzer, ibLeds, ibConnect;
    private JoystickView joystick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        joystick =findViewById(R.id.joystick);
        ibConnect=findViewById(R.id.ibConnect);
        ibBuzzer=findViewById(R.id.ibBuzzer);
        ibLeds=findViewById(R.id.ibLeds);

        bluetoothManager=new BluetoothManager(this);
        if (bluetoothManager.getmBluetoothAdapter() == null) {
            // Device does not support Bluetooth
            Toast.makeText(this, "The devicie doesn't support bluetooth", Toast.LENGTH_SHORT).show();
            finish();
        }

        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                if (bluetoothManager.isDeviceConnected()) {
                    if (strength > 10) {
                        if ((angle <= 45 && angle >= 0) || (angle >= 315 && angle < 360))
                            //right
                            bluetoothManager.sendMessage("b");
                        else if ((angle <= 135 && angle >= 90) || (angle > 45 && angle <= 89))
                            //up
                            bluetoothManager.sendMessage("a");
                        else if ((angle <= 225 && angle >= 180) || (angle > 135 && angle <= 179))
                            //left
                            bluetoothManager.sendMessage("d");
                        else if ((angle <= 315 && angle >= 270) || (angle > 225 && angle <= 269))
                            //down
                            bluetoothManager.sendMessage("e");
                    } else {
                        bluetoothManager.sendMessage("c");
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "First connect the bluetooth", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ibConnect.setOnClickListener(this);
        ibBuzzer.setOnClickListener(this);
        ibLeds.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bluetoothManager.connectDisconnect();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.ibConnect){
            if (bluetoothManager.DEVICE_ADDRESS!=null) {
                if(!bluetoothManager.DEVICE_ADDRESS.equals("")){
                    if(bluetoothManager.isDeviceConnected()){
                        if(bluetoothManager.connectDisconnect()){
                            ibConnect.setImageResource(R.drawable.connect);
                            ibConnect.setBackgroundResource(R.color.colorAccent);
                            Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(getApplicationContext(), "The device could not disconnect", Toast.LENGTH_SHORT).show();
                    }else{
                        if(bluetoothManager.connectDisconnect()){
                            ibConnect.setImageResource(R.drawable.disconnect);
                            ibConnect.setBackgroundColor(Color.RED);
                            Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(getApplicationContext(), "The device could not connect", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Select a device", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, PairedDevices.class);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Select a device", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, PairedDevices.class);
                startActivity(intent);
            }
        }
        if (bluetoothManager.isDeviceConnected()) {
            switch (view.getId()) {
                case R.id.ibBuzzer:
                    if (statusBuzzer) {
                        bluetoothManager.sendMessage("z");
                    } else {
                        bluetoothManager.sendMessage("p");
                    }
                    statusBuzzer = !statusBuzzer;
                    break;
                case R.id.ibLeds:
                    if (statusLed) {
                        bluetoothManager.sendMessage("w");
                    } else {
                        bluetoothManager.sendMessage("q");
                    }
                    statusLed = !statusLed;
                    break;
            }
        }else{
            Toast.makeText(getApplicationContext(), "First connect the bluetooth", Toast.LENGTH_SHORT).show();
        }
    }
}