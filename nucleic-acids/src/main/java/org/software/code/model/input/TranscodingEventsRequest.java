package org.software.code.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class TranscodingEventsRequest {

    @NotNull
    @JsonProperty("uid")
    private Long uid;

    @NotNull
    @JsonProperty("event")
    private Integer event;

    public TranscodingEventsRequest(Long uid, Integer event) {
        this.uid = uid;
        this.event = event;
    }

    
    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getEvent() {
        return event;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }
}
