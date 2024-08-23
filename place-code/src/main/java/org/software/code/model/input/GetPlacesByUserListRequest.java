package org.software.code.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class GetPlacesByUserListRequest {

    @NotNull(message = "uid_list不能为空")
    @Size(min = 1, message = "uid_list不能为空")
    @JsonProperty("uid_list")
    private List<Long> uidList;

    @NotNull(message = "开始时间不能为空")
    @JsonProperty("start_time")
    private String startTime;

    @NotNull(message = "结束时间不能为空")
    @JsonProperty("end_time")
    private String endTime;

    
    public List<Long> getUidList() {
        return uidList;
    }

    public void setUidList(List<Long> uidList) {
        this.uidList = uidList;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
