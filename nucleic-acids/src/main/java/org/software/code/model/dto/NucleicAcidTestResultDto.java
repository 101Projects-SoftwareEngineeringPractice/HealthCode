package org.software.code.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class NucleicAcidTestResultDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("created_at")
    private Date createdAt;
    private Integer result;
    @JsonProperty("testing_organization")
    private String testingOrganization;


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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