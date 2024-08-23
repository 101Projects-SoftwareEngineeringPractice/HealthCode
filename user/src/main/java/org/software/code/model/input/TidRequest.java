package org.software.code.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

public class TidRequest {

    @NotNull(message = "tid不能为空")
    @JsonProperty("tid")
    private Long tid;

    
    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }
}
