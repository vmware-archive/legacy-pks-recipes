package com.pivotalservices.sample.web;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/transaction")
@Produces({"application/json"})
public class TransactionService {


    @Inject
    TransactionServiceEJB transactionServiceEJB;

    @Path("/success")
    @GET
    public String performSuccessfulTransaction() {
        try {
            transactionServiceEJB.performSucessfulTransaction();
        } catch (Exception ex) {
            return "FAILURE";
        }
        return "SUCCESS";
    }

    @Path("/failure")
    @GET
    public String performFailingTransaction() {
        try {
            //Just using USERID of 1 for now.
            transactionServiceEJB.performUnSucessfulTransaction();

        } catch (Exception ex) {

            return "FAILURE";
        }

        return "SUCCESS";

    }


}
