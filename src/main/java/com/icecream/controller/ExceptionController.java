package com.icecream.controller;

import com.icecream.model.ResultMap;
import org.apache.shiro.ShiroException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionController {

    private final ResultMap resultMap;

    @Autowired
    public ExceptionController (ResultMap resultMap) {
        this.resultMap = resultMap;
    }

    @ExceptionHandler(ShiroException.class)
    public ResultMap handle401() {
        return resultMap.fail().code(401).message("您没有访问权限");
    }

    public ResultMap globalException(HttpServletRequest request, Throwable ex) {
        return resultMap.fail()
                .code(getStatus(request).value())
                .message("访问出错，无法访问："  + ex.getMessage());
    }

    public HttpStatus getStatus(HttpServletRequest request) {
        Integer stautsCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (stautsCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(stautsCode);
    }
}
