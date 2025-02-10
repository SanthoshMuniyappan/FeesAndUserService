package com.schoolworld.user.exception;

import com.schoolworld.user.dto.ResponseDTO;
import com.schoolworld.user.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(BadRequestServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO handleBadRequestAlertException(BadRequestServiceException exception) {
        ResponseDTO responseDTO = new ResponseDTO();
        exception.printStackTrace();
        responseDTO.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
        responseDTO.setMessage(Constants.NOT_FOUND);
        responseDTO.setData(exception.getMessage());
        return responseDTO;
    }
}
