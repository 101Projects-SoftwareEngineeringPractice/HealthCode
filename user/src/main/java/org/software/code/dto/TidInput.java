package org.software.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

public class TidInput {

    @NotNull(message = "tid不能为空")
    @JsonProperty("tid")
    private Long tid;

    // Getters and Setters
    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }
}
