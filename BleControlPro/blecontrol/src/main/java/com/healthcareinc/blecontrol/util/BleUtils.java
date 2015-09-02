package com.healthcareinc.blecontrol.util;

import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;

import com.healthcareinc.blecontrol.service.BluetoothLeService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by lxl on 14-3-11.
 */
public class BleUtils {

    /**
     * 注册接收的事件
     *
     * @return
     */
    public static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SPECIAL_EIGHT);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
        return intentFilter;
    }


    /**
     * @param hex
     * @return byte[]
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
            // 其实和上面的函数是一样的 multiple 16 就是右移4位 这样就成了高4位了
            // 然后和低四位相加， 相当于 位操作"|"
            // 相加后的数字 进行 位 "&" 操作 防止负数的自动扩展. {0xff byte最大表示数}
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
     * @return String
     */
    public static String byteToStringHex(byte[] bytes) {
        if (bytes.length <= 0) {
            return null;
        }
        final StringBuilder stringBuilder = new StringBuilder(bytes.length);
        for (byte byteChar : bytes) {
            if (String.format("%02X", byteChar).equals("00")) {

                stringBuilder.append("\\0");
            } else {
                String format = String.format("%02X", byteChar);
                int parseInt = Integer.parseInt(format, 16);
                stringBuilder.append((char) parseInt);
            }
        }
        return stringBuilder.toString();

    }

    /**
     * 将字节数组转换为字符串
     *
     * @param bytes
     * @return String
     */
    public static String byteToString(byte[] bytes) {
        if (bytes == null) {
//            Log.i("PETER", "bytes is null--------");
        }
        if (bytes.length <= 0) {
            return null;
        }
        final StringBuilder stringBuilder = new StringBuilder(bytes.length);
        for (byte byteChar : bytes)
            stringBuilder.append(String.format("%02X ", byteChar) + "\n");
        return stringBuilder.toString();

    }

    /**
     * 将字节数组转为字符串数组
     *
     * @param bytes
     * @return String[]
     */
    public static String[] byteToStringArrays(byte[] bytes) {
        int len = bytes.length;
        if (bytes.length <= 0) {
            return null;
        }
        String[] arrays = new String[len];
        for (int i = 0; i < len; i++) {
            arrays[i] = String.format("%02X", bytes[i]);
        }
        return arrays;
    }

    /**
     * 获取当前时间
     *
     * @param timeStyle
     * @return String
     */
    public static String getCurrentTime(String timeStyle) {
        SimpleDateFormat formatter = new SimpleDateFormat(timeStyle);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String timeValue = formatter.format(curDate);

        return timeValue;

    }

    /**
     * @param date 长度十位的数字字符串
     * @return String
     */
    public static String dateFormat(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        date = date.replaceAll("[^0-9]", "");
        Calendar c = Calendar.getInstance();
//			String date =""+1403191630;
        if (date.length() == 10) {
            int yy = Integer.parseInt(date.substring(0, 2));
            int MM = Integer.parseInt(date.substring(2, 4));
            int dd = Integer.parseInt(date.substring(4, 6));
            int hh = Integer.parseInt(date.substring(6, 8));
            int mm = Integer.parseInt(date.substring(8, 10));
            c.set((2000 + yy), MM, dd, hh, mm);
        }
        Date mDate = c.getTime();

        return format.format(mDate);
    }


}
