package org.software.code.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;

public class CreateNucleicAcidRequest {

    @NotBlank(message = "identity_card不能为空")
    @JsonProperty("identity_card")
    private String identityCard;

    @NotBlank(message = "name不能为空")
    @JsonProperty("name")
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
