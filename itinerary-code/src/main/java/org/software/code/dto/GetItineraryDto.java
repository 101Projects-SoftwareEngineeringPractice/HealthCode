package org.software.code.dto;

import java.util.Date;
import java.util.List;

public class GetItineraryDto {
    private List<PlaceStarDto> places;
    private String identity_card;

    private Date created_at;

    public List<PlaceStarDto> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceStarDto> places) {
        this.places = places;
    }

    public String getIdentity_card() {
        return identity_card;
    }

    public void setIdentity_card(String identity_card) {
        this.identity_card = identity_card;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
