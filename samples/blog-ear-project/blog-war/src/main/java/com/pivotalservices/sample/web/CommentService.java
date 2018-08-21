package com.pivotalservices.sample.web;

import com.pivotalservices.sample.dao.CommentDAO;
import com.pivotalservices.sample.model.Comment;

import javax.ejb.EJB;
import javax.ws.rs.*;
import java.util.List;

@Path("/api/comment")
@Produces({"application/json"})
public class CommentService {

    @EJB
    private CommentDAO commentDao;

    @Path("/create")
    @PUT
    public Comment create(@QueryParam("author") String author,
                          @QueryParam("content") String content,
                          @QueryParam("postId") long postId) {
        return commentDao.create(author, content, postId);
    }

    @Path("/list/{postId}")
    @GET
    public List<Comment> list(@PathParam("postId") long postId) {
        return commentDao.list(postId);
    }

    @Path("/delete/{id}")
    @DELETE
    public void delete(@PathParam("id") long id) {
        commentDao.delete(id);
    }

    @Path("/update/{id}")
    @POST
    public Comment update(@PathParam("id") long id,
                          @QueryParam("author") String author,
                          @QueryParam("content") String content) {
        return commentDao.update(id, author, content);
    }
}
