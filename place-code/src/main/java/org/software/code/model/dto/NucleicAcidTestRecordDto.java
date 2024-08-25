package org.software.code.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class NucleicAcidTestRecordDto {
    @JsonProperty("created_at")
    private String createdAt;
    private int result;
    @JsonProperty("testing_organization")
    private String testingOrganization;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getTestingOrganization() {
        return testingOrganization;
    }

    public void setTestingOrganization(String testingOrganization) {
        this.testingOrganization = testingOrganization;
    }
}
