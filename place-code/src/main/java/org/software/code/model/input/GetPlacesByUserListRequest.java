package org.software.code.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class GetPlacesByUserListRequest {

    @NotNull(message = "uidList不能为空")
    @Size(min = 1, message = "uidList不能为空")
    @JsonProperty("uidList")
    private List<Long> uidList;

    @NotNull(message = "开始时间不能为空")
    @JsonProperty("start_time")
    private String start_time;

    @NotNull(message = "结束时间不能为空")
    @JsonProperty("end_time")
    private String end_time;

    // Getters and Setters
    public List<Long> getUidList() {
        return uidList;
    }

    public void setUidList(List<Long> uidList) {
        this.uidList = uidList;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
