package com.pivotalservices.sample.web;

import com.pivotalservices.sample.dao.AuditEntryDAO;
import com.pivotalservices.sample.dao.PostDAO;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class TransactionServiceEJBImpl implements TransactionServiceEJB {

    @EJB
    private AuditEntryDAO auditEntryDAO;

    @EJB
    private PostDAO postDAO;

    public void performSucessfulTransaction(){
        //Just using USERID of 1 for now.
        postDAO.create("Cross Database Transaction Post", "This post demonstrates the creation of a transaction that oppeartes across datasources", 1);
        auditEntryDAO.create("This is a succesfull audit entry save.");
    }

    public void performUnSucessfulTransaction(){
        postDAO.create("Failure cross database post", "You should never see this post, because it indicates that the transaction wasn't rolled back.", 1);
        auditEntryDAO.createFailure("This is a succesfull audit entry save.");

    }

}
