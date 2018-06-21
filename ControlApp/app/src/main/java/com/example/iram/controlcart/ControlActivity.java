package com.example.iram.controlcart;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class ControlActivity extends AppCompatActivity implements View.OnClickListener {
    boolean statusLed=false, statusBuzzer=false;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    //Adress of bluetooth module
    private String DEVICE_ADDRESS="AB:60:51:57:34:02";
    String message;
    byte buffer[];
    boolean stopThread;
    boolean deviceConnected=false;
    int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    Button btnConnect, btnDisconnect, btnBuzzer, btnLeds, btnStop;
    ImageView ibUp, ibLeft, ibRight, ibDown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        btnConnect=findViewById(R.id.btnConnect);
        btnDisconnect=findViewById(R.id.btnDisconnect);
        btnBuzzer=findViewById(R.id.btnBuzzer);
        btnLeds=findViewById(R.id.btnLeds);
        btnStop=findViewById(R.id.btnStop);
        ibUp=findViewById(R.id.ibUp);
        ibDown=findViewById(R.id.ibDown);
        ibLeft=findViewById(R.id.ibLeft);
        ibRight=findViewById(R.id.ibRight);


        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(this, "El dispositivo no soporta bluetooth", Toast.LENGTH_SHORT).show();
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                Log.d("DEVICES BLUETOOTH",device.getName() + "\n" + device.getAddress());
            }
        }
        btnConnect.setOnClickListener(this);
        btnDisconnect.setOnClickListener(this);
        btnBuzzer.setOnClickListener(this);
        btnLeds.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        ibUp.setOnClickListener(this);
        ibLeft.setOnClickListener(this);
        ibRight.setOnClickListener(this);
        ibDown.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnConnect:
                if(BTinit())
                {
                    if(BTconnect())
                    {
                        deviceConnected=true;
                        beginListenForData();
                    }

                }
                break;
            case R.id.btnDisconnect:
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
                deviceConnected=false;
                break;
            case R.id.ibUp:
                message="a";
                try {
                    outputStream.write(message.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ibRight:
                message= "b";
                try {
                    outputStream.write(message.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnStop:
                message= "c";
                try {
                    outputStream.write(message.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ibLeft:
                message= "d";
                try {
                    outputStream.write(message.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ibDown:
                message= "e";
                try {
                    outputStream.write(message.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnBuzzer:
                if (statusBuzzer){
                    message= "z";
                }else {
                    message = "p";
                }
                statusBuzzer=!statusBuzzer;
                try {
                    outputStream.write(message.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnLeds:
                if (statusBuzzer){
                    message= "w";
                }else {
                    message = "q";
                }
                statusLed=!statusLed;
                try {
                    outputStream.write(message.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    public boolean BTconnect()
    {
        boolean connected=true;
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            connected=false;
        }
        if(connected)
        {
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
    public boolean BTinit()
    {
        boolean found=false;
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Device doesnt Support Bluetooth",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if(bondedDevices.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please Pair the Device first",Toast.LENGTH_SHORT).show();
        }
        else
        {
            for (BluetoothDevice iterator : bondedDevices)
            {
                if(iterator.getAddress().equals(DEVICE_ADDRESS))
                {
                    device=iterator;
                    found=true;
                    break;
                }
            }
        }
        return found;
    }
    void beginListenForData()
    {
        final Handler handler = new Handler();
        stopThread = false;
        buffer = new byte[1024];
        Thread thread  = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopThread)
                {
                    try
                    {
                        int byteCount = inputStream.available();
                        if(byteCount > 0)
                        {
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);
                            final String string=new String(rawBytes,"UTF-8");
                            handler.post(new Runnable() {
                                public void run()
                                {
                                }
                            });

                        }
                    }
                    catch (IOException ex)
                    {
                        stopThread = true;
                    }
                }
            }
        });

        thread.start();
    }
}
