package com.ozersoft.indoorlocation;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int REQUEST_ENABLE_BT = 1;
    boolean isBluetoothActive = false;

    int delayTime=0;
    Context mContext;

    SeekBar delay_seekbar;
    Switch delay_switch;
    TextView delay_textview;
    ListView listDevicesFound;
    Button btnScanDevice,btnStopDevice;
    TextView stateBluetooth;
    BluetoothAdapter bluetoothAdapter;

    ArrayAdapter<String> btArrayAdapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        btnScanDevice = (Button)findViewById(R.id.scandevice);
        btnStopDevice = (Button)findViewById(R.id.stopdevice);

        delay_seekbar=(SeekBar)findViewById(R.id.delaySeekbar);
        delay_seekbar.setOnSeekBarChangeListener(seekBarChangeListener);

        delay_switch=(Switch)findViewById(R.id.delaySwitch);
        delay_textview=(TextView)findViewById(R.id.delayTextview);

        stateBluetooth = (TextView)findViewById(R.id.bluetoothstate);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        listDevicesFound = (ListView)findViewById(R.id.devicesfound);
        btArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);
        listDevicesFound.setAdapter(btArrayAdapter);

        CheckBlueToothState();

        btnScanDevice.setOnClickListener(btnDeviceOnClickListener);
        btnStopDevice.setOnClickListener(btnDeviceOnClickListener);

        registerReceiver(ActionFoundReceiver,
                new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(ActionFoundReceiver);
    }

    private void CheckBlueToothState(){
        if (bluetoothAdapter == null){
            stateBluetooth.setText("Bluetooth NOT support");
            isBluetoothActive = false;
            stateBluetooth.setTextColor(Color.MAGENTA);
        }else{
            if (bluetoothAdapter.isEnabled()){
                stateBluetooth.setTextColor(Color.GREEN);
                isBluetoothActive = true;
                if(bluetoothAdapter.isDiscovering()){
                    stateBluetooth.setText("Bluetooth is currently in device discovery process.");
                }else{
                    stateBluetooth.setText("Bluetooth is Enabled.");
                    btnScanDevice.setEnabled(true);
                }
            }else{
                stateBluetooth.setText("Bluetooth is NOT Enabled!");
                stateBluetooth.setTextColor(Color.RED);
                isBluetoothActive = false;
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener
            = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            delay_seekbar.setMax(10);
            delay_textview.setText("Delay: " + progress + " sn.");
            delayTime = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    };
    private Button.OnClickListener btnDeviceOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            ScanDevices scanner = new ScanDevices();
            if (arg0 == btnScanDevice) {
                if (isBluetoothActive) {
                    btArrayAdapter.clear();
                    bluetoothAdapter.startDiscovery();
                }
              //  scanner.execute();
            }else if(arg0 == btnStopDevice){
              //  scanner.cancel(true);
            }
        }};

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if(requestCode == REQUEST_ENABLE_BT){
            CheckBlueToothState();
        }
    }

    private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);

                btArrayAdapter.add(device.getName() + "\n" +
                                   device.getAddress() +"\n" +
                                   rssi);
                btArrayAdapter.notifyDataSetChanged();            }
        }};
    
    private class ScanDevices extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            Toast.makeText(mContext,"Scanning",Toast.LENGTH_SHORT).show();
         /*   while(true) {
                try {
                //    Toast.makeText(mContext,"Scanning",Toast.LENGTH_SHORT).show();
                    if (isBluetoothActive) {
                        btArrayAdapter.clear();
                        bluetoothAdapter.startDiscovery();
                    }
                    Thread.sleep(delayTime * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
                }
            }*/
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

}