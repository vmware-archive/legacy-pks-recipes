package com.pivotalservices.sample.web.flow;

import com.pivotalservices.sample.dao.UserDAO;
import com.pivotalservices.sample.model.User;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/web/open/changeActiveUser")
public class ChangeActiveUserServlet extends HttpServlet {

    @EJB
    private UserDAO userDAO;


    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {

        List<User> users = this.userDAO.list(0, 20);
        request.setAttribute("users", users);
        request.getRequestDispatcher("/WEB-INF/jsp/changeActiveUser.jsp")
                .forward(request, response);

    }
    
}
