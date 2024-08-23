package org.software.code.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class AddNucleicAcidTestRecordRequest {
    @NotNull(message = "uid不能为空")
    @JsonProperty("uid")
    private Long uid;
    @NotNull(message = "tid不能为空")
    @JsonProperty("tid")
    private Long tid;
    @NotNull(message = "kind不能为空")
    @JsonProperty("kind")
    private Integer kind;
    @NotNull(message = "tubeid不能为空")
    @JsonProperty("tubeid")
    private Long tubeid;
    @NotNull(message = "identity_card不能为空")
    @JsonProperty("identity_card")
    private String identityCard;
    @NotNull(message = "phoneNumber不能为空")
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @NotNull(message = "name不能为空")
    @JsonProperty("name")
    private String name;
    @NotNull(message = "district不能为空")
    @JsonProperty("district")
    private Integer district;
    @NotNull(message = "street不能为空")
    @JsonProperty("street")
    private Integer street;
    @NotNull(message = "community不能为空")
    @JsonProperty("community")
    private Long community;
    @NotNull(message = "address不能为空")
    @JsonProperty("address")
    private String address;
    @NotNull(message = "test_address不能为空")
    @JsonProperty("test_address")
    private String testAddress;
    @NotNull(message = "testing_organization不能为空")
    @JsonProperty("testing_organization")
    private String testingOrganization;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
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

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDistrict() {
        return district;
    }

    public void setDistrict(Integer district) {
        this.district = district;
    }

    public Integer getStreet() {
        return street;
    }

    public void setStreet(Integer street) {
        this.street = street;
    }

    public Long getCommunity() {
        return community;
    }

    public void setCommunity(Long community) {
        this.community = community;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTestAddress() {
        return testAddress;
    }

    public void setTestAddress(String testAddress) {
        this.testAddress = testAddress;
    }

    public String getTestingOrganization() {
        return testingOrganization;
    }

    public void setTestingOrganization(String testingOrganization) {
        this.testingOrganization = testingOrganization;
    }
}
