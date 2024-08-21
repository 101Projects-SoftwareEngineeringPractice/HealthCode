package org.software.code.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

public class MidRequest {

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
