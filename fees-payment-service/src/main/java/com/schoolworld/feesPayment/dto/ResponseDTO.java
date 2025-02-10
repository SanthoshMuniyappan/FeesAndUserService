package com.schoolworld.feesPayment.dto;


public class ResponseDTO {
    private String message;
    private Object data;
    private String statusCode;

    public ResponseDTO() {

    }

    public ResponseDTO(String message, Object data, String statusCode) {
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public String getStatusCode() {
        return statusCode;
    }
}
