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

public class ControlActivity extends AppCompatActivity implements View.OnClickListener {
    boolean statusLed=false, statusBuzzer=false;
    public static BluetoothManager bluetoothManager;
    ImageView ibUp, ibLeft, ibRight, ibDown, ibBuzzer, ibLeds, ibStop, ibConnect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        ibConnect=findViewById(R.id.ibConnect);
        ibBuzzer=findViewById(R.id.ibBuzzer);
        ibLeds=findViewById(R.id.ibLeds);
        ibStop=findViewById(R.id.ibStop);
        ibUp=findViewById(R.id.ibUp);
        ibDown=findViewById(R.id.ibDown);
        ibLeft=findViewById(R.id.ibLeft);
        ibRight=findViewById(R.id.ibRight);

        bluetoothManager=new BluetoothManager(this);
        if (bluetoothManager.getmBluetoothAdapter() == null) {
            // Device does not support Bluetooth
            Toast.makeText(this, "The devicie doesn't support bluetooth", Toast.LENGTH_SHORT).show();
            finish();
        }
        ibConnect.setOnClickListener(this);
        ibBuzzer.setOnClickListener(this);
        ibLeds.setOnClickListener(this);
        ibStop.setOnClickListener(this);
        ibUp.setOnClickListener(this);
        ibLeft.setOnClickListener(this);
        ibRight.setOnClickListener(this);
        ibDown.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bluetoothManager.connectDisconnect();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.ibConnect){
            if (!bluetoothManager.DEVICE_ADDRESS.equals("") && bluetoothManager.DEVICE_ADDRESS!=null) {
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
            } else {
                Toast.makeText(getApplicationContext(), "Select a device", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, PairedDevices.class);
                startActivity(intent);
            }
        }
        if (bluetoothManager.isDeviceConnected()) {
            switch (view.getId()) {
                case R.id.ibUp:
                    bluetoothManager.sendMessage("a");
                    break;
                case R.id.ibRight:
                    bluetoothManager.sendMessage("b");
                    break;
                case R.id.ibStop:
                    bluetoothManager.sendMessage("c");
                    break;
                case R.id.ibLeft:
                    bluetoothManager.sendMessage("d");
                    break;
                case R.id.ibDown:
                    bluetoothManager.sendMessage("e");
                    break;
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