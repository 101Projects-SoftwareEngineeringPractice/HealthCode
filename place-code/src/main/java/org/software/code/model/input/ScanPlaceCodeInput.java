package org.software.code.model.input;

public class ScanPlaceCodeInput {
    private Long uid;
    private Long pid;

    public ScanPlaceCodeInput(Long uid, Long pid) {
        this.uid = uid;
        this.pid = pid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }
}
