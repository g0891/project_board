package com.example.board.rest.errorController;

import com.example.board.config.locale.LocaleTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class DefaultErrorHandleController {

    private final static Logger log = LoggerFactory.getLogger(DefaultErrorHandleController.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {

        log.warn(LocaleTranslator.translateForLogs("CommonException.error", e.getMessage()));
        log.debug("Details:", e.getCause());
        e.printStackTrace();
        return new ResponseEntity<>(
                new ErrorResponse(LocaleTranslator.translate("CommonException.error", e.getMessage())),
                HttpStatus.BAD_REQUEST
        );
    }
}
