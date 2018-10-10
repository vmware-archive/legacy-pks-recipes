package com.pivotalservices.sample.dao;


import com.pivotalservices.sample.model.AuditEntry;

public interface AuditEntryDAO {

    AuditEntry create(String message);

    AuditEntry createFailure(String message);

}
