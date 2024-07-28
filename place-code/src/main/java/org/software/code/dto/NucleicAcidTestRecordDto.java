package org.software.code.dto;


public class NucleicAcidTestRecordDto {
    private String created_at; //timestamp
    private int result;
    private String testing_organization;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getTesting_organization() {
        return testing_organization;
    }

    public void setTesting_organization(String testing_organization) {
        this.testing_organization = testing_organization;
    }
}
