package org.software.code.model.entity;

import java.util.Date;
import java.io.Serializable;

public class UidMapping implements Serializable {

    private Long uid;

    private String openid;

    private Date createdAt;


    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}

