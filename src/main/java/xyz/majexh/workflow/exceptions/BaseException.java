package xyz.majexh.workflow.exceptions;

public class BaseException extends RuntimeException {

    private int status;

    public BaseException(int status, String message) {
        super(message);
        this.status = status;
    }

    public BaseException(ExceptionEnum exceptionEnum) {
        this(exceptionEnum.getStatus(), exceptionEnum.getMessage());
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
