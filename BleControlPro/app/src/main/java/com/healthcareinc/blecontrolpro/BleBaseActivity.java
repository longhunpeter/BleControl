package com.healthcareinc.blecontrolpro;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.healthcareinc.blecontrol.ble.BleControl;
import com.healthcareinc.blecontrol.ble.BleScan;
import com.healthcareinc.blecontrol.listener.BleOperationListener;
import com.healthcareinc.blecontrol.listener.BleScanListener;


/**
 * Created by lxl on 2014/8/28.
 */
public class BleBaseActivity extends Activity implements BleOperationListener, BluetoothAdapter.LeScanCallback, BleScanListener {
    protected BleControl bleControl;
    protected BleScan bleScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bleControl != null) {
            bleControl.unRegisterBleReceiver();
//            bleControl.disConnectDev();
//            bleControl.destroyBluetoothLeService();
        }
    }

    /**
     * initial method.
     */
    private void init() {
        bleControl = BleControl.initial(this);
        bleControl.setBleOperationListener(this);
        bleScan = BleScan.initial(this);
        bleScan.setLeScanCallback(this);
        bleScan.setBleOnlistener(this);
    }


    /**
     * 弹出提示信息
     *
     * @param msg 信息
     */
    public void makeToast(CharSequence msg) {
        makeToast(msg, Toast.LENGTH_SHORT);
    }

    /**
     * 弹出提示信息
     *
     * @param msg      信息
     * @param duration 弹出时间长度
     */
    public void makeToast(CharSequence msg, int duration) {
        Toast.makeText(this, msg, duration).show();
    }

    /**
     * 弹出提示信息
     *
     * @param resId 资源ID
     */
    public void makeToast(int resId) {
        makeToast(resId, Toast.LENGTH_SHORT);
    }

    /**
     * 弹出提示信息
     *
     * @param resId    资源ID
     * @param duration 弹出时间长度
     */
    public void makeToast(int resId, int duration) {
        Toast.makeText(this, resId, duration).show();
    }

    @Override
    public void bleServerOk() {
    }

    @Override
    public void bleGattConnect() {

    }

    @Override
    public void bleServerConnect() {

    }

    @Override
    public void bleDisConnect() {

    }

    @Override
    public void bleData(byte[] bytes) {

    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

    }

    @Override
    public void bleScanFinish() {

    }
}
