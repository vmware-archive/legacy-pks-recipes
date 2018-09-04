package com.pivotalservices.sample.web.flow;

import com.pivotalservices.sample.model.User;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/web/closed/*")
public class ActiveUserFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        User activeUser = (User)request.getSession().getAttribute("activeUser");
        
        if (activeUser == null) {
            response.sendRedirect(request.getContextPath() + "/web/open/changeActiveUser");
            return;
        }
        
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
