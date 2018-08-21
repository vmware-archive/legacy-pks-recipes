package com.pivotalservices.sample.web;

import com.pivotalservices.sample.dao.UserDAO;
import com.pivotalservices.sample.model.User;

import javax.ejb.EJB;
import javax.ws.rs.*;
import java.util.List;

@Path("/api/user")
@Produces({"application/json"})
public class UserService {

    @EJB
    private UserDAO dao;

    @Path("/create")
    @PUT
    public User create(User user) {
        return dao.create(user.getFullname(), user.getPassword(), user.getEmail());
    }

    @Path("/list")
    @GET
    public List<User> list(@QueryParam("first") @DefaultValue("0") int first,
                           @QueryParam("max") @DefaultValue("20") int max) {
        return dao.list(first, max);
    }

    @Path("/show/{id}")
    @GET
    public User show(@PathParam("id") long id) {
        return dao.find(id);
    }

    @Path("/delete/{id}")
    @DELETE
    public void delete(@PathParam("id") long id) {
        dao.delete(id);
    }

    @Path("/update/{id}")
    @POST
    public User update(@PathParam("id") long id,
                       User user) {
        return dao.update(id, user.getFullname(), user.getPassword(), user.getEmail());
    }
}
