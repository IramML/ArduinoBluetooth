package com.example.iram.controlcart.Util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.iram.controlcart.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothManager {
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    //Adress of bluetooth module
    public String DEVICE_ADDRESS;
    String message;
    byte buffer[];
    boolean stopThread;
    boolean deviceConnected=false;
    int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    AppCompatActivity activity;
    public BluetoothManager(AppCompatActivity activity){
        this.activity=activity;
    }
    public void setDeviceAddress(String address){
        this.DEVICE_ADDRESS=address;
    }
    public BluetoothAdapter getmBluetoothAdapter(){
        return mBluetoothAdapter;
    }
    public boolean isDeviceConnected(){
        return deviceConnected;
    }
    public void sendMessage(String message){
        message = message;
        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean BTconnect(){
        boolean connected=true;
        try{
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            connected=false;
        }
        if(connected){
            try {
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream=socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return connected;
    }
    public boolean BTinit() {
        boolean found=false;
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(activity,"Device doesnt Support Bluetooth",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled()) {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if(bondedDevices.isEmpty()) {
            Toast.makeText(activity,"Please Pair the Device first",Toast.LENGTH_SHORT).show();
        }
        else{
            for (BluetoothDevice iterator : bondedDevices){
                if(iterator.getAddress().equals(DEVICE_ADDRESS)){
                    device=iterator;
                    found=true;
                    break;
                }
            }
        }
        return found;
    }
    void beginListenForData(){
        final Handler handler = new Handler();
        stopThread = false;
        buffer = new byte[1024];
        Thread thread  = new Thread(new Runnable() {
            public void run() {
                while(!Thread.currentThread().isInterrupted() && !stopThread) {
                    try {
                        int byteCount = inputStream.available();
                        if(byteCount > 0){
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);
                            final String string=new String(rawBytes,"UTF-8");
                            handler.post(new Runnable(){
                                public void run(){
                                }
                            });

                        }
                    }
                    catch (IOException ex){
                        stopThread = true;
                    }
                }
            }
        });

        thread.start();
    }
    public boolean connectDisconnect(){
        if (DEVICE_ADDRESS!="" && DEVICE_ADDRESS!=null) {
            if (deviceConnected) {
                stopThread = true;
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                deviceConnected = false;
                if (!deviceConnected) return true;
                else return false;
            } else {
                if (BTinit()) {
                    if (BTconnect()) {
                        deviceConnected = true;
                        beginListenForData();
                    }
                }
                if (deviceConnected) return false;
                else return false;
            }
        }
        return false;
    }
}
