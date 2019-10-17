/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filter;

import Domain.Distributor;
import Domain.User;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Jado
 */
public class loginfilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest rq = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        User session = (User) rq.getSession().getAttribute("session");
        String url = rq.getRequestURI();
        if (session == null && !url.contains("index.xhtml")) {

            resp.sendRedirect(rq.getServletContext().getContextPath() + "/pages/index.xhtml");
  
         }
         else{
             chain.doFilter(request, response);
        
         }
    }

    @Override
    public void destroy() {
        
    }
    
}
