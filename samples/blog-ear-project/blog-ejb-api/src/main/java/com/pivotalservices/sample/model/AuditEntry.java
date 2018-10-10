package com.pivotalservices.sample.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class AuditEntry extends DatedModel {

    @NotNull
    @Size(min = 1)
    private String message;

    public AuditEntry() {
    }

    public void setMessage(final String message){ this.message = message;}

    public String getMessage() {return message;}

    public AuditEntry(String _message){
        this.message = _message;
    }


}
