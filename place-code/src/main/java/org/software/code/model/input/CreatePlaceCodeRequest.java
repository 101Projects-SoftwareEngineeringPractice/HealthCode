package org.software.code.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreatePlaceCodeRequest {

    @NotBlank(message = "identity_card不能为空")
    @JsonProperty("identity_card")
    private String identity_card;

    @NotBlank(message = "name不能为空")
    @JsonProperty("name")
    private String name;

    @NotNull(message = "district_id不能为空")
    @JsonProperty("district_id")
    private Integer district_id;

    @NotNull(message = "street_id不能为空")
    @JsonProperty("street_id")
    private Integer street_id;

    @NotNull(message = "community_id不能为空")
    @JsonProperty("community_id")
    private Long community_id;

    @NotBlank(message = "address不能为空")
    @JsonProperty("address")
    private String address;

    // Getters and Setters
    public String getIdentity_card() {
        return identity_card;
    }

    public void setIdentity_card(String identity_card) {
        this.identity_card = identity_card;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(Integer district_id) {
        this.district_id = district_id;
    }

    public Integer getStreet_id() {
        return street_id;
    }

    public void setStreet_id(Integer street_id) {
        this.street_id = street_id;
    }

    public Long getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(Long community_id) {
        this.community_id = community_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
