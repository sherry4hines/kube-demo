package com.s3pub.fdademo.fdademoservice.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Table(name = "screen_review", schema = "fdademo")
public class ScreenReview implements Serializable {
	private static final long serialVersionUID = 946849018128360531L;

	
	@Id
	@SequenceGenerator(name = "screen_review_review_id_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id")
	private Long reviewId;
	
	@Column(name = "shipment_id", length = 40)
	private String shipmentId;
	
	@Column(name = "review_check", length = 60, nullable = false)
	private String reviewCheck;
	
	@Column(name = "check_passed")
	private Boolean checkPassed;
	
	@Column(name = "automated_check")
	private Boolean automatedCheck;
	
	@Column(name = "require_manual_review")
	private Boolean requireManualReview;
	
	@Column(name = "reviewer_required_role")
	private String reviewerRequiredRole;
	
	@Column(name = "check_weight")
	private Integer checkWeight;
	
	@Column(name = "create_date")
	private LocalDateTime createDate;
	
	@Column(name = "create_user")
	private String createUser;

}
