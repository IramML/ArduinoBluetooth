package com.iramml.songscontrol.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.iramml.songscontrol.R;
import com.iramml.songscontrol.RecyclerViewDevices.ClickListener;
import com.iramml.songscontrol.RecyclerViewDevices.adapterDevices;
import com.iramml.songscontrol.RecyclerViewSongs.adapterSongs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    //Adress of bluetooth module
    public static String DEVICE_ADDRESS;
    String message;
    byte buffer[];
    boolean stopThread;
    boolean deviceConnected=false;
    int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    String[] songsMessage={"a", "b", "c", "d", "e"};
    ArrayList<String> songs;
    RecyclerView rvSongs;
    RecyclerView.LayoutManager layoutManager;
    adapterSongs adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
        inicializeSongs();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(this, "The device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        implementRecyclerView();
    }

    private void implementRecyclerView() {
        adapter= new adapterSongs(this, songs, new com.iramml.songscontrol.RecyclerViewSongs.ClickListener() {
            @Override
            public void onClick(View view, int index) {
                if (deviceConnected) {
                    try {
                        message=songsMessage[index];
                        outputStream.write(message.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    if(DEVICE_ADDRESS!="" && DEVICE_ADDRESS!=null){
                        connectDisconnect();
                    }else{
                        Intent intent=new Intent(MainActivity.this, pairedDevices.class);
                        startActivity(intent);
                    }

                }
            }
        });
        rvSongs.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        connectDisconnect();
    }
    private void inicializeSongs(){
        songs= new ArrayList<>();
        songs.add("Mario Bros");
        songs.add("Star Wars");
        songs.add("Game Of Thrones");
        songs.add("Zelda");
        songs.add("Doctor Who");
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
    void initRecyclerView() {
        rvSongs=findViewById(R.id.rvSongs);
        rvSongs.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        rvSongs.setLayoutManager(layoutManager);
    }
    public boolean BTinit() {
        boolean found=false;
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Device doesn't Support Bluetooth",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled()) {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if(bondedDevices.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Please Pair the Device first",Toast.LENGTH_SHORT).show();
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
    public void connectDisconnect(){
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
                if (!deviceConnected) Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
                else Toast.makeText(getApplicationContext(), "The device could not disconnect", Toast.LENGTH_SHORT).show();
            } else {
                if (BTinit()) {
                    if (BTconnect()) {
                        deviceConnected = true;
                        beginListenForData();
                    }
                }
                if (deviceConnected) Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                else Toast.makeText(getApplicationContext(), "The device could not connect", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
