package com.project.equipmanagement.http;

import com.project.equipmanagement.bean.InfoCount;
import com.project.equipmanagement.bean.MobBaseEntity;
import com.project.equipmanagement.bean.MobUserInfo;
import com.project.equipmanagement.bean.ProcessItemInfo;
import com.project.equipmanagement.constant.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by AnnieYe on 17/8/12 22:13.
 * email:15191755477@163.com
 */
public interface APIService {

    //用户登录
    // http://192.168.1.109:8088/equip/a/index/login?username=banzua&password=e35c9e19691be326d7b30f68548ed498
    @GET(Constants.BASEURL + "index/login")
    Call<MobBaseEntity<MobUserInfo>> userLogin(@Query("username") String username,
                                               @Query("password") String password
    );


    //获取流程对应条数
    @GET(Constants.BASEURL + "flowBase/index/json")
    Call<MobBaseEntity<InfoCount>>  getFlowCount(@Query("user_name") String username,
                                                 @Query("user_msg") String userMsg

    );

    //获取具体流程
    @GET(Constants.BASEURL + "flowBase/list/json")
    Call<MobBaseEntity<ArrayList<ProcessItemInfo>>> getFlows(@Query("user_name") String username,
                                                            @Query("user_msg") String userMsg

    );


}
