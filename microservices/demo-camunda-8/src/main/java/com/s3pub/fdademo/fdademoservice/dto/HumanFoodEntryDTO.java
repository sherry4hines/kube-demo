package com.s3pub.fdademo.fdademoservice.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class HumanFoodEntryDTO implements Serializable {
	private static final long serialVersionUID = 8718867031929467186L;
	
	@NotEmpty
	@Size(max = 40, message = "The CBP Shipment ID should be 40 characters or less ")
	@Schema(description = "Shipment ID assigned by CBP to this line entry")
	private String shipmentId;

	@NotNull
	@Schema(description = "Time stamp for when prior notice was submitted to FDA")
	private LocalDateTime submissionDate;
	
	@NotNull
	@Schema(description = "Time stamp for when shipment arrived at port of entry")
	private LocalDateTime arrivalDate;
	
	@NotEmpty(message = "Official Port of Entry is a required field, must be 40 chars or less")
	@Size(max = 40, message = "The length of the Port of Entry should be 40 characters or less ")
	@Schema(description = "Official Port of Entry Code")
	private String portOfEntryDivision;
	
	@NotEmpty(message = "Country of import origin is a required field, must be 100 chars or less")
	@Size(max = 100, message = "The length of the Import Origin Country should be 100 characters or less ")
	@Schema(description = "Country of import origin")
	private String countryOfOrigin;
	
	@NotEmpty(message = "Product Category is a required field, must be 40 chars or less")
	@Size(max = 40, message = "The length of the Product Category code should be 40 characters ")
	@Schema(description = "Product Category Code")
	private String productCategory;
	
	@NotEmpty(message = "FDA Product Description is a required field, must be 255 chars or less")
	@Size(max = 255, message = "The length of the FDA Product Code should be 255 characters or less ")
	@Schema(description = "FDA Product Description")
	private String productCodeDescription;
	
	@NotEmpty(message = "FDA Product Code is a required field, must be 7 chars")
	@Size(min = 7, max = 7, message = "The length of the FDA Product Code should be 7 characters ")
	@Schema(description = "FDA Product Code")
	private String productCode;

	@NotEmpty(message = "Manufacturer FEI number is a required field, must be 10 chars")
	@Size(min=10, max = 10, message = "The length of the Manufacturer FEI number should be 10 characters ")
	@Schema(description = "Manufacturer Federal Entity Identifier(FEI) number")
	private String manufacturerFEINumber;
	
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
	
	@NotEmpty(message = "Filer FEI number is a required field, must be 10 chars")
	@Size(min=10, max = 10, message = "The length of the Filer FEI number should be 10 characters ")
	@Schema(description = "Filer Federal Entity Identifier(FEI) number")
	private String filerFEINumber;
	
	@NotEmpty(message = "Filer Country is a required field, must be 100 chars or less")
	@Size(max = 100, message = "The length of the Filer Country should be 100 characters or less ")
	@Schema(description = "Country of official residence")
	private String filerCountry;
	
	private List<ScreenReviewDTO> reviews;
	
	@Size(max = 100, message = "The length of the Final Disposition code should be 100 characters or less ")
	@Schema(description = "Final disposition determined upon review")
	private String finalDisposition;
	
	@Schema(description = "Final disposition timestamp")
	private LocalDateTime finalDispositionDate;
	
	private LocalDateTime createDate;
	private String createUser;
	
	@Schema(description = "Consolidated score of all reviews completed used to determine if shipment meets threshold for allowing entry")
	private Integer consolidateReviewScore;

	@Schema(description = "Completion of all required reviews will require manual review")
	private Boolean manualReviewRequired;
	
	private String processInstanceId;
 

}
