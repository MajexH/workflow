package xyz.majexh.workflow.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.majexh.workflow.exceptions.BaseException;

@ControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public ResEntity baseExceptionHandler(BaseException baseException) {
        log.error(baseException.getMessage());
        return new ResEntity().error(baseException.getStatus(), baseException.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResEntity exceptionHandler(Exception exception) {
        log.error(exception.getMessage());
        return new ResEntity().error(500, exception.getMessage());
    }
}
