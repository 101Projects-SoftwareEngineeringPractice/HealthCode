package org.software.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class UidInput {

    @NotNull(message = "uid不能为空")
    @JsonProperty("uid")
    private Long uid;

    // Getters and Setters
    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
