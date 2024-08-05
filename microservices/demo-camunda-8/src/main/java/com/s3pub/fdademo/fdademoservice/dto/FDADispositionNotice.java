package com.s3pub.fdademo.fdademoservice.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class FDADispositionNotice implements Serializable {
	private static final long serialVersionUID = 6765597821749025115L;

	private String shipmentId;
	
	private String finalDisposition;
	
	private LocalDateTime finalDispositionDate;
	
	private LocalDateTime createDate;
	private String createUser;
	
}
