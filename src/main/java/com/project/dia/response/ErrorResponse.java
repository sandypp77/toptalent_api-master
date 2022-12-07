package com.project.dia.response;

public class ErrorResponse<T> {
    private int code;
    private String status;
    private T errorCode;
    private T message;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(T errorCode) {
        this.errorCode = errorCode;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }
}
