package xyz.majexh.workflow.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class TokenException extends AuthenticationException {

    private HttpStatus status;

    public TokenException(String msg) {
        super(msg);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public TokenException(String msg, HttpStatus status) {
        this(msg);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
