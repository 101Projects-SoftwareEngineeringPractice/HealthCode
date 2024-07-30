package org.software.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AddNucleicAcidTestRecordByIDRequest {

    @NotBlank(message = "identity_card不能为空")
    @JsonProperty("identity_card")
    private String identity_card;

    @NotNull(message = "kind不能为空")
    @JsonProperty("kind")
    private Integer kind;

    @NotNull(message = "tubeid不能为空")
    @JsonProperty("tubeid")
    private Long tubeid;

    @NotBlank(message = "test_address不能为空")
    @JsonProperty("test_address")
    private String test_address;

    // Getters and Setters
    public String getIdentity_card() {
        return identity_card;
    }

    public void setIdentity_card(String identity_card) {
        this.identity_card = identity_card;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public Long getTubeid() {
        return tubeid;
    }

    public void setTubeid(Long tubeid) {
        this.tubeid = tubeid;
    }

    public String getTest_address() {
        return test_address;
    }

    public void setTest_address(String test_address) {
        this.test_address = test_address;
    }
}
