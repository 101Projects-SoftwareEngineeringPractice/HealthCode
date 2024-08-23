package org.software.code.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class PidRequest {

    @NotNull(message = "pid不能为空")
    @JsonProperty("pid")
    private Long pid;

    
    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }
}
