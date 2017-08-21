package com.project.equipmanagement.utils;


import android.util.Log;

import com.project.equipmanagement.app.MyApplication;
import com.project.equipmanagement.bean.DeviceInfo;
import com.project.equipmanagement.bean.MobUserInfo;

import java.util.List;

/**
 * Created by maning on 2017/4/1.
 */

public class UserUtils {

    private static final String cache_UserLogin = "cache_UserLogin";

    private static final String cache_DeviceInfo = "cache_DeviceInfo";

    //退出登录
    public static void quitLogin() {
        MyApplication.getACache().put(cache_UserLogin, new MobUserInfo());
    }

    //用户登录信息
    public static void saveUserCache(MobUserInfo userInfo) {
        if (userInfo != null) {
            MyApplication.getACache().put(cache_UserLogin, userInfo);
        }
    }

    //获取用户信息
    public static MobUserInfo getUserCache() {
        MobUserInfo userInfo = (MobUserInfo) MyApplication.getACache().getAsObject(cache_UserLogin);
        if (userInfo == null) {
            userInfo = new MobUserInfo();
        }
        return userInfo;
    }

    //保存设备信息
    public static void saveDeviceCache(List<DeviceInfo> deviceInfos) {
        Log.e(UserUtils.class.getName(),deviceInfos.size()+"");
        DeviceInfo[] deviceInfosArray = new DeviceInfo[deviceInfos.size()];

        deviceInfos.toArray(deviceInfosArray);
        if( deviceInfos !=null) {
        MyApplication.getACache().put(cache_DeviceInfo, deviceInfosArray);
    }
}

    //获取设备信息
    public static List<DeviceInfo> getDeviceInfos(){
        List<DeviceInfo> deviceInfoList = null;
        DeviceInfo[] deviceInfos = (DeviceInfo[]) MyApplication.getACache().getAsObject(cache_DeviceInfo);
        if (deviceInfos == null){
            return deviceInfoList;
        }
        deviceInfoList = java.util.Arrays.asList(deviceInfos);
        Log.e(UserUtils.class.getName(),deviceInfoList.size()+"get");
        return deviceInfoList;
    }

}
