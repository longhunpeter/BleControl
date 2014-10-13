package peter.blepro.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;

import org.apache.http.impl.auth.UnsupportedDigestAlgorithmException;

/**
 * Created by lxl on 2014/8/22.
 * 蓝牙扫描.
 */
public class BleScan {

    private BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    public boolean mScanning;
    private LeScanCallback mLeScanCallback;
    private static BleScan bleScan;

    public BleScan(Context context) {
        this.mContext = context;
        supportBle();
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
     * 设置扫描监听.
     *
     * @param leScanCallback
     */
    public void setmLeScanCallback(LeScanCallback leScanCallback) {
        this.mLeScanCallback = leScanCallback;
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
    private void scanDevice(final boolean isEnable, final long scanPeriod) {
        if (isEnable) {
            // Stops scanning after a pre-defined scan period.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mScanning) {
                        mScanning = false;
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    }
                }
            }, scanPeriod);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    /**
     * 检测是否支持蓝牙4.0
     */
    private void supportBle() {
        final BluetoothManager bluetoothManager = (BluetoothManager)
                mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {

            throw new UnsupportedDigestAlgorithmException("该设备不支持蓝颜4.0");

        }
    }

}
