package com.pivotalservices.sample.web.flow;

import com.pivotalservices.sample.dao.PostDAO;
import com.pivotalservices.sample.dao.UserDAO;
import com.pivotalservices.sample.model.Post;
import com.pivotalservices.sample.model.User;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/web/closed/showPosts")
public class ShowPostsOfActiveUser extends HttpServlet {

    @EJB
    private UserDAO userDAO;

    @EJB
    private PostDAO postDAO;
    
    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {


        User activeUser = (User)request.getSession().getAttribute("activeUser");

        List<Post> posts = postDAO.findPostsByUser(activeUser);
        request.setAttribute("posts", posts);

        request.getRequestDispatcher("/WEB-INF/jsp/posts.jsp")
                .forward(request, response);

    }
    
}
