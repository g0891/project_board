package com.example.board.rest.errorController;

import com.example.board.config.locale.LocaleTranslator;
import com.example.board.rest.errorController.exception.BoardAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @ExceptionHandler(value = {BoardAppException.class})
    public ResponseEntity<ErrorResponse> handleOtherException(BoardAppException e) {
        String msg = e.getMessage();
        log.warn(LocaleTranslator.translateForLogs(msg, e.getParams()));
        log.debug("Details:", e);
        return new ResponseEntity<>(new ErrorResponse(LocaleTranslator.translate(msg, e.getParams())), HttpStatus.BAD_REQUEST);
    }

}



