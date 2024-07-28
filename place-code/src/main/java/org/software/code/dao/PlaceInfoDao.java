package org.software.code.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "place_info", schema = "health_code_user")
public class PlaceInfoDao {

    @Id
    @Column(name = "pid", nullable = false)
    private Long pid;

    @NotNull
    @Column(name = "uid", nullable = false)
    private Long uid;

    @Size(max = 18)
    @NotNull
    @Column(name = "identity_card", nullable = false, length = 18)
    private String identity_card;

    @Size(max = 64)
    @NotNull
    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @NotNull
    @Column(name = "district", nullable = false)
    private Integer district;

    @NotNull
    @Column(name = "street", nullable = false)
    private Integer street;

    @NotNull
    @Column(name = "community", nullable = false)
    private Integer community;

    @Size(max = 255)
    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_at")
    private Date updated_at;

    // Getters and Setters

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public @NotNull Long getUid() {
        return uid;
    }

    public void setUid(@NotNull Long uid) {
        this.uid = uid;
    }

    public @Size(max = 18) @NotNull String getIdentity_card() {
        return identity_card;
    }

    public void setIdentity_card(@Size(max = 18) @NotNull String identity_card) {
        this.identity_card = identity_card;
    }

    public @Size(max = 64) @NotNull String getName() {
        return name;
    }

    public void setName(@Size(max = 64) @NotNull String name) {
        this.name = name;
    }

    public @NotNull Integer getDistrict() {
        return district;
    }

    public void setDistrict(@NotNull Integer district) {
        this.district = district;
    }

    public @NotNull Integer getStreet() {
        return street;
    }

    public void setStreet(@NotNull Integer street) {
        this.street = street;
    }

    public @NotNull Integer getCommunity() {
        return community;
    }

    public void setCommunity(@NotNull Integer community) {
        this.community = community;
    }

    public @Size(max = 255) @NotNull String getAddress() {
        return address;
    }

    public void setAddress(@Size(max = 255) @NotNull String address) {
        this.address = address;
    }

    public @NotNull Boolean getStatus() {
        return status;
    }

    public void setStatus(@NotNull Boolean status) {
        this.status = status;
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
}

