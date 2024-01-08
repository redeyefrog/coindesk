package org.redeyefrog.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redeyefrog.dto.CommonResponse;
import org.redeyefrog.exception.CoinDeskRuntimeException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public CommonResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = StringUtils.join(e.getAllErrors().stream()
                          .map(ObjectError::getDefaultMessage)
                          .collect(Collectors.toList()), ",");
        return CommonResponse.builder()
                             .resultDesc(message)
                             .build();
    }

    @ExceptionHandler(value = {CoinDeskRuntimeException.class, Exception.class, RuntimeException.class})
    public CommonResponse onException(Exception e) {
        log.error(e.getMessage(), e);
        return CommonResponse.builder()
                             .resultDesc("System Error.")
                             .build();
    }

}
