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
public class UserTaskSearchDTO implements Serializable  {
	private static final long serialVersionUID = -4267748773027063448L;

	public enum State { CREATED, COMPLETED, CANCELED, FAILED }
	
	private String processInstanceKey;
	private String assignee;
	private Boolean assigned;
	private String candidateGroup;
	private String taskDefinitionId;
	private String processDefinitionKey;
	private String state;
	private FilterDTO[] taskVariables;
	
	private Integer pageSize;
	private SortOptionDTO[] sort;
	
}
