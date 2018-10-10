package com.pivotalservices.sample.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AuditEntry extends DatedModel {

    @NotNull
    @Size(min = 1)
    private String message;

    public void setMessage(final String message){ this.message = message;}

    public String getMessage() {return message;}

    public AuditEntry(String _message){
        this.message = _message;
    }


}
