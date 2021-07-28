package com.example.board.rest.errorController;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class DefaultErrorHandleController {

    @Autowired
    Logger logger;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        logger.trace("logging exception", e);
        return new ResponseEntity<>(new ErrorResponse("Something unexpected happened: " + e.getMessage() + e.getStackTrace().toString()), HttpStatus.BAD_REQUEST);
    }
}
