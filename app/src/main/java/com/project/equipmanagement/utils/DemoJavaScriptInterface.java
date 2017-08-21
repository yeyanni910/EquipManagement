package com.project.equipmanagement.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.equipmanagement.bean.DeviceInfo;
import com.project.equipmanagement.constant.Constants;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by AnnieYe on 16/9/1 16:02.
 * email:15191755477@163.com
 */

public class DemoJavaScriptInterface {
    Context mContxt;
    Handler mHandler;

    //sdk17版本以上加上注解
    public DemoJavaScriptInterface(Context mContxt, Handler handler) {
        this.mContxt = mContxt;
        this.mHandler = handler;
    }

    /**
     *js交互之后，弹打印框
     */
    @JavascriptInterface
    public void printQrCode(String json) {
        Type type = new TypeToken<List<DeviceInfo>>() {
        }.getType();
        List<DeviceInfo> checkVersionBean = new Gson().fromJson(json, type);

        UserUtils.saveDeviceCache(checkVersionBean);

        Log.e(DemoJavaScriptInterface.class.getName(),"=========="+json.toString());
        Message msg = new Message();
        if (checkVersionBean != null){
            msg.what = Constants.CONSTANT_PUSH_PRINT_DIALOG;
        }else{
            msg.what =Constants.CONSTANT_SHOW_NO_MESSAGE_DIALOG;
        }
        mHandler.sendMessage(msg);
    }
}