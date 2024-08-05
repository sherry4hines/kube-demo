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
public class FilterDTO implements Serializable {
	private static final long serialVersionUID = -1556977812317691933L;

	public enum Operator { EQ, NEQ, GTE, LTE, MA, NMA }
	
	private String name;
	private String operator;
	private String value;
}
