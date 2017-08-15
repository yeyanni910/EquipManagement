package com.project.equipmanagement.bean;

import java.io.Serializable;

/**
 * Created by AnnieYe on 17/8/14 19:13.
 * email:15191755477@163.com
 */
public class ProcessItemInfo implements Serializable {

    private int id;
    private String description;
    private String flowName;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "processItemInfo {" +
                "id='" + id + '\'' +
                ", description ='" + description + '\'' +
                ", flowName ='" + flowName + '\'' +
                ", url ='" + url + '\'' +
                '}';
    }
}
