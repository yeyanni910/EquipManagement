package com.project.equipmanagement.bean;

import java.io.Serializable;

/**
 * Created by AnnieYe on 17/8/17 18:26.
 * email:15191755477@163.com
 */
public class DeviceInfo implements Serializable {
    private int id;
    private String equipNo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEquipNo() {
        return equipNo;
    }

    public void setEquipNo(String equipNo) {
        this.equipNo = equipNo;
    }

    @Override
    public String toString() {
        return  "deviceInfo {" +
                "  id ='" + id + '\'' +
                ", equipNo ='" + equipNo + '\'' +
                '}';
    }
}
