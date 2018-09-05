package com.pivotalservices.sample.web.flow;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/web/open/errorMessage")
public class ErrorMessageServlet extends HttpServlet {
    
    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {

        String errorMessage = request.getParameter("message");
        request.setAttribute("message", errorMessage);
        
        request.getRequestDispatcher("/WEB-INF/jsp/errorMessage.jsp")
                .forward(request, response);

    }
    
}
