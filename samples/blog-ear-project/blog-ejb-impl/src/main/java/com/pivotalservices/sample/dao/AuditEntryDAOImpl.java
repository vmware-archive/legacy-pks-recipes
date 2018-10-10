package com.pivotalservices.sample.dao;

import com.pivotalservices.sample.model.AuditEntry;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton(name = "AuditEntryDAO")
@Lock(LockType.READ)
public class AuditEntryDAOImpl implements  AuditEntryDAO{

    @PersistenceContext(unitName = "audit")
    protected EntityManager em;


    @Override
    public AuditEntry create(String message) {
        AuditEntry auditEntry= new AuditEntry(message);
        em.persist(auditEntry);
        return auditEntry;
    }

    @Override
    public AuditEntry createFailure(String message) {
        throw new RuntimeException("Rollback transaction!");
        AuditEntry auditEntry = new AuditEntry("We should never have this transaction persisted.");
        em.persist(auditEntry);
        return auditEntry;
    }
}
