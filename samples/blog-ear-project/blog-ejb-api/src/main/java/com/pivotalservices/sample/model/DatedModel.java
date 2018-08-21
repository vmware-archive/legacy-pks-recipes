package com.pivotalservices.sample.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.util.Date;

@MappedSuperclass
public abstract class DatedModel extends Model {

    private Date created;

    @PrePersist
    public void create() {
        created = new Date();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
