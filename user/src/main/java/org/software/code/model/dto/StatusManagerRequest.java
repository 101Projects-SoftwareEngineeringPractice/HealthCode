package org.software.code.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

public class StatusManagerRequest {

    @NotNull(message = "mid不能为空")
    @JsonProperty("mid")
    private Long mid;

    @NotNull(message = "status不能为空")
    @JsonProperty("status")
    private Boolean status;

    // Getters and Setters
    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
