package org.software.code.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreatePlaceCodeRequest {

    @NotBlank(message = "identity_card不能为空")
    @JsonProperty("identity_card")
    private String identityCard;

    @NotBlank(message = "name不能为空")
    @JsonProperty("name")
    private String name;

    @NotNull(message = "district_id不能为空")
    @JsonProperty("district_id")
    private Integer districtId;

    @NotNull(message = "street_id不能为空")
    @JsonProperty("street_id")
    private Integer streetId;

    @NotNull(message = "community_id不能为空")
    @JsonProperty("community_id")
    private Long communityId;

    @NotBlank(message = "address不能为空")
    @JsonProperty("address")
    private String address;

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public Integer getStreetId() {
        return streetId;
    }

    public void setStreetId(Integer streetId) {
        this.streetId = streetId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
