package org.software.code.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AddNucleicAcidTestRecordByIDRequest {

    @NotBlank(message = "identity_card不能为空")
    @JsonProperty("identity_card")
    private String identityCard;

    @NotNull(message = "kind不能为空")
    @JsonProperty("kind")
    private Integer kind;

    @NotNull(message = "tubeid不能为空")
    @JsonProperty("tubeid")
    private Long tubeid;

    @NotBlank(message = "test_address不能为空")
    @JsonProperty("test_address")
    private String testAddress;

    
    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
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

    public String getTestAddress() {
        return testAddress;
    }

    public void setTestAddress(String testAddress) {
        this.testAddress = testAddress;
    }
}
