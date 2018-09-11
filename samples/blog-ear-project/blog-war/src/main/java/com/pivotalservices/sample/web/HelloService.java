package com.pivotalservices.sample.web;

import com.pivotalservices.sample.api.Hello;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/hello")
@Produces({"application/json"})
public class HelloService {

    @EJB
    private Hello helloBean;


    @Path("/")
    @GET
    public String sayHello() {
        return helloBean.hello();
    }
    
}
