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
public class ReviewRuleDTO implements Serializable {
	private static final long serialVersionUID = 358512542960561891L;

	private Long ruleId;
	
	@NotEmpty(message = "Product Category is a required field, must be 40 chars or less")
	@Size(max = 40, message = "The length of the Product Category code should be 40 characters ")
	@Schema(description = "Product Category Code")
	private String productCategory;
	
	@NotEmpty(message = "FDA Product Code is a required field, must be 7 chars")
	@Size(min = 7, max = 7, message = "The length of the FDA Product Code should be 7 characters ")
	@Schema(description = "FDA Product Code")
	private String productCode;
	
	@NotEmpty(message = "Product Review Code is a required field, must be 60 chars or less")
	@Size(max = 60, message = "The length of the Product Review code should be 60 characters ")
	@Schema(description = "Product Review Code")
	private String reviewCheck;
	
	@NotNull
	@Schema(description = "Does this review include an automated component?" )
	private Boolean automatedCheck;
	
	@NotNull
	@Schema(description = "Does this review require final manual approval from a user?" )
	private Boolean requireManualReview;
	
	@NotNull
	@Min(1)
	@Max(5)
	@Schema(description = "Weight of importance of check relative to others must be between 1 - 5 ")
	private Integer checkWeight;

	@NotEmpty
	@Size(max = 255, message = "The length of the Required Reviewer role list should be 255 characters or less ")
	@Schema(description = "Comma separated list of roles qualified to conduct this review")
	private String reviewerRequiredRole;
	
	private LocalDateTime createDate;
	private String createUser;
	private LocalDateTime inactiveDate;
	private LocalDateTime lupdateDate;
	private String lupdateUser;

}
