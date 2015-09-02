package com.healthcareinc.blecontrol.ble;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.healthcareinc.blecontrol.listener.BleOperationListener;
import com.healthcareinc.blecontrol.service.BluetoothLeService;
import com.healthcareinc.blecontrol.util.BleUtils;


/**
 * Created by lxl on 2014/8/22.
 * 进行相关蓝牙的操作.
 */
public class BleControl {
    private final String TAG = this.getClass().getCanonicalName();

    private Context mContext;
    private BleOperationListener mListener;
    private Intent gattServiceIntent;
    private BluetoothLeService bluetoothLeService;
    private static BleControl bleControl;

    public BleControl(Context context) {
        this.mContext = context;
        registerBleReceiver();
    }

    /**
     * 初始化方法
     *
     * @return
     */
    public static BleControl initial(Context context) {
        bleControl = new BleControl(context);
//        if (bleControl == null) {
//            synchronized (BleControl.class) {
//                if (bleControl == null)
//
//            }
//        }

        return bleControl;
    }


    /**
     * 设置蓝牙操作监听
     *
     * @param listener
     */
    public void setBleOperationListener(BleOperationListener listener) {

        this.mListener = listener;
    }

    /**
     * 根据mac地址连接蓝牙设备
     *
     * @param macAddress 设备mac地址
     */
    public void connectDev(String macAddress) {
        if (bluetoothLeService != null &&
                (macAddress != null && !macAddress.equals("")))
            bluetoothLeService.connect(macAddress);
    }


    /**
     * 断开蓝牙连接
     */
    public void disConnectDev() {
        if (bluetoothLeService != null) bluetoothLeService.disconnect();
    }

    /**
     * 向蓝牙设备写入字符串
     *
     * @param strValue
     */
    public void writeStrValue(String strValue) {
        if (bluetoothLeService != null) bluetoothLeService.writeValue(strValue.getBytes());
    }

    /**
     * 向蓝牙设备写入16进制字符串
     *
     * @param hexStrValue
     */
    public void writeHexStrValue(String hexStrValue) {
        if (bluetoothLeService != null)
            bluetoothLeService.writeValue(BleUtils.hexStringToBytes(hexStrValue));
    }

    /**
     * 写入字节数组
     *
     * @param bytes
     */
    public void writeBytesValue(byte[] bytes) {
        if (bluetoothLeService != null) bluetoothLeService.writeValue(bytes);
    }

    /**
     * 解除服务绑定
     * 在Activity或者Fragment
     * 调用onDestroy的时候调用该方法解除相关绑定
     */
    public void unRegisterBleReceiver() {
        mContext.unregisterReceiver(mGattUpdateReceiver);
        mContext.unbindService(mServiceConnection);
    }

    /**
     * 退出程序关闭蓝牙服务
     */
    public void destroyBluetoothLeService() {
        if (bluetoothLeService != null) {
            bluetoothLeService.close();
            bluetoothLeService = null;
        }
    }

    /**
     * 绑定启动服务
     */
    public void registerBleReceiver() {
        gattServiceIntent = new Intent(mContext, BluetoothLeService.class);
        mContext.bindService(gattServiceIntent, mServiceConnection, mContext.BIND_AUTO_CREATE);
        mContext.registerReceiver(mGattUpdateReceiver, BleUtils.makeGattUpdateIntentFilter());

    }


    /**
     * Code to manage Service lifecycle.
     */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            bluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!bluetoothLeService.initialize()) {
                Log.i(TAG, "Unable to initialize Bluetooth");
            }
            mListener.bleServerOk();
            Log.i(TAG, "mBluetoothLeService is okay");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bluetoothLeService = null;
        }
    };

    /**
     * 接收广播
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {  //关联成功
                Log.i(TAG, "Only gatt, just wait");
                mListener.bleGattConnect();

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) { //断开连接
                mListener.bleDisConnect();

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) //建立蓝牙服务
            {
                mListener.bleServerConnect();
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { //收到数据
                byte[] data = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);

                mListener.bleData(data);

            }
        }
    };

}
