package org.software.code.model.input;

public class AddNucleicAcidTestRecordByIDInput {
    private Long tid;
    private String identityCard;
    private Integer kind;
    private Long tubeId;
    private String testAddress;

    public AddNucleicAcidTestRecordByIDInput(Long tid, String identityCard, Integer kind, Long tubeId, String testAddress) {
        this.tid = tid;
        this.identityCard = identityCard;
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

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
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
