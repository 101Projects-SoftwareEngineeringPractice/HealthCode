package org.software.code.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Table(name = "health_code", schema = "health_code_health_code")
public class HealthCodeDao {
    @Id
    @Column(name = "uid", nullable = false)
    private Long uid;
    /**
     * 健康码颜色，0：红色，1：黄色，2：绿色
     */
    @NotNull
    @Column(name = "color", nullable = false)
    private Integer color;
    @Column(name = "created_at")
    private Instant created_at;
    @Column(name = "updated_at")
    private Instant updated_at;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public Instant getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Instant created_at) {
        this.created_at = created_at;
    }

    public Instant getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Instant updated_at) {
        this.updated_at = updated_at;
    }
}