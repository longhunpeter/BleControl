package peter.blepro.listener;

/**
 * Created by lxl on 2014/8/25.
 * 监听蓝牙连接
 */
public interface BleOperationListener {
    public void bleConnect();
    public void bleDisConnect();
    public void bleData(byte[] bytes);
}
