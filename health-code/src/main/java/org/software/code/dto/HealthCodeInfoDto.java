package org.software.code.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

public class HealthCodeInfoDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private long uid;
    private String name;
    private int status;
    private String identity_card;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIdentity_card() {
        return identity_card;
    }

    public void setIdentity_card(String identity_card) {
        this.identity_card = identity_card;
    }
}
