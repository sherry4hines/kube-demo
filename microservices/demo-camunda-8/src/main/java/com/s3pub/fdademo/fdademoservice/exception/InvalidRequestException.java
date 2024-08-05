package com.s3pub.fdademo.fdademoservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends RuntimeException {
	private static final long serialVersionUID = -2757792383962206294L;

	public InvalidRequestException ( String mesg) {
		super(mesg);
	}
}