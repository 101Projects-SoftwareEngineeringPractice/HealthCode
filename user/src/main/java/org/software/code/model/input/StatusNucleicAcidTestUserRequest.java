package org.software.code.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

public class StatusNucleicAcidTestUserRequest {

    @NotNull(message = "tid不能为空")
    @JsonProperty("tid")
    private Long tid;

    @NotNull(message = "status不能为空")
    @JsonProperty("status")
    private Boolean status;

    
    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
