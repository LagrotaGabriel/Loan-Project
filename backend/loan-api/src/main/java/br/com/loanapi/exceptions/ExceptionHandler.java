package br.com.loanapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@ControllerAdvice
@RestController
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<StandartError> invalidRequestException(InvalidRequestException invalidRequestException,
                                                                 HttpServletRequest httpServletRequest){

        StandartError standartError = new StandartError(
                LocalDateTime.now(),
                400,
                invalidRequestException.getMessage(),
                httpServletRequest.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standartError);

    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandartError> objectNotFoundException(ObjectNotFoundException objectNotFoundException,
                                                                 HttpServletRequest httpServletRequest) {

        StandartError standartError = new StandartError(
                LocalDateTime.now(),
                404,
                objectNotFoundException.getMessage(),
                httpServletRequest.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standartError);

    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ConnectionFailedException.class)
    public ResponseEntity<StandartError> connectionFailedException(ConnectionFailedException connectionFailedException,
                                                                   HttpServletRequest httpServletRequest) {

        StandartError standartError = new StandartError(
                LocalDateTime.now(),
                403,
                connectionFailedException.getMessage(),
                httpServletRequest.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(standartError);

    }

}
