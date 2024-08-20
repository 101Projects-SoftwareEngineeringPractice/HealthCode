package org.software.code.model.input;

import javax.validation.constraints.NotNull;

public class AddPlaceInput {
    @NotNull
    private Long uid;
    @NotNull
    private String name;
    @NotNull
    private Integer district;
    @NotNull
    private Integer street;
    @NotNull
    private Long community;
    @NotNull
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

