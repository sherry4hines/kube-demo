package com.s3pub.fdademo.fdademoservice.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class ScreenReviewDTO implements Serializable {
	private static final long serialVersionUID = -5627718617960920217L;

	private Long reviewId;
	
	@NotEmpty(message = "Shipment ID is a required field, must be 40 chars or less")
	@Size(max = 40, message = "The length of the Shipment ID should be 40 characters ")
	@Schema(description = "Unique Shipment Identifier for an entry line")
	private String shipmentId;
	
	@NotEmpty(message = "Product Review Code is a required field, must be 60 chars or less")
	@Size(max = 60, message = "The length of the Product Review code should be 60 characters ")
	@Schema(description = "Product Review Code")
	private String reviewCheck;
	
	@Schema(description = "Did this shipment entry pass the review?")
	private Boolean checkPassed;

	@NotNull
	@Schema(description = "Does this review include an automated component?" )
	private Boolean automatedCheck;
	
	@NotNull
	@Schema(description = "Does this review require final manual approval from a user?" )
	private Boolean requireManualReview;

	@NotEmpty
	@Size(max = 255, message = "The length of the Required Reviewer role list should be 255 characters or less ")
	@Schema(description = "Comma separated list of roles qualified to conduct this review")
	private String reviewerRequiredRole;
	
	@NotNull
	@Min(1)
	@Max(5)
	@Schema(description = "Weight of importance of check relative to others must be between 1 - 5 ")
	private Integer checkWeight;
	
	private LocalDateTime createDate;
	private String createUser;
}
