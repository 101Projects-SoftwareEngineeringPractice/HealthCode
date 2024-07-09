package org.software.code.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "place_mapping", schema = "health_code_user")
public class PlaceMappingDao {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "pid", nullable = false)
    private Long pid;

    @NotNull
    @Column(name = "uid", nullable = false)
    private Long uid;

    @Column(name = "time")
    private Date time;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull Long getPid() {
        return pid;
    }

    public void setPid(@NotNull Long pid) {
        this.pid = pid;
    }

    public @NotNull Long getUid() {
        return uid;
    }

    public void setUid(@NotNull Long uid) {
        this.uid = uid;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
