package org.software.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class PidTokenInput {

    @NotBlank(message = "token(pid)不能为空")
    @JsonProperty("token")
    private String token;

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
