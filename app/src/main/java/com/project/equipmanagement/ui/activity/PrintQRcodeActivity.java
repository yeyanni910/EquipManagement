package com.project.equipmanagement.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lvrenyang.io.BTPrinting;
import com.lvrenyang.io.IOCallBack;
import com.lvrenyang.io.Label;
import com.lvrenyang.io.Page;
import com.project.equipmanagement.R;
import com.project.equipmanagement.utils.Prints;
import com.project.equipmanagement.utils.UserUtils;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrintQRcodeActivity extends BaseActivity implements IOCallBack {

    @Bind(R.id.lv_device)
    LinearLayout lvDevice;
    @Bind(R.id.sv_device)
    ScrollView svDevice;
    @Bind(R.id.btn_disconnect)
    Button btnDisconnect;
    @Bind(R.id.btn_print)
    Button btnPrint;
    @Bind(R.id.btn_search_bluetooth)
    Button btnSearchBluetooth;
    @Bind(R.id.tv_print_title)
    TextView tvPrintTitle;
    @Bind(R.id.pb_search)
    ProgressBar pbSearch;

    PrintQRcodeActivity mPrintActivity;
    private BroadcastReceiver broadcastReceiver = null;
    private IntentFilter intentFilter = null;


    ExecutorService es = Executors.newScheduledThreadPool(30);
    Page mPage = new Page();
    Label mLabel = new Label();
    BTPrinting mBt = new BTPrinting();
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_qrcode);
        ButterKnife.bind(this);
        mPrintActivity = this;
          /* 启动蓝牙 */

        mBt.SetCallBack(this);
        mPage.Set(mBt);
        mLabel.Set(mBt);

        initConnectDevice();

        initBroadcast();
        pbSearch.setVisibility(View.GONE);

        btnPrint.setEnabled(false);
        btnDisconnect.setEnabled(false);
    }

    private void initConnectDevice() {
        if (adapter.enable()) {
            Set<BluetoothDevice> devices = adapter.getBondedDevices();
            if (devices != null) {
                tvPrintTitle.setText("已配对蓝牙设备：");
                if (devices.size() > 0) {
                    for (Iterator<BluetoothDevice> it = devices.iterator(); it.hasNext(); ) {
                        BluetoothDevice device = it.next();
                        //自动连接已有蓝牙设备
//                                createBond(device, null);
                        setLvDevice(device, mPrintActivity);
                    }
                }
            } else {
                tvPrintTitle.setText("搜索到的蓝牙设备");
                adapter.cancelDiscovery();
                lvDevice.removeAllViews();
                adapter.startDiscovery();
            }
        }else {
            finish();
            return;
        }
    }

    /**
     * print
     */
    @OnClick(R.id.btn_print)
    void print() {
        btnPrint.setEnabled(false);
        es.submit(new TaskPrint(mLabel));
    }

    /**
     * disconnect
     */
    @OnClick(R.id.btn_disconnect)
    void disconnect() {
        es.submit(new TaskClose(mBt));
    }

    /**
     * create qrCode
     */
    @OnClick(R.id.btn_search_bluetooth)
    void searchBlueTooth() {
        //TODO 搜索蓝牙设备
        adapter.cancelDiscovery();
        lvDevice.removeAllViews();
        adapter.startDiscovery();
    }


    public class TaskOpen implements Runnable {
        BTPrinting bt = null;
        String address = null;
        Context context = null;

        public TaskOpen(BTPrinting bt, String address, Context context) {
            this.bt = bt;
            this.address = address;
            this.context = context;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            bt.Open(address, context);
        }
    }

    public class TaskPrint implements Runnable {
        Label label = null;

        public TaskPrint(Label page) {
            this.label = page;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub

            final boolean bPrintResult = Prints.PrintLabel(getApplicationContext(), label, 384, 320, UserUtils.getDeviceInfos());
            showProgressDialog("正在打印");
            final boolean bIsOpened = label.GetIO().IsOpened();

            mPrintActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Toast.makeText(
                            mPrintActivity.getApplicationContext(),
                            bPrintResult ? getResources().getString(
                                    R.string.str_print_success) : getResources()
                                    .getString(R.string.str_print_failed),
                            Toast.LENGTH_SHORT).show();
                    dissmissProgressDialog();
                    mPrintActivity.btnPrint.setEnabled(bIsOpened);
                }
            });

        }
    }

    public class TaskClose implements Runnable {
        BTPrinting bt = null;

        public TaskClose(BTPrinting bt) {
            this.bt = bt;
        }

        @Override
        public void run() {
            bt.Close();
        }

    }

    @Override
    public void OnOpen() {
        // TODO Auto-generated method stub
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                btnPrint.setEnabled(true);
                btnDisconnect.setEnabled(true);
                lvDevice.setEnabled(false);
                for (int i = 0; i < lvDevice.getChildCount(); ++i) {
                    Button btn = (Button) lvDevice.getChildAt(i);
                    btn.setEnabled(false);
                }
                dissmissProgressDialog();
                Toast.makeText(mPrintActivity, "连接成功", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    public void OnOpenFailed() {
        // TODO Auto-generated method stub
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                btnDisconnect.setEnabled(false);
                btnPrint.setEnabled(false);
                lvDevice.setEnabled(true);
                for (int i = 0; i < lvDevice.getChildCount(); ++i) {
                    Button btn = (Button) lvDevice.getChildAt(i);
                    btn.setEnabled(true);
                }
                dissmissProgressDialog();
                Toast.makeText(mPrintActivity, "连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnClose() {
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                btnDisconnect.setEnabled(false);
                btnPrint.setEnabled(false);
//                btnSearch.setEnabled(true);
                lvDevice.setEnabled(true);
                for (int i = 0; i < lvDevice.getChildCount(); ++i) {
                    Button btn = (Button)
                            lvDevice.getChildAt(i);
                    btn.setEnabled(true);
                }
            }
        });
    }

    /**
     * 初始化广播
     */
    private void initBroadcast() {
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                String action = intent.getAction();
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                tvPrintTitle.setText("搜索到的蓝牙设备：");
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    if (device == null)
                        return;
                    setLvDevice(device, mPrintActivity);
                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED
                        .equals(action)) {
                    pbSearch.setVisibility(View.VISIBLE);
                    pbSearch.setIndeterminate(true);
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                        .equals(action)) {
                    dissmissProgressDialog();
                    pbSearch.setVisibility(View.GONE);
                    pbSearch.setIndeterminate(false);
                }

            }

        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void setLvDevice(BluetoothDevice device, Context context) {
        final String address = device.getAddress();
        String name = device.getName();
        if (name == null)
            name = "BT";
        else if (name.equals(address))
            name = "BT";
        Button button = new Button(context);
        button.setText(name + ": " + address);

        for (int i = 0; i < lvDevice.getChildCount(); ++i) {
            Button btn = (Button) lvDevice.getChildAt(i);
            if (btn.getText().equals(button.getText())) {
                return;
            }
        }
        button.setGravity(Gravity.CENTER_VERTICAL
                | Gravity.LEFT);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                showProgressDialog("正在连接蓝牙");
                lvDevice.setEnabled(false);
                for (int i = 0; i < lvDevice
                        .getChildCount(); ++i) {
                    Button btn = (Button) lvDevice
                            .getChildAt(i);
                    btn.setEnabled(false);
                }
                btnDisconnect.setEnabled(false);
                btnPrint.setEnabled(false);
                es.submit(new TaskOpen(mBt, address, mPrintActivity));
            }
        });
        button.getBackground().setAlpha(100);
        lvDevice.addView(button);
    }


    private void uninitBroadcast() {
        if (broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uninitBroadcast();
        btnDisconnect.performClick();
    }
}
