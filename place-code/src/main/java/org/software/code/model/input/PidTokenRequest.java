package org.software.code.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class PidTokenRequest {

    @NotBlank(message = "token不能为空")
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
