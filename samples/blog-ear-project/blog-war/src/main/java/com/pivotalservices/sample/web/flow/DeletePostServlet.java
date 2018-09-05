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
import java.net.URLEncoder;

@WebServlet("/web/closed/deletePost")
public class DeletePostServlet extends HttpServlet {

    @EJB
    private UserDAO userDAO;

    @EJB
    private PostDAO postDAO;

    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {

        User activeUser = (User) request.getSession().getAttribute("activeUser");

        String id = request.getParameter("id");
        Post post = postDAO.find(Long.valueOf(id));

        if (post.getUser().getId() != activeUser.getId()) {
            response.sendRedirect(request.getContextPath() + "/web/open/errorMessage?message=" + URLEncoder.encode("Post does not belong to user", "UTF-8"));
            return;
        }

        postDAO.delete(Long.valueOf(id));
        response.sendRedirect(request.getContextPath() + "/web/closed/showPosts");
    }

}
