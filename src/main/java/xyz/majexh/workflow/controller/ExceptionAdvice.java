package xyz.majexh.workflow.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.majexh.workflow.exceptions.BaseException;
import xyz.majexh.workflow.exceptions.ExceptionEnum;

import java.util.HashMap;

@ControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> baseExceptionHandler(BaseException baseException) {
        log.error(baseException.getMessage());
        return ResEntity.error(baseException.getStatus(), baseException.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> exceptionHandler(Exception exception) {
        log.error(exception.getMessage());
        return ResEntity.error(ExceptionEnum.INTERNAL_ERROR.getStatus(), exception.getMessage(), null);
    }
}
