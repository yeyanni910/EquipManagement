package com.project.equipmanagement.http;

import android.util.Log;

import com.project.equipmanagement.R;
import com.project.equipmanagement.app.MyApplication;
import com.project.equipmanagement.bean.InfoCount;
import com.project.equipmanagement.bean.MobBaseEntity;
import com.project.equipmanagement.bean.MobUserInfo;
import com.project.equipmanagement.bean.ProcessItemInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by AnnieYe on 17/8/12 22:21.
 * email:15191755477@163.com
 */
public class MobApi {

    private static final String TAG = MobApi.class.getName();
    public final static String GET_DATA_FAIL = MyApplication.getInstance().getString(R.string.equip_get_data_fail);
    public final static String NET_FAIL = MyApplication.getInstance().getString(R.string.equip_net_fail);

    /**
     * 用户登录
     * @param userName
     * @param userPsd
     * @param what
     * @param myCallBack
     * @return
     */
    public static Call<MobBaseEntity<MobUserInfo>> userLogin(String userName, String userPsd, final int what, final MyCallBack myCallBack) {

        Call<MobBaseEntity<MobUserInfo>> call = BuildApi.getAPIService().userLogin(userName, userPsd);
        call.enqueue(new Callback<MobBaseEntity<MobUserInfo>>() {
            @Override
            public void onResponse(Call<MobBaseEntity<MobUserInfo>> call, Response<MobBaseEntity<MobUserInfo>> response) {
                if (response.isSuccessful()) {
                    MobBaseEntity body = response.body();
                    if (body != null) {
                        if (body.getMsg().equals("成功")) {
                            Log.i("USERLOGIN","userLogin---success：" + body.toString());
                            myCallBack.onSuccess(what, body.getResult());
                        } else {
                            myCallBack.onFail(what, body.getMsg());
                        }
                    } else {
                        myCallBack.onFail(what, GET_DATA_FAIL);
                    }
                } else {
                    myCallBack.onFail(what, GET_DATA_FAIL);
                }
            }

            @Override
            public void onFailure(Call<MobBaseEntity<MobUserInfo>> call, Throwable t) {
                Log.e(TAG,"userLogin-----onFailure：" + t.toString());
                //数据错误
                myCallBack.onFail(what, NET_FAIL);
            }
        });

        return call;
    }

    /**
     * 获取流程条数
     * @param username
     * @param userMsg
     * @param what
     * @param myCallBack
     * @return
     */
    public static Call<MobBaseEntity<InfoCount>> getFlowCount(String username,String userMsg,final int what,final MyCallBack myCallBack){
        Call<MobBaseEntity<InfoCount>> call = BuildApi.getAPIService().getFlowCount(username,userMsg);
        call.enqueue(new Callback<MobBaseEntity<InfoCount>>() {
            @Override
            public void onResponse(Call<MobBaseEntity<InfoCount>> call, Response<MobBaseEntity<InfoCount>> response) {
                if (response.isSuccessful()){
                    MobBaseEntity body = response.body();
                    if (body != null){
                        if (body.getMsg().equals("成功")){
                            Log.i("getFlowCount","flowCont---success：" + body.toString());
                            myCallBack.onSuccess(what,body.getResult());
                        }else {
                            myCallBack.onFail(what,body.getMsg());
                        }
                    }else {
                        myCallBack.onFail(what,GET_DATA_FAIL);
                    }
                }else {
                    myCallBack.onFail(what,GET_DATA_FAIL);
                }
            }

            @Override
            public void onFailure(Call<MobBaseEntity<InfoCount>> call, Throwable t) {
                Log.e(TAG,"getFlowCount-----onFailure：" + t.toString());
                myCallBack.onFail(what, NET_FAIL);
            }
        });
        return call;
    }

    /**
     * 获取具体流程
     * @param username
     * @param userMsg
     * @param what
     * @param myCallBack
     * @return
     */
    public static Call<MobBaseEntity<ArrayList<ProcessItemInfo>>> getFlows(String username, String userMsg,final int what,final MyCallBack myCallBack){
        Call<MobBaseEntity<ArrayList<ProcessItemInfo>>> call = BuildApi.getAPIService().getFlows(username,userMsg);
        call.enqueue(new Callback<MobBaseEntity<ArrayList<ProcessItemInfo>>>() {
            @Override
            public void onResponse(Call<MobBaseEntity<ArrayList<ProcessItemInfo>>> call, Response<MobBaseEntity<ArrayList<ProcessItemInfo>>> response) {
            if (response.isSuccessful()){
                MobBaseEntity body = response.body();
                if (body != null){
                    Log.i("getFlows","flows----success"+ body.toString());
                    myCallBack.onSuccess(what,body.getResult());
                }else {
                    myCallBack.onFail(what,GET_DATA_FAIL);
                }
            }else{
                myCallBack.onFail(what,GET_DATA_FAIL);
            }
            }

            @Override
            public void onFailure(Call<MobBaseEntity<ArrayList<ProcessItemInfo>>> call, Throwable t) {
                Log.e(TAG,"getFlows------onFailure:" + t.toString());
                myCallBack.onFail(what,NET_FAIL);
            }
        });
        return call;
    }
}
