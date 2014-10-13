BleOperation
============

描述：
----------------
ble 蓝牙的简单封装,主要用于与硬件端的交互，简单实现蓝牙的扫描、连接、写入和接收.

注意事项：
-----------------
使用时请注意BlutoothLeServer 中使用自己的UUID, 本例中“UUID_NOTIFY”，“UUID_SERVICE”为部分蓝牙通用UUID,"UUID_WRITE" 为自写蓝牙模块写入UUID.
