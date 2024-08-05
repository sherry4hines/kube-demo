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
@Table(name = "review_rule", schema = "fdademo")
public class ReviewRule  implements Serializable {
	private static final long serialVersionUID = 1333909992905662379L;

	@Id
	@SequenceGenerator(name = "review_rule_rule_id_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rule_id")
	private Long ruleId;
	
	@Column(name = "product_category", length = 40, nullable = false)
	private String productCategory;
	
	@Column(name = "product_code", length = 7, nullable = false)
	private String productCode;
	
	@Column(name = "review_check", length = 60, nullable = false)
	private String reviewCheck;
	
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
	
	@Column(name = "inactive_date")
	private LocalDateTime inactiveDate;
	
	@Column(name = "lupdate_date")
	private LocalDateTime lupdateDate;
	
	@Column(name = "lupdate_user")
	private String lupdateUser;

}
