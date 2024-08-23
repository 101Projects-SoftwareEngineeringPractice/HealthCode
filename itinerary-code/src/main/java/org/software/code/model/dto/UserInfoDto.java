package org.software.code.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfoDto {
    private Long uid;
    private String name;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("identity_card")
    private String identityCard;

    private int district;

    private int street;

    private long community;

    private String address;


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

    public int getDistrict() {
        return district;
    }

    public void setDistrict(int district) {
        this.district = district;
    }

    public int getStreet() {
        return street;
    }

    public void setStreet(int street) {
        this.street = street;
    }

    public long getCommunity() {
        return community;
    }

    public void setCommunity(long community) {
        this.community = community;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
