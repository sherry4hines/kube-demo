package com.s3pub.fdademo.fdademoservice.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class DateRangeDTO implements Serializable {
	private static final long serialVersionUID = -2480263028910851928L;
	
	private String from;
	private String to;
	

}
