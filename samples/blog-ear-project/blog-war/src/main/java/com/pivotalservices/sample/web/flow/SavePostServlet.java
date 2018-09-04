package com.pivotalservices.sample.web.flow;

import com.pivotalservices.sample.dao.PostDAO;
import com.pivotalservices.sample.dao.UserDAO;
import com.pivotalservices.sample.model.User;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/web/closed/savePost")
public class SavePostServlet extends HttpServlet {

    @EJB
    private UserDAO userDAO;

    @EJB
    private PostDAO postDAO;

    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {

        User activeUser = (User) request.getSession().getAttribute("activeUser");

        String title = request.getParameter("title");
        String content = request.getParameter("content");

        postDAO.create(title, content, activeUser.getId());

        response.sendRedirect(request.getContextPath() + "/web/closed/showPosts");

    }

}
