package com.icecream.controller;

import com.icecream.model.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.AccountException;

@RestControllerAdvice
public class ExceptionController {

    private final ResultMap resultMap;

    @Autowired
    public ExceptionController (ResultMap resultMap) {
        this.resultMap = resultMap;
    }

    @ExceptionHandler(AccountException.class)
    public ResultMap handleShrioException(Exception e) {
        return resultMap.fail().message(e.getMessage());
    }
}
