package ru.itis.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        Boolean isAuthenticated = false;
        Boolean sessionExist = session != null;
        Boolean isLoginPage = request.getRequestURI().equals("/signIn");
        Boolean isSignUpPage = request.getRequestURI().equals("/signUp");
        Boolean isEmptyPath = request.getRequestURI().equals("");
        Boolean isStaticResource = request.getRequestURI().startsWith("/styles/");

        if (sessionExist) {
            isAuthenticated = (Boolean) session.getAttribute("authenticated");
            if (isAuthenticated == null) {
                isAuthenticated = false;
            }
        }

        if (isStaticResource) {
            filterChain.doFilter(request, response);
            return;
        }

        if (isEmptyPath) {
            response.sendRedirect("/feed");
            return;
        }

        if ((isLoginPage || isSignUpPage) && !isAuthenticated) {
            filterChain.doFilter(request, response);
            return;
        }

        if(isSignUpPage && isAuthenticated) {
            response.sendRedirect("/feed");
            return;
        }

        if (isAuthenticated && !isLoginPage || !isAuthenticated && isLoginPage) {
            filterChain.doFilter(request,  response);
        } else if (isAuthenticated && isLoginPage){
            response.sendRedirect("/feed");
        } else {
            response.sendRedirect("/signIn");
        }
    }


    @Override
    public void destroy() {

    }
}
