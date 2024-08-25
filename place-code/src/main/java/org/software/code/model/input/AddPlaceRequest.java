package org.software.code.model.input;

import javax.validation.constraints.NotNull;

public class AddPlaceRequest {
    @NotNull(message = "uid不能为空")
    private Long uid;
    @NotNull(message = "name不能为空")
    private String name;
    @NotNull(message = "district不能为空")
    private Integer district;
    @NotNull(message = "street不能为空")
    private Integer street;
    @NotNull(message = "community不能为空")
    private Long community;
    @NotNull(message = "address不能为空")
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

