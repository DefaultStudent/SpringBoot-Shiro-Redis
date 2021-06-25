package com.icecream.filter;

import com.icecream.utils.Constant;
import com.icecream.utils.JWTUtil;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "LoginFilter", urlPatterns = {"/admin/*", "/user/*"})
@Order(1)
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String username = (String) request.getSession().getAttribute(Constant.USER_NAME);

        if (username != null) {
            if(response.getHeader(Constant.TOKEN) == null || "".equals(response.getHeader(Constant.TOKEN))) {
                response.setHeader(Constant.TOKEN, JWTUtil.createToken(username));
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/start");
        }
    }

    @Override
    public void destroy() {

    }
}
