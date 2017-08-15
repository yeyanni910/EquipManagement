package com.project.equipmanagement.http;

import java.util.List;

/**
 * Created by AnnieYe on 17/8/12 22:15.
 * email:15191755477@163.com
 */
public interface MyCallBack {
    /**
     * 成功的回调对象
     *
     * @param what
     * @param result
     */
    void onSuccess(int what, Object result) ;

    /**
     * 成功的回调集合
     *
     * @param what
     * @param results
     */
    void onSuccessList(int what, List results);

    /**
     * 失败的回调
     *
     * @param what
     * @param result
     */
    void onFail(int what, String result);
}
