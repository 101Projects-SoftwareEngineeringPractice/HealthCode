package org.software.code.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Entity
@Table(name = "uid_maping", schema = "health_code_user")
public class UidMappingDao {
    @Id
    @Column(name = "uid", nullable = false)
    private Long uid;
    @Size(max = 255)
    @NotNull
    @Column(name = "openid", nullable = false, length = 18)
    private String openid;
    @Column(name = "created_at")
    private Instant created_at;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Instant getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Instant created_at) {
        this.created_at = created_at;
    }
}
