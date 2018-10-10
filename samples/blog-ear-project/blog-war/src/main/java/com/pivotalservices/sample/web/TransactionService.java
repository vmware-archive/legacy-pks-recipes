package com.pivotalservices.sample.web;

import com.pivotalservices.sample.batcher.SampleDataManager;
import com.pivotalservices.sample.dao.AuditEntryDAO;
import com.pivotalservices.sample.dao.PostDAO;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/transaction")
@Produces({"application/json"})
public class TransactionService {

    @EJB
    private AuditEntryDAO auditEntryDAO;

    @EJB
    private PostDAO postDAO;


    @Path("/Success")
    @GET
    public String performSuccessfulTransaction() {
        try {
            //Just using USERID of 1 for now.
            postDAO.create("Cross Database Transaction Post", "This post demonstrates the creation of a transaction that oppeartes across datasources", 1);
            auditEntryDAO.create("This is a succesfull audit entry save.");

        }catch (Exception ex){
            return "FAILURE";
        }


        return "SUCCESS";
    }

    @Path("/Failure")
    @GET
    public String performFailingTransaction() {
        try{
            //Just using USERID of 1 for now.
            postDAO.create("Cross Database Transaction Post", "This post demonstrates the creation of a transaction that oppeartes across datasources", 1);
            auditEntryDAO.createFailure("This is a succesfull audit entry save.");

        }catch (Exception ex){

            return "FAILURE";
        }

        return "SUCCESS";

    }



}
