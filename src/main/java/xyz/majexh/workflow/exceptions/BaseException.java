package xyz.majexh.workflow.exceptions;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {

    private HttpStatus status;

    public BaseException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public BaseException(ExceptionEnum exceptionEnum) {
        this(exceptionEnum.getStatus(), exceptionEnum.getMessage());
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
