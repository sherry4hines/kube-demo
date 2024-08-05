package com.s3pub.fdademo.fdademoservice.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
@Table(name = "shipment_entry_line", schema = "fdademo")
public class HumanFoodEntry implements Serializable {
	private static final long serialVersionUID = -2295167598023927812L;
	
	@Id
	@Column(name = "shipment_id", length = 40, nullable = false)
	private String shipmentId;
	
	@Column(name = "submission_date")
	private LocalDateTime submissionDate; 
	
	@Column(name = "arrival_date")
	private LocalDateTime arrivalDate;
	
	@Column(name = "port_of_entry_division", length = 60, nullable = false)
	private String portOfEntryDivision;
	
	@Column(name = "country_of_origin", length = 100, nullable = false)
	private String countryOfOrigin;
	
	@Column(name = "product_category", length = 40, nullable = false)
	private String productCategory;
	
	@Column(name = "product_code", length = 7, nullable = false)
	private String productCode;
	
	@Column(name = "product_code_description", length = 255, nullable = false)
	private String productCodeDescription;
	
	@Column(name = "manufacturer_fei_number", length = 10 )
	private String manufacturerFEINumber;
	
	@Column(name = "manufacturer_legal_name", length = 255, nullable = false)
	private String manufacturerLegalName;
	
	@Column(name = "manufacturer_address1", length = 100 )
	private String manufacturerAddress1;
	
	@Column(name = "manufacturer_address2", length = 100 )
	private String manufacturerAddress2;
	
	@Column(name = "manufacturer_city", length = 100 )
	private String manufacturerCity;
	
	@Column(name = "manufacturer_country", length = 100 )
	private String manufacturerCountry;
	
	@Column(name = "filer_fei_number", length = 10 )
	private String filerFEINumber;
	
	@Column(name = "filer_country", length = 100 )
	private String filerCountry;
	
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="SHIPMENT_ID") 
	private List<ScreenReview> reviews;
    
	@Column(name = "final_disposition", length = 100 )
	private String finalDisposition;
	
	@Column(name = "final_disposition_date")
	private LocalDateTime finalDispositionDate; 
	
	@Column(name = "create_date")
	private LocalDateTime createDate;
	
	@Column(name = "create_user")
	private String createUser;
	
	@Column(name = "consolidate_review_score")
	private Integer consolidateReviewScore;
	
	@Column(name = "manual_review_required")
	private Boolean manualReviewRequired;
	
	@Column(name = "process_instance_id")
	private String processInstanceId;

}
