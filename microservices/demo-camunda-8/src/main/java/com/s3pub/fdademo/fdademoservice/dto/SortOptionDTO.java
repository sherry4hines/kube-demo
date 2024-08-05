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
public class SortOptionDTO implements Serializable {
	private static final long serialVersionUID = -6730282716020134964L;

	public enum Order { ASC, DESC }
	
	private String field;
	private String order;
}
