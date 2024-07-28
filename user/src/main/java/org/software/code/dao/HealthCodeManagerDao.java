package org.software.code.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "health_code_manger", schema = "health_code_user")
public class HealthCodeManagerDao {
    @Id
    @Column(name = "mid", nullable = false)
    private Long mid;
    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;
    @Size(max = 18)
    @NotNull
    @Column(name = "identity_card", nullable = false)
    private String identity_card;
    @Size(max = 255)
    @NotNull
    @Column(name = "password_hash", nullable = false)
    private String password_hash;
    /**
     * false：不可登录，true：可以登录
     */
    @NotNull
    @Column(name = "status", nullable = false)
    private Boolean status;
    @Column(name = "created_at")
    private Date created_at;
    @Column(name = "updated_at")
    private Date updated_at;


    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentity_card() {
        return identity_card;
    }

    public void setIdentity_card(String identityCard) {
        this.identity_card = identityCard;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String passwordHash) {
        this.password_hash = passwordHash;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date createdAt) {
        this.created_at = createdAt;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updatedAt) {
        this.updated_at = updatedAt;
    }
}

