package org.software.code.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class AddNucleicAcidTestRecordByTokenRequest {

    @NotNull(message = "token不能为空")
    @JsonProperty("token")
    private String token;

    @NotNull(message = "kind不能为空")
    @JsonProperty("kind")
    private Integer kind;

    @NotNull(message = "tubeid不能为空")
    @JsonProperty("tubeid")
    private Long tubeid;

    @NotNull(message = "test_address不能为空")
    @JsonProperty("test_address")
    private String testAddress;

    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
