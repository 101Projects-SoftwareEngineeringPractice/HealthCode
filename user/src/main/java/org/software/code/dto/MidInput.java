package org.software.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

public class MidInput {

    @NotNull(message = "mid不能为空")
    @JsonProperty("mid")
    private Long mid;

    // Getter and Setter
    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }
}
