package com.project.equipmanagement.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.lvrenyang.io.BTPrinting;
import com.lvrenyang.io.IOCallBack;
import com.lvrenyang.io.Label;
import com.lvrenyang.io.Page;
import com.project.equipmanagement.R;
import com.project.equipmanagement.utils.Prints;
import com.project.equipmanagement.utils.UserUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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


    PrintQRcodeActivity mPrintActivity;
    private BroadcastReceiver broadcastReceiver = null;
    private IntentFilter intentFilter = null;


    ExecutorService es = Executors.newScheduledThreadPool(30);
    Page mPage = new Page();
    Label mLabel = new Label();
    BTPrinting mBt = new BTPrinting();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_qrcode);
        ButterKnife.bind(this);
        this.setTitle("搜索到的设备");
        mPrintActivity = this;
          /* 启动蓝牙 */
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (null != adapter) {
            if (!adapter.isEnabled()) {
                if (!adapter.enable()) {
                    finish();
                    return;
                }
            }
        }
        mBt.SetCallBack(this);
        mPage.Set(mBt);
        mLabel.Set(mBt);

        if (!adapter.isEnabled()) {
            if (adapter.enable()) {
                while (!adapter.isEnabled())
                    ;
            } else {
                finish();
                return;
            }
        }

        adapter.cancelDiscovery();
        lvDevice.removeAllViews();
        adapter.startDiscovery();

        initBroadcast();

        btnPrint.setEnabled(false);
        btnDisconnect.setEnabled(false);
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
    }

    public static Bitmap getBitmap(String path) throws IOException {

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
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
                Toast.makeText(mPrintActivity, "Connected", Toast.LENGTH_SHORT)
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
                Toast.makeText(mPrintActivity, "Failed", Toast.LENGTH_SHORT).show();
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


    private void initBroadcast() {
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                String action = intent.getAction();
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    if (device == null)
                        return;
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
                            Toast.makeText(mPrintActivity, "Connecting...",
                                    Toast.LENGTH_SHORT).show();
//                            btnSearch.setEnabled(false);
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
                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED
                        .equals(action)) {
                    showProgressDialog("正在搜索蓝牙设备");
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                        .equals(action)) {
                    dissmissProgressDialog();
//                    es.submit(new TaskLoadUrlBitmap());
                }

            }

        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver, intentFilter);
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
