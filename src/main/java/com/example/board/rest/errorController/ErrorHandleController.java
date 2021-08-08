package com.example.board.rest.errorController;

import com.example.board.rest.errorController.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedRuntimeException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ErrorHandleController {

    private static final Logger log = LoggerFactory.getLogger(ErrorHandleController.class);

    @ExceptionHandler(value = {
            BoardAppIncorrectIdException.class,
            BoardAppIncorrectRoleException.class,
            BoardAppIncorrectEnumException.class,
            BoardAppIncorrectStateException.class,
            BoardAppConsistencyViolationException.class,
            BoardAppPermissionViolationException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ErrorResponse> handleOtherException(Exception e) {
        String msg;
        if (e instanceof NestedRuntimeException) {
            msg = ((NestedRuntimeException) e).getMostSpecificCause().getMessage();
        } else {
            msg = e.getMessage();
        }
        log.warn(msg);
        log.debug("Details:", e);
        return new ResponseEntity<>(new ErrorResponse(msg), HttpStatus.BAD_REQUEST);
    }

}



