package com.example.fluke;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
    private final int REQUEST_LOCATION_PERMISSIONS = 0;

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
    WifiP2pDevice[] deviceArray;

    Socket socket;

    ServerClass serverClass;
    ClientClass clientClass;

    boolean isHost;

    private boolean hasLocationPermission() {

        // Request fine location permission if not already granted
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this ,
                    new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                    REQUEST_LOCATION_PERMISSIONS);

            return false;
        }

        return true;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialCalls();
        exqListener();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this ,
                    new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                    REQUEST_LOCATION_PERMISSIONS);


        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this ,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    REQUEST_LOCATION_PERMISSIONS);


        }

    }


    private void exqListener() {
        onOffBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivityForResult(intent, 1);
        });

        discoverBtn.setOnClickListener(this::onClick
        );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final WifiP2pDevice device = deviceArray[i];
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;


                manager.connect(channel, config, new WifiP2pManager.ActionListener() {

                    @Override
                    public void onSuccess() {
                        connectionStatus.setText(new StringBuilder().append(getString(R.string.gooCon)).append(device.deviceAddress).toString());

                    }

                    @Override
                    public void onFailure(int i) {
                        connectionStatus.setText(R.string.badCon);

                    }
                });

            }
        });

        sendBtn.setOnClickListener(view -> {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    String msg = writeMsg.getText().toString();
    executorService.execute(() -> {
if(msg!=null && isHost) {
    serverClass.write(msg.getBytes());
}else if(msg!=null && !isHost) {
    clientClass.write(msg.getBytes());
}
});
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
            if (!wifiP2pDeviceList.equals(peers)) {



            peers.clear();
            peers.addAll(wifiP2pDeviceList.getDeviceList());
//            Log.v("list device list", wifiP2pDeviceList.getDeviceList().toString());
//            Log.v("list", peers.toString());

            deviceNameArray = new String[wifiP2pDeviceList.getDeviceList().size()];
            deviceArray = new WifiP2pDevice[wifiP2pDeviceList.getDeviceList().size()];

            int index = 0;
            for (WifiP2pDevice device : wifiP2pDeviceList.getDeviceList()) {

                deviceNameArray[index] = device.deviceName;
                deviceArray[index] = device;
                index++;
//                Log.v("device found", "device is " + Arrays.toString(deviceArray));
            }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, deviceNameArray);
                listView.setAdapter(adapter);


                if (peers.size() == 0) {
                    connectionStatus.setText(R.string.missDevice);
                }


        }

        }
    };

    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiGroup) {
            final InetAddress groupOwnerAddress = wifiGroup.groupOwnerAddress;
            if(wifiGroup.groupFormed && wifiGroup.isGroupOwner)
            {
                connectionStatus.setText(R.string.hostStat);
                isHost = true;
                serverClass = new ServerClass();
                serverClass.start();
            }else if (wifiGroup.groupFormed)
            {
                connectionStatus.setText(R.string.client);
                isHost = false;
                clientClass = new ClientClass(groupOwnerAddress);
                clientClass.start();

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

    @SuppressLint("MissingPermission")
    private void onClick(View view) {
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                connectionStatus.setText(R.string.discstart);
            }


            @Override
            public void onFailure(int i) {
                connectionStatus.setText(R.string.discnostart);


            }
        });
    }

    public class ServerClass extends Thread {
        ServerSocket hostAdd;
        private InputStream inputStream;
        private OutputStream outputStream;
        private ServerSocket serverSocket;

        public void write(byte[] bytes)
        {
            try {
                outputStream.write(bytes);
            }catch (IOException e){
                e.printStackTrace();

            }

        }

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(8888);
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();

            } catch (IOException e) {
                e.printStackTrace();

            }
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executorService.execute(() -> {

                byte[] buffer = new byte[1024];
                int bytes;

                while (socket != null) {
                    try {
                        bytes = inputStream.read(buffer);
                        if (bytes > 0) {
                            int finalBytes = bytes;
                            handler.post(() -> {
                                String tempMSG = new String(buffer, 0, finalBytes);
                                msg_box.setText(tempMSG);


                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            });
        }
    }

    public class ClientClass extends Thread {
        String hostAdd;
        private InputStream inputStream;
        private OutputStream outputStream;

        public void write(byte[] bytes)
        {
            try {
                outputStream.write(bytes);
            }catch (IOException e){
                e.printStackTrace();

            }

        }

        public ClientClass(InetAddress hostAddress) {
            hostAdd = hostAddress.getHostAddress();
            socket= new Socket();

        }

        @Override
        public void run(){
            try{
                socket.connect(new InetSocketAddress(hostAdd, 8888),500);
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();

            }catch (IOException e){
                e.printStackTrace();
            }


            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());


            executorService.execute(() -> {

                byte[] buffer = new byte[1024];
                int bytes;

                while (socket!=null){
                    try{
                        bytes = inputStream.read(buffer);
                        if(bytes>0){
                            int finalBytes = bytes;
                            handler.post(() -> {
                                String tempMSG = new String(buffer,0,finalBytes);
                                msg_box.setText(tempMSG);


                            });
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }

            });
        }
    }



}

