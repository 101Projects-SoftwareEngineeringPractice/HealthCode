package org.software.code.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class OppositePlaceCodeRequest {

    @NotNull(message = "pid不能为空")
    @JsonProperty("pid")
    private Long pid;

    @NotNull(message = "status不能为空")
    @JsonProperty("status")
    private Boolean status;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
