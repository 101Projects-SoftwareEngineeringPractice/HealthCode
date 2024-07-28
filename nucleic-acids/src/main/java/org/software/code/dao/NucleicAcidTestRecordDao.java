package org.software.code.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.Date;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "nucleic_acid_test", schema = "health_code_user")
public class NucleicAcidTestRecordDao {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "uid", nullable = false)
    private Long uid;

    @NotNull
    @Column(name = "tid", nullable = false)
    private Long tid;

    @NotNull
    @Column(name = "kind", nullable = false)
    private Integer kind;

    @NotNull
    @Column(name = "tubeid", nullable = false)
    private Long tubeid;

    @Size(max = 18)
    @NotNull
    @Column(name = "identity_card", nullable = false, length = 18)
    private String identity_card;

    @Size(max = 11)
    @NotNull
    @Column(name = "phone_number", nullable = false, length = 11)
    private String phone_number;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "district", nullable = false)
    private Integer district;

    @NotNull
    @Column(name = "street", nullable = false)
    private Integer street;

    @NotNull
    @Column(name = "community", nullable = false)
    private Long community;

    @Size(max = 255)
    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @Size(max = 255)
    @NotNull
    @Column(name = "test_address", nullable = false)
    private String test_address;

    @Column(name = "result")
    private Integer result;

    @Column(name = "testing_organization")
    private String testing_organization;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public Long getUid() {
        return uid;
    }

    public void setUid(@NotNull Long uid) {
        this.uid = uid;
    }

    @NotNull
    public Long getTid() {
        return tid;
    }

    public void setTid(@NotNull Long tid) {
        this.tid = tid;
    }

    @NotNull
    public Integer getKind() {
        return kind;
    }

    public void setKind(@NotNull Integer kind) {
        this.kind = kind;
    }

    @NotNull
    public Long getTubeid() {
        return tubeid;
    }

    public void setTubeid(@NotNull Long tubeid) {
        this.tubeid = tubeid;
    }

    @NotNull
    public @Size(max = 18) String getIdentity_card() {
        return identity_card;
    }

    public void setIdentity_card(@NotNull @Size(max = 18) String identity_card) {
        this.identity_card = identity_card;
    }

    @NotNull
    public @Size(max = 11) String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(@NotNull @Size(max = 11) String phone_number) {
        this.phone_number = phone_number;
    }

    @NotNull
    public @Size(max = 255) String getName() {
        return name;
    }

    public void setName(@NotNull @Size(max = 255) String name) {
        this.name = name;
    }

    @NotNull
    public Integer getDistrict() {
        return district;
    }

    public void setDistrict(@NotNull Integer district) {
        this.district = district;
    }

    @NotNull
    public Integer getStreet() {
        return street;
    }

    public void setStreet(@NotNull Integer street) {
        this.street = street;
    }

    @NotNull
    public Long getCommunity() {
        return community;
    }

    public void setCommunity(@NotNull Long community) {
        this.community = community;
    }

    @NotNull
    public @Size(max = 255) String getAddress() {
        return address;
    }

    public void setAddress(@NotNull @Size(max = 255) String address) {
        this.address = address;
    }

    @NotNull
    public @Size(max = 255) String getTest_address() {
        return test_address;
    }

    public void setTest_address(@NotNull @Size(max = 255) String test_address) {
        this.test_address = test_address;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getTesting_organization() {
        return testing_organization;
    }

    public void setTesting_organization(String testing_organization) {
        this.testing_organization = testing_organization;
    }

    public Boolean getRe_test() {
        return re_test;
    }

    public void setRe_test(Boolean re_test) {
        this.re_test = re_test;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    @Column(name = "re_test")
    private Boolean re_test;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_at")
    private Date updated_at;

    // Getters and Setters
}

