package org.software.code.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class GetItineraryDto {
    private List<PlaceStarDto> places;
    @JsonProperty("identity_card")
    private String identityCard;
    @JsonProperty("created_at")
    private Date createdAt;

    public List<PlaceStarDto> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceStarDto> places) {
        this.places = places;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
