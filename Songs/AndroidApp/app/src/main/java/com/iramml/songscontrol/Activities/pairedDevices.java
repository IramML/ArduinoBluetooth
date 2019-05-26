package com.iramml.songscontrol.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.iramml.songscontrol.R;
import com.iramml.songscontrol.RecyclerViewDevices.ClickListener;
import com.iramml.songscontrol.RecyclerViewDevices.adapterDevices;

import java.util.ArrayList;
import java.util.Set;

public class pairedDevices extends AppCompatActivity {
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    adapterDevices adapter;
    ArrayList<BluetoothDevice> listDevices;
    RecyclerView rvDevices;
    Toolbar toolbar;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paired_devices);
        initToolbar();
        initRecyclerView();
        listDevices=new ArrayList();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                listDevices.add(device);
            }
        }else{
            Toast.makeText(getApplicationContext(), "There are no paired devices ", Toast.LENGTH_SHORT).show();
        }
        adapter= new adapterDevices(this, listDevices, new ClickListener(){
            @Override
            public void onClick(View view, int index) {
                MainActivity.DEVICE_ADDRESS=listDevices.get(index).getAddress();
                finish();
            }
        });
        rvDevices.setAdapter(adapter);
    }

    void initRecyclerView() {
        rvDevices=findViewById(R.id.rvDevices);
        rvDevices.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        rvDevices.setLayoutManager(layoutManager);
    }

    public void initToolbar(){
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Paired devices");
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }
}
