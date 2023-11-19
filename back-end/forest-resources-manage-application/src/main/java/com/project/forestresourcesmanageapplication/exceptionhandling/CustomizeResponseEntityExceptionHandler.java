package com.project.forestresourcesmanageapplication.exceptionhandling;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomizeResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{
    
    @ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionDetail> handlerException(Exception ex, WebRequest request) {
		ExceptionDetail exceptionDetail = new ExceptionDetail(LocalDateTime.now(),ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<ExceptionDetail>(exceptionDetail, HttpStatus.INTERNAL_SERVER_ERROR);
	}

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		String allMessages = ex.getFieldErrors().stream().map(fe -> fe.getDefaultMessage()).toString();
        ExceptionDetail exceptionDetail = new ExceptionDetail(LocalDateTime.now(), allMessages,
				request.getDescription(false));
		return new ResponseEntity<Object>(exceptionDetail, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = {DataNotFoundException.class , DataAlreadyExistsException.class , InvalidDataException.class})
	public ResponseEntity<ExceptionDetail> handleClientException(RuntimeException ex , WebRequest request){
		ExceptionDetail exceptionDetail = new ExceptionDetail(LocalDateTime.now(), ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<ExceptionDetail>(exceptionDetail,HttpStatus.BAD_REQUEST);
	}
}
