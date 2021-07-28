package com.example.board.rest.errorController;

import com.example.board.rest.errorController.exception.BoardAppIncorrectEnumException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectIdException;
import com.example.board.rest.errorController.exception.BoardAppIncorrectRoleException;
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

    @ExceptionHandler(value = {
            BoardAppIncorrectIdException.class,
            BoardAppIncorrectRoleException.class,
            BoardAppIncorrectEnumException.class
    })
    public ResponseEntity<ErrorResponse> handleOtherException(Exception e) {
        String msg;
        if (e instanceof NestedRuntimeException) {
            msg = ((NestedRuntimeException) e).getMostSpecificCause().getMessage();
        } else {
            msg = e.getMessage();
        }
        return new ResponseEntity<>(new ErrorResponse(msg), HttpStatus.BAD_REQUEST);
    }

}



