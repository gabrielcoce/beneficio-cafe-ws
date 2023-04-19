package com.umg.ws.bc.exceptions;

import com.umg.ws.bc.payload.response.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public final ResponseEntity<Object> handleAuthenticationException(Exception ex, HttpServletRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", details, request.getServletPath());
        return new ResponseEntity(error, HttpStatus.UNAUTHORIZED);
    }

    /*@ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleExceptionInternal(Exception ex, HttpServletRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error", details, request.getServletPath());
        return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }*/

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<Object> handleAccessDeniedException(Exception ex, HttpServletRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN, "Forbidden", details, request.getServletPath());
        return new ResponseEntity(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<Object> handleConstraintViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        List<String> details = new ArrayList<>();
        if ((ex.getCause() != null) && ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            String specificCauseMessage = (ex).getRootCause()
                    .getMessage();
            details.add(specificCauseMessage);
            ErrorResponse error = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Data Base Error", details, request.getServletPath());
            return new ResponseEntity(error, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        details.add(ex.getMessage());
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error", details, request.getServletPath());
        return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(RecordNotFoundException ex, HttpServletRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "Record Not Found", details, request.getServletPath());
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(error.getField() + " " + error.getDefaultMessage());
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            details.add(error.getObjectName() + " " + error.getDefaultMessage());
        }
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, "Validation Failed", details, request.getDescription(false));
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }


}
