package com.project.equipmanagement.bean;

import java.io.Serializable;

/**
 * Created by AnnieYe on 17/8/14 15:56.
 * email:15191755477@163.com
 */
public class InfoCount implements Serializable {
    private int myCurrentFlow;
    private int myTasking;
    private int myInitiatorFlow;

    public int getMyCurrentFlow() {
        return myCurrentFlow;
    }

    public void setMyCurrentFlow(int myCurrentFlow) {
        this.myCurrentFlow = myCurrentFlow;
    }

    public int getMyTasking() {
        return myTasking;
    }

    public void setMyTasking(int myTasking) {
        this.myTasking = myTasking;
    }

    public int getMyInitiatorFlow() {
        return myInitiatorFlow;
    }

    public void setMyInitiatorFlow(int myInitiatorFlow) {
        this.myInitiatorFlow = myInitiatorFlow;
    }

    @Override
    public String toString() {
        return  "InfoCount {" +
                "myCurrentFlow='" + myCurrentFlow + '\'' +
                ", myTasking ='" + myTasking + '\'' +
                ", myInitiatorFlow ='" + myInitiatorFlow + '\'' +
                '}';
    }
}
