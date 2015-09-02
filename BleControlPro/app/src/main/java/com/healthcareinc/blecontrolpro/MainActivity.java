package com.healthcareinc.blecontrolpro;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.healthcareinc.blecontrol.ble.BleScan;
import com.healthcareinc.blecontrol.listener.BleScanListener;

import java.util.ArrayList;

public class MainActivity extends BleBaseActivity implements View.OnClickListener {
    private final String TAG = "PETER";
    private ImageView back;
    private TextView scanBtn;
    private ListView devListView;
    private boolean isScan = true;

    private DevAdapter devAdapter;
    private BleScan bleScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        devAdapter = new DevAdapter();

        back = (ImageView) findViewById(R.id.scan_back);
        back.setOnClickListener(this);
        scanBtn = (TextView) findViewById(R.id.scan_scan_btn);
        scanBtn.setOnClickListener(this);
        devListView = (ListView) findViewById(R.id.dev_list);

        bleScan = BleScan.initial(this);
        bleScan.setLeScanCallback(new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                Log.i(TAG, "dev is: " + device.getName());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        devAdapter.addDevice(device, rssi, scanRecord);
                        devAdapter.notifyDataSetChanged();
                    }
                });

            }
        });
        bleScan.setBleOnlistener(new BleScanListener() {
            @Override
            public void bleScanFinish() {
                isScan = false;
            }
        });


        devListView.setAdapter(devAdapter);
        devListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final BluetoothDevice device = devAdapter.getDevice(position);
                if (device == null) return;
                bleScan.stopScan(0);
                isScan = false;
                Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
                intent.putExtra("devAddress", device.getAddress());
                intent.putExtra("devName", device.getName());
                startActivity(intent);
//                finish();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        isScan = true;
        bleScan.startScan(5000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scan_back:
                finish();
                break;
            case R.id.scan_scan_btn:
                if (isScan) {
                    return;
                }
                bleScan.startScan(5000);
                break;
            default:
                break;
        }
    }

    class DevAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private ArrayList<Integer> rssis;
        private ArrayList<byte[]> bRecord;
        private LayoutInflater inflater;

        public DevAdapter() {
//            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            rssis = new ArrayList<Integer>();
            bRecord = new ArrayList<byte[]>();
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        public void addDevice(BluetoothDevice device, int rs, byte[] record) {
            if (!mLeDevices.contains(device)) {
                mLeDevices.add(device);
                rssis.add(rs);
                bRecord.add(record);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int position) {
            return mLeDevices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DevHolder devHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.scan_dev_item, null);
                devHolder = new DevHolder();
                convertView.setTag(devHolder);
            } else {
                devHolder = (DevHolder) convertView.getTag();
            }
            devHolder.devName = (TextView) convertView.findViewById(R.id.dev_name);
            devHolder.devAddress = (TextView) convertView.findViewById(R.id.dev_address);
            devHolder.devName.setText(mLeDevices.get(position).getName());

            BluetoothDevice device = mLeDevices.get(position);
            final String deviceName = device.getName();
            Log.i(TAG, "adapter deviceName is: " + deviceName);
            if (deviceName != null && deviceName.length() > 0)
                devHolder.devName.setText(deviceName);
            else
                devHolder.devName.setText("未知设备");
            devHolder.devAddress.setText(device.getAddress());
            return convertView;
        }

        class DevHolder {
            private TextView devName;
            private TextView devAddress;
        }
    }

}
