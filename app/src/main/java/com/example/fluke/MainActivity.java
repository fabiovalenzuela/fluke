package com.example.fluke;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button onOffBtn, discoverBtn;
    ListView listView;
    TextView msg_box, connectionStatus;
    EditText writeMsg;
    ImageButton sendBtn;

    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    BroadcastReceiver receiver;
    IntentFilter intentFilter;

    List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    String[] deviceNameArray;
    WifiP2pDevice[] devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialCalls();
        exqListener();
    }

    private void exqListener() {
        onOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivityForResult(intent, 1);
            }
        });
        discoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        connectionStatus.setText("Discovery Started");
                    }

                    @Override
                    public void onFailure(int i) {
                        connectionStatus.setText("Discovery Not Started");


                    }
                });
            }
        });
    }


    private void initialCalls() {
        onOffBtn= findViewById(R.id.switch1);
        discoverBtn= findViewById(R.id.discoverBt);
        sendBtn= findViewById(R.id.sendBT);
        listView= findViewById(R.id.peerLV);
        msg_box= findViewById(R.id.msgTV);
        writeMsg= findViewById(R.id.msgET);
        connectionStatus= findViewById(R.id.connectionStatus);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        receiver = new WifiDirectBroadcastReceiver(manager, channel, this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
    }

    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            if(wifiP2pDeviceList.equals(peers)){
                
            }
        }
    };

    @Override
    protected void onPostResume() {
        super.onPostResume();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}