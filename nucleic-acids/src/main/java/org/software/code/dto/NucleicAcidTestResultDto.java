package org.software.code.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class NucleicAcidTestResultDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created_at;
    private Integer result;
    private String testing_organization;


    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getTesting_organization() {
        return testing_organization;
    }

    public void setTesting_organization(String testing_organization) {
        this.testing_organization = testing_organization;
    }


}