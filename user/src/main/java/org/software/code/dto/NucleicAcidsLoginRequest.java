package org.software.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class NucleicAcidsLoginRequest {

    @NotBlank(message = "identity_card不能为空")
    @JsonProperty("identity_card")
    private String identityCard;

    @NotBlank(message = "password不能为空")
    @JsonProperty("password")
    private String password;

    // Getters and Setters
    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
