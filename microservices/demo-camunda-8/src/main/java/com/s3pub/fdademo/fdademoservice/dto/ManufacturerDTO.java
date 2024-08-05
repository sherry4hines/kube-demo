package com.s3pub.fdademo.fdademoservice.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ManufacturerDTO implements Serializable {
	private static final long serialVersionUID = 4744559837647300425L;

	@NotEmpty(message = "Manufacturer FEI number is a required field, must be 10 chars")
	@Size(min=10, max = 10, message = "The length of the Manufacturer FEI number should be 10 characters ")
	@Schema(description = "Manufacturer Federal Entity Identifier(FEI) number")
	private String feiNumber;
	
	@NotEmpty(message = "Manufacturer Legal Name is a required field, must be 100 chars or less")
	@Size(min=10, max = 10, message = "The length of the Manufacturer Legal Name should be 100 characters or less ")
	@Schema(description = "Manufacturer Legal Name as registered with IRS")
	private String manufacturerLegalName;

	@NotEmpty(message = "Manufacturer Street Address is a required field, must be 100 chars or less")
	@Size(max = 100, message = "The length of the Manufacturer Street Address should be 100 characters or less ")
	@Schema(description = "Manufacturer Street Address")
	private String manufacturerAddress1;
	
	@Size(max = 100, message = "The length of the Manufacturer Street Address 2nd Line should be 100 characters or less ")
	@Schema(description = "Manufacturer Street Address 2nd Line")
	private String manufacturerAddress2;
	
	@NotEmpty(message = "Manufacturer City is a required field, must be 100 chars or less")
	@Size(max = 100, message = "The length of the Manufacturer City should be 100 characters or less ")
	@Schema(description = "Manufacturer City")
	private String manufacturerCity;
	
	@NotEmpty(message = "Manufacturer Country is a required field, must be 100 chars or less")
	@Size(max = 100, message = "The length of the Manufacturer Country should be 100 characters or less ")
	@Schema(description = "Manufacturer Country")
	private String manufacturerCountry; 
	
	private LocalDateTime createDate;
	private String createUser;

}
