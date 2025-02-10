package com.schoolworld.feesPayment.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolworld.feesPayment.dto.ResponseDTO;
import com.schoolworld.feesPayment.util.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationEntryPointImplement implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setStatusCode(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        responseDTO.setMessage(Constants.CREDENTIALS_MISMATCH);

        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("json");
        mapper.writeValue(response.getOutputStream(), responseDTO);
    }

}
