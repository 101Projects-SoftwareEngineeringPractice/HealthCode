package org.software.code.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserInfoRequest {

    @NotNull(message = "uid不能为空")
    @JsonProperty("uid")
    private Long uid;

    @NotBlank(message = "name不能为空")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "phone_number不能为空")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotBlank(message = "identity_card不能为空")
    @JsonProperty("identity_card")
    private String identityCard;

    @NotNull(message = "district不能为空")
    @JsonProperty("district")
    private Integer district;

    @NotNull(message = "street不能为空")
    @JsonProperty("street")
    private Integer street;

    @NotNull(message = "community不能为空")
    @JsonProperty("community")
    private Long community;

    @NotBlank(message = "address不能为空")
    @JsonProperty("address")
    private String address;

    public UserInfoRequest() {
    }

    public UserInfoRequest(Long uid, String name, String phoneNumber, String identityCard, Integer district, Integer street, Long community, String address) {
        this.uid = uid;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.identityCard = identityCard;
        this.district = district;
        this.street = street;
        this.community = community;
        this.address = address;
    }

    
    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
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
}
