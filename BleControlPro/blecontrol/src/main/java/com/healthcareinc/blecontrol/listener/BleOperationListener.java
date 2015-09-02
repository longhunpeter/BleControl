package com.healthcareinc.blecontrol.listener;

/**
 * Created by lxl on 2014/8/25.
 * 监听蓝牙连接
 */
public interface BleOperationListener {
    public void bleServerOk();
    public void bleGattConnect();
    public void bleServerConnect();
    public void bleDisConnect();
    public void bleData(byte[] bytes);
}
