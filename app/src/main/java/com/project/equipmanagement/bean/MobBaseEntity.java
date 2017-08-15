package com.project.equipmanagement.bean;

import java.io.Serializable;

/**
 * Created by maning on 2017/4/11.
 */

public class MobBaseEntity<T> implements Serializable {
    private static final long serialVersionUID = -4553802208756427393L;

    private String msg;


    private int code;

    private T result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "MobBaseEntity{" +
                "msg='" + msg + '\'' +
                ", code='" + code + '\'' +
                ", result=" + (result == null ? "" : result.toString())+
                '}';
    }
}
