package org.software.code.model.input;

public class AddNucleicAcidTestRecordByTokenInput {
    private Long tid;
    private Long uid;
    private Integer kind;
    private Long tubeId;
    private String testAddress;

    public AddNucleicAcidTestRecordByTokenInput(Long tid, Long uid, Integer kind, Long tubeId, String testAddress) {
        this.tid = tid;
        this.uid = uid;
        this.kind = kind;
        this.tubeId = tubeId;
        this.testAddress = testAddress;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public Long getTubeId() {
        return tubeId;
    }

    public void setTubeId(Long tubeId) {
        this.tubeId = tubeId;
    }

    public String getTestAddress() {
        return testAddress;
    }

    public void setTestAddress(String testAddress) {
        this.testAddress = testAddress;
    }
}
