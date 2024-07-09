package org.software.code.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "itinerary_code", schema = "health_code_itinerary_code")
public class ItineraryCodeDao {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @NotNull
    @Column(name = "uid")
    private Long uid;
    @NotNull
    @Column(name = "place")
    private Integer place;
    @NotNull
    @Column(name = "time")
    private Date time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}