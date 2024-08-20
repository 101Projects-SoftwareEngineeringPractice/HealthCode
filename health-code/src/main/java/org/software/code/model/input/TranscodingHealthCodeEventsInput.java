package org.software.code.model.input;

import org.software.code.common.consts.FSMConst;

public class TranscodingHealthCodeEventsInput {
    private Long uid;
    public FSMConst.HealthCodeEvent healthCodeEvent;

    public TranscodingHealthCodeEventsInput(Long uid, FSMConst.HealthCodeEvent healthCodeEvent) {
        this.uid = uid;
        this.healthCodeEvent = healthCodeEvent;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public FSMConst.HealthCodeEvent getHealthCodeEvent() {
        return healthCodeEvent;
    }

    public void setHealthCodeEvent(FSMConst.HealthCodeEvent healthCodeEvent) {
        this.healthCodeEvent = healthCodeEvent;
    }
}
