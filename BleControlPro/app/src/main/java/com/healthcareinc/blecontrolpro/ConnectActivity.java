package com.healthcareinc.blecontrolpro;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;


/**
 * Created by xianglei on 15/9/2.
 */
public class ConnectActivity extends BleBaseActivity implements View.OnClickListener {
    private Button mConnectBtn;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_layout);
        address = getIntent().getStringExtra("devAddress");
        mConnectBtn = (Button) findViewById(R.id.test_connect_btn);
        mConnectBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_connect_btn:
                if (TextUtils.isEmpty(address)) {
                    makeToast("mac address is null");
                    return;
                }
                Log.i("peter","address is:" +address);
                bleControl.connectDev(address);
                break;
        }
    }

    @Override
    public void bleServerOk() {

    }

    @Override
    public void bleGattConnect() {

    }

    @Override
    public void bleServerConnect() {
        makeToast("连接成功");
    }

    @Override
    public void bleDisConnect() {
        makeToast("连接失败");
    }

    @Override
    public void bleData(byte[] bytes) {

    }
}
