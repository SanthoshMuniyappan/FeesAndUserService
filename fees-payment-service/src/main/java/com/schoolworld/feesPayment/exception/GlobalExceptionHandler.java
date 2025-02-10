package com.schoolworld.feesPayment.exception;

import com.schoolworld.feesPayment.dto.ResponseDTO;
import com.schoolworld.feesPayment.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadServiceAlertException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO handleBadRequestServiceAlertException(BadServiceAlertException exception) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constants.NOT_FOUND);
        responseDTO.setData(exception.getMessage());
        responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.getReasonPhrase());
        return responseDTO;
    }

    @ExceptionHandler(StudentNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO handleStudentNotFoundException(StudentNotFoundException exception) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constants.NOT_FOUND);
        responseDTO.setData(exception.getMessage());
        responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.getReasonPhrase());
        return responseDTO;
    }

    @ExceptionHandler(FeesNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO handleFeesNotFoundException(FeesNotFoundException exception) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constants.NOT_FOUND);
        responseDTO.setData(exception.getMessage());
        responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.getReasonPhrase());
        return responseDTO;
    }

    @ExceptionHandler(StandardNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO handleStandardNotFoundException(StandardNotFoundException exception) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constants.NOT_FOUND);
        responseDTO.setData(exception.getMessage());
        responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.getReasonPhrase());
        return responseDTO;
    }

    @ExceptionHandler(Exception.class)
    public ResponseDTO handleException(Exception e) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constants.NOT_FOUND);
        responseDTO.setData(e.getMessage());
        responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.getReasonPhrase());
        return responseDTO;
    }
}
