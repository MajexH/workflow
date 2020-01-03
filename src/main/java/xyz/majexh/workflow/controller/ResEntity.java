package xyz.majexh.workflow.controller;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResEntity<T> {

    private int status;
    private String message;
    private T data;

    public ResEntity<T> ok(String message) {
        this.status = HttpStatus.OK.value();
        this.message = message;
        return this;
    }

    public ResEntity<T> okDefault(T data) {
        this.ok("success");
        this.data = data;
        return this;
    }

    public ResEntity<T> error(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
        return this;
    }

}
