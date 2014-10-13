package ichronocloud.blepro.uitl;

import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;

import ichronocloud.blepro.server.BluetoothLeService;


/**
 * Created by lxl on 14-3-11.
 */
public class Utils {

    /**
     * 注册接收的事件
     *
     * @return
     */
    public static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
        return intentFilter;
    }


    /**
     * @param hex
     * @return
     */
    public static byte[] hex2Byte(String... hex) {
        String digital = "0123456789ABCDEF";
        byte[] bytes = new byte[hex.length];
        int index = 0;
        for (String string : hex) {
            if (index > 1 && index < 4) {
                string = Integer.toHexString(Integer.parseInt(string))
                        .toUpperCase();
            }
            if (string.length() == 1) {
                string = "0" + string;
            }
            char[] hex2char = string.toCharArray();
            int temp;
            temp = digital.indexOf(hex2char[0]) * 16;
            temp += digital.indexOf(hex2char[1]);
            bytes[index] = (byte) (temp & 0xff);
            index++;
        }
        return bytes;
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] mByte = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            mByte[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return mByte;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /**
     * 将字节数组转换为字符串
     *
     * @param bytes
     * @return
     */
    public static String byteToString(byte[] bytes) {
        if (bytes.length <= 0) {
            return null;
        }
        final StringBuilder stringBuilder = new StringBuilder(bytes.length);
        for (byte byteChar : bytes)
            stringBuilder.append(String.format("%02X ", byteChar) + "\n");
        return stringBuilder.toString();

    }

    /**
     * 将字节数组
     *
     * @param bytes
     * @return
     */
    public static String[] byteToStringArrays(byte[] bytes) {
        int len = bytes.length;
        if (bytes.length <= 0) {
            return null;
        }
        String[] arrays = new String[len];
        for (int i = 0; i < len; i++) {
            arrays[i] = String.format("%02X ", bytes[i]);
        }
        return arrays;
    }


}
