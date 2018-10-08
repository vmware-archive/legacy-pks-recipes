package com.pivotalservices.sample.web;

import com.pivotalservices.sample.batcher.SampleDataManager;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/config")
@Produces({"application/json"})
public class ConfigService {

    @EJB
    private SampleDataManager dataManager;

    @Path("/create")
    @GET
    public String create() {
        dataManager.createSomeData();
        return "DONE";
        
    }
}
