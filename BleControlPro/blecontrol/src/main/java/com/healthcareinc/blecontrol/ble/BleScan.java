package com.healthcareinc.blecontrol.ble;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;

import com.healthcareinc.blecontrol.listener.BleScanListener;


/**
 * Created by lxl on 2014/8/22.
 * 蓝牙扫描.
 */
public class BleScan {
    private BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    public boolean mScanning;
    private BleScanListener mListener;
    private LeScanCallback mLeScanCallback;
    private static BleScan bleScan;
    private boolean isSupportBle;

    public BleScan(Context context) {
        this.mContext = context;
        isSupportBle = supportBle();
    }

    /**
     * 初始化方法
     *
     * @param context
     * @return BleScan
     */
    public static BleScan initial(Context context) {
        if (bleScan == null) bleScan = new BleScan(context);
        return bleScan;
    }

    /**
     * 判断是否支持ble4.0
     *
     * @return
     */
    public boolean isSupportBle() {
        return isSupportBle;
    }

    /**
     * 判断蓝牙时候打开
     *
     * @return
     */
    public boolean bleIsEnabled() {
        if (mBluetoothAdapter == null) {
            return false;
        }
        return mBluetoothAdapter.isEnabled();
    }

    /**
     * 打开蓝牙
     *
     * @return
     */
    public boolean openBle() {
        return mBluetoothAdapter.enable();

    }

    /**
     * 设置扫描监听.
     *
     * @param leScanCallback
     */
    public void setLeScanCallback(LeScanCallback leScanCallback) {
        this.mLeScanCallback = leScanCallback;
    }


    public void setBleOnlistener(BleScanListener listener) {
        this.mListener = listener;
    }


    /**
     * 启动扫描
     *
     * @param scanPeriod 扫描周期（以毫秒为单位）
     */
    public void startScan(int scanPeriod) {
        if (mLeScanCallback != null && mBluetoothAdapter.isEnabled()) scanDevice(true, scanPeriod);


    }

    /**
     * 停止扫描
     *
     * @param scanPeriod 扫描周期（以毫秒为单位）
     */
    public void stopScan(int scanPeriod) {
        if (mLeScanCallback != null && mScanning) scanDevice(false, scanPeriod);
    }


    /**
     * 扫描设备
     *
     * @param isEnable   扫描或暂停扫描标志位
     * @param scanPeriod 扫描周期（以毫秒为单位）
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void scanDevice(final boolean isEnable, final long scanPeriod) {
        if (isEnable) {
            // Stops scanning after a pre-defined scan period.
            new Handler().postDelayed(new Runnable() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public void run() {
                    if (mScanning) {
                        mScanning = false;
                        mListener.bleScanFinish();
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    }
                }
            }, scanPeriod);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
//            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    /**
     * 检测是否支持蓝牙4.0
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private boolean supportBle() {
        final BluetoothManager bluetoothManager = (BluetoothManager)
                mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {

//            throw new UnsupportedDigestAlgorithmException("该设备不支持蓝颜4.0");
            return false;
        }

        return true;
    }

}
