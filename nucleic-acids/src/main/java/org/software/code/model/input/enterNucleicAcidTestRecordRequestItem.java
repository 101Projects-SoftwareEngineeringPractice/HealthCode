package org.software.code.model.input;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class enterNucleicAcidTestRecordRequestItem {
    @NotNull(message = "tubeid不能为空")
    @JsonProperty("tubeid")
    private Long tubeid;
    @NotNull(message = "kind不能为空")
    @JsonProperty("kind")
    private Integer kind;
    @NotNull(message = "result不能为空")
    @JsonProperty("result")
    private Integer result;
    @NotNull(message = "testing_organization不能为空")
    @JsonProperty("testing_organization")
    private String testingOrganization;

    public Long getTubeid() {
        return tubeid;
    }

    public void setTubeid(Long tubeid) {
        this.tubeid = tubeid;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getTestingOrganization() {
        return testingOrganization;
    }

    public void setTestingOrganization(String testingOrganization) {
        this.testingOrganization = testingOrganization;
    }

}
