package com.pivotalservices.sample.web;

import com.pivotalservices.sample.dao.PostDAO;
import com.pivotalservices.sample.model.Post;

import javax.ejb.EJB;
import javax.ws.rs.*;
import java.util.List;

@Path("/post")
@Produces({"application/json"})
public class PostService {

    @EJB
    private PostDAO dao;

    @Path("/create")
    @PUT
    public Post create(Post post,
                       @QueryParam("userId") long userId) {
        return dao.create(post.getTitle(), post.getContent(), userId);
    }

    @Path("/list")
    @GET
    public List<Post> list(@QueryParam("first") @DefaultValue("0") int first,
                           @QueryParam("max") @DefaultValue("20") int max) {
        return dao.list(first, max);
    }

    @Path("/show/{id}")
    @GET
    public Post show(@PathParam("id") long id) {
        return dao.find(id);
    }

    @Path("/delete/{id}")
    @DELETE
    public void delete(@PathParam("id") long id) {
        dao.delete(id);
    }

    @Path("/update/{id}")
    @POST
    public Post update(@PathParam("id") long id,
                       @QueryParam("userId") long userId,
                       Post post) {
        return dao.update(id, userId, post.getTitle(), post.getContent());
    }
}
