package com.s3pub.fdademo.fdademoservice.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponseDTO implements Serializable {
	private static final long serialVersionUID = 5473128912242081702L;
	private String status;
    private String message;
    private Object data;
}