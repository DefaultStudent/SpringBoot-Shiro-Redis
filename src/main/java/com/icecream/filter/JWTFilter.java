package com.icecream.filter;

import com.icecream.shiro.JWTToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

public class JWTFilter extends BasicHttpAuthenticationFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 如果带有 token，则对 token 进行检查，否则直接通过
     *
     * @param request ServletRequest
     * @param response ServletResponse
     * @param mappedValue Object
     * @return true
     * @throws UnauthorizedException 未认证
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws UnauthorizedException {

        // 判断请求的请求头上是否带有 "Token"
        if (isLoginAttempt(request, response)) {

            // 如果存在，则进入 executeLogin 方法进行登入， 检查 token 是否正确
            try {
                executeLogin(request, response);
            } catch (Exception ex) {
                // token 错误
                responseError(response, ex.getMessage());
            }
        }
        return true;
    }

    /**
     * 判断用户是否想要登录
     * 检测 Header 里面是否包含 token 字段
     *
     * @param request ServletRequest
     * @param response ServletResponse
     * @return Boolean
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String token = httpServletResponse.getHeader("Token");
        return token != null;
    }

    /**
     * 执行登录操作
     *
     * @param request ServletRequest
     * @param response ServletResponse
     * @return boolean
     * @throws Exception 登录异常
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String token = httpServletResponse.getHeader("Token");
        JWTToken jwtToken = new JWTToken(token);
        // 交给 Realm 进行登入，如果出错他会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回 true
        return true;
    }

    /**
     * 对跨域提供支持
     *
     * @param request ServletRequest
     * @param response ServletResponse
     * @return boolean
     * @throws Exception Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));

        // 跨域时会首先发送一个 option 请求，这里我们给 option 请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 将非法请求跳转至 /unauthorized/**
     *
     * @param response ServletResponse
     * @param message 错误信息
     */
    private void responseError(ServletResponse response, String message) {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            // 设置编码， 否则中文字符在重定向时会变为空字符串
            message = URLEncoder.encode(message, "UTF-8");
            httpServletResponse.sendRedirect("/unauthorized/" + message);
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }
}
