package yte.intern.project.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.common.enums.MessageType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionResolver {
    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public void handleException(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException exception)
            throws IOException {

        MessageResponse messageResponse = new MessageResponse(MessageType.ERROR, exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(new ObjectMapper().writeValueAsString(messageResponse));
    }
}
