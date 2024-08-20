package org.software.code.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ScanPlaceCodeRequest {

    @NotNull(message = "uid不能为空")
    @JsonProperty("uid")
    private Long uid;

    @NotBlank(message = "token不能为空")
    @JsonProperty("token")
    private String token;

    // Getters and Setters
    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
