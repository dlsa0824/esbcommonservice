package indi.daniel.esbcommonservice.common.controller;

import indi.daniel.esbcommonservice.common.controller.model.RestServiceResponse;
import indi.daniel.esbcommonservice.common.exception.AuthTaxIdException;
import indi.daniel.esbcommonservice.common.exception.BackendInvalidResponseException;
import indi.daniel.esbcommonservice.common.exception.InvalidJwtTokenException;
import indi.daniel.esbcommonservice.common.exception.SendMailException;
import indi.daniel.esbcommonservice.common.utils.ExceptionUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.apache.logging.log4j.core.config.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static indi.daniel.esbcommonservice.common.controller.ResponseCode.AUTH_TAX_ID_FAIL;
import static indi.daniel.esbcommonservice.common.controller.ResponseCode.BACKEND_INVALID_RESPONSE;
import static indi.daniel.esbcommonservice.common.controller.ResponseCode.DATA_ACCESS_FAIL;
import static indi.daniel.esbcommonservice.common.controller.ResponseCode.ERROR;
import static indi.daniel.esbcommonservice.common.controller.ResponseCode.INVALID_JWT_TOKEN_EXCEPTION;
import static indi.daniel.esbcommonservice.common.controller.ResponseCode.MEDIA_TYPE_NOT_SUPPORTED;
import static indi.daniel.esbcommonservice.common.controller.ResponseCode.MISSING_REQUEST_PARAMETER;
import static indi.daniel.esbcommonservice.common.controller.ResponseCode.NO_RESOURCE_FOUND;
import static indi.daniel.esbcommonservice.common.controller.ResponseCode.PARAM_CONVERT_FAIL;
import static indi.daniel.esbcommonservice.common.controller.ResponseCode.REQUEST_BODY_PARSE_FAIL;
import static indi.daniel.esbcommonservice.common.controller.ResponseCode.REQUEST_BODY_VALID_FAIL;
import static indi.daniel.esbcommonservice.common.controller.ResponseCode.REQUEST_METHOD_NOT_SUPPORTED;
import static indi.daniel.esbcommonservice.common.controller.ResponseCode.REQUEST_PARAM_VALID_FAIL;
import static indi.daniel.esbcommonservice.common.controller.ResponseCode.SEND_MAIL_FAIL;
// import static indi.daniel.esbcommonservice.common.controller.ResponseCode.SEND_MAIL_FAIL;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class ControllerAdviceHandler {

    @Autowired
    ExceptionUtils exceptionUtils;

    // 處理缺少輸入參數, ex. @RequestParam, @RequestHeader
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MissingPathVariableException.class,
            MissingRequestHeaderException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestServiceResponse exceptionHandler(MissingRequestValueException e) {
        RestServiceResponse response = new RestServiceResponse(MISSING_REQUEST_PARAMETER);
        response.setErrorMessages(Collections.singletonList(e.getMessage()));
        return response;
    }

    // 處理找不到對應的資源(URI)
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestServiceResponse exceptionHandler(NoResourceFoundException e) {
        RestServiceResponse response = new RestServiceResponse(NO_RESOURCE_FOUND);
        response.setErrorMessages(Collections.singletonList(e.getMessage()));
        return response;
    }

    // 處理參數型態轉換錯誤, ex. 將String轉成Integer失敗
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestServiceResponse exceptionHandler(MethodArgumentTypeMismatchException e) {
        RestServiceResponse response = new RestServiceResponse(PARAM_CONVERT_FAIL);
        response.setErrorMessages(Collections.singletonList(e.getMessage()));
        return response;
    }

    // 處理檢核參數失敗, ex. Min(10), *** 需要在controller加上@Validate ***
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestServiceResponse exceptionHandler(ConstraintViolationException e) {
        RestServiceResponse response = new RestServiceResponse(REQUEST_BODY_VALID_FAIL);
        List<String> errorMessages = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        response.setErrorMessages(errorMessages);
        return response;
    }

    // 處理不支援HTTP methods
    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public RestServiceResponse exceptionHandler(HttpRequestMethodNotSupportedException e) {
        RestServiceResponse response = new RestServiceResponse(REQUEST_METHOD_NOT_SUPPORTED);
        response.setErrorMessages(Collections.singletonList(e.getMessage()));
        return response;
    }

    // 處理方法上參數檢核失敗, ex. Min(10)
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestServiceResponse exceptionHandler(HandlerMethodValidationException e) {
        RestServiceResponse response = new RestServiceResponse(REQUEST_PARAM_VALID_FAIL);
        response.setErrorMessages(Collections.singletonList(e.getMessage()));
        return response;
    }

    // 處理讀取JSON request body失敗
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public RestServiceResponse exceptionHandler(HttpMessageNotReadableException e) {
        RestServiceResponse response = new RestServiceResponse(REQUEST_BODY_PARSE_FAIL);
        response.setErrorMessages(Collections.singletonList(e.getMessage()));
        return response;
    }

    // 處理validation exceptions, ex. 檢核@Valid request body失敗
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestServiceResponse exceptionHandler(MethodArgumentNotValidException e) {
        RestServiceResponse response = new RestServiceResponse(REQUEST_BODY_VALID_FAIL);
        List<String> errorMessages = new ArrayList<>();
        List<FieldError> fieldErrors  = e.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errorMessages.add(fieldError.getField() + " " + fieldError.getDefaultMessage());
        }
        response.setErrorMessages(errorMessages);
        return response;
    }

    // 處理MediaType檢核失敗, ex. consumeType, produceType
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public RestServiceResponse exceptionHandler(HttpMediaTypeNotSupportedException e) {
        RestServiceResponse response = new RestServiceResponse(MEDIA_TYPE_NOT_SUPPORTED);
        response.setErrorMessages(Collections.singletonList(e.getMessage()));
        return response;
    }

    // 處理AuthTaxIdException
    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestServiceResponse exceptionHandler(AuthTaxIdException e) {
        RestServiceResponse response = new RestServiceResponse(AUTH_TAX_ID_FAIL);
        return response;
    }

    // 處理SendMailException
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestServiceResponse exceptionHandler(SendMailException e) {
        RestServiceResponse response = new RestServiceResponse(SEND_MAIL_FAIL);
        response.setErrorMessages(Collections.singletonList(exceptionUtils.getStackTraceAsString(e)));
        return response;
    }

    // 處理DataAccessException
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestServiceResponse exceptionHandler(DataAccessException e) {
        RestServiceResponse response = new RestServiceResponse(DATA_ACCESS_FAIL);
        response.setErrorMessages(Collections.singletonList(exceptionUtils.getStackTraceAsString(e)));
        return response;
    }

    // 處理 BackendInvalidResponseException
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestServiceResponse exceptionHandler(BackendInvalidResponseException e) {
        RestServiceResponse response = new RestServiceResponse(BACKEND_INVALID_RESPONSE);
        response.setErrorMessages(Collections.singletonList(e.getMessage()));
        return response;
    }

        // 處理 BackendInvalidResponseException
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestServiceResponse exceptionHandler(InvalidJwtTokenException e) {
        RestServiceResponse response = new RestServiceResponse(INVALID_JWT_TOKEN_EXCEPTION);
        response.setErrorMessages(Collections.singletonList(e.getMessage()));
        return response;
    }

    // 處理其他非預期exception
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestServiceResponse exceptionHandler(Exception e) {
        RestServiceResponse response = new RestServiceResponse(ERROR);
        response.setErrorMessages(Collections.singletonList(exceptionUtils.getStackTraceAsString(e)));
        return response;
    }
}
