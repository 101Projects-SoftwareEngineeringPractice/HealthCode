package org.software.code.dto;


public class PositiveInfo {
    private Long uid;
    private Integer kind;
    private Long tid;
    private Long tubeid;
    private String name;
    private String phone_number;
    private String identity_card;
    private Integer district_id;
    private Integer street_id;
    private  Integer community_id;
    private String address;
    private String test_address;
    private String created_at; //timestamp
    private String updated_at; //timestamp
    private Boolean check;
    private Boolean result;
    private String testing_organization;

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

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getTubeid() {
        return tubeid;
    }

    public void setTubeid(Long tubeid) {
        this.tubeid = tubeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getIdentity_card() {
        return identity_card;
    }

    public void setIdentity_card(String identity_card) {
        this.identity_card = identity_card;
    }

    public Integer getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(Integer district_id) {
        this.district_id = district_id;
    }

    public Integer getStreet_id() {
        return street_id;
    }

    public void setStreet_id(Integer street_id) {
        this.street_id = street_id;
    }

    public Integer getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(Integer community_id) {
        this.community_id = community_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTest_address() {
        return test_address;
    }

    public void setTest_address(String test_address) {
        this.test_address = test_address;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getTesting_organization() {
        return testing_organization;
    }

    public void setTesting_organization(String testing_organization) {
        this.testing_organization = testing_organization;
    }
}
