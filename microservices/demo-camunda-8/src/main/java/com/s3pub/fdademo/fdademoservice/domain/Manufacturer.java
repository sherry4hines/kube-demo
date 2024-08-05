package com.s3pub.fdademo.fdademoservice.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "manufacturer", schema = "fdademo")
public class Manufacturer implements Serializable {
	private static final long serialVersionUID = 8603758907200575520L;
	
	@Id
	@Column(name = "fei_number", length = 10 )
	private String feiNumber;
	
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
	
	@Column(name = "create_date")
	private LocalDateTime createDate;
	
	@Column(name = "create_user")
	private String createUser;


}
