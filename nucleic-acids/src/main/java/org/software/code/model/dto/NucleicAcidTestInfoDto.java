package org.software.code.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NucleicAcidTestInfoDto {
    private long record;
    private long uncheck;
    @JsonProperty("one_positive")
    private long onePositive;
    private long positive;
    public long getRecord() {
        return record;
    }

    public void setRecord(long record) {
        this.record = record;
    }

    public long getUncheck() {
        return uncheck;
    }

    public void setUncheck(long uncheck) {
        this.uncheck = uncheck;
    }

    public long getOnePositive() {
        return onePositive;
    }

    public void setOnePositive(long onePositive) {
        this.onePositive = onePositive;
    }

    public long getPositive() {
        return positive;
    }

    public void setPositive(long positive) {
        this.positive = positive;
    }
}
