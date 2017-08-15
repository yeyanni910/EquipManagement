package com.project.equipmanagement.bean;

import java.io.Serializable;

/**
 * Created by AnnieYe on 17/8/12 22:22.
 * email:15191755477@163.com
 */
public class MobUserInfo implements Serializable {

    private String teamName;
    private String userName;
    private String userId;
    private String userPsd;
    private int teamId;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPsd() {
        return userPsd;
    }

    public void setUserPsd(String userPsd) {
        this.userPsd = userPsd;
    }


    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }


    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    @Override
    public String toString() {
       return "MobUserInfo {" +
                "teamName='" + teamName + '\'' +
                ", userName ='" + userName + '\'' +
               ", userPsd ='" + userPsd + '\'' +
               ", userId ='" + userId + '\'' +
                ", teamId='" + teamId + '\'' +
                '}';
    }
}
