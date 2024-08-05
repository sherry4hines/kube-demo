package com.s3pub.fdademo.fdademoservice.repo;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.s3pub.fdademo.fdademoservice.domain.HumanFoodEntry;

import jakarta.transaction.Transactional;

@Repository
public interface HumanFoodEntryRepository extends JpaRepository<HumanFoodEntry, Long>, JpaSpecificationExecutor<HumanFoodEntry> {

	public int deleteByShipmentId(String shipmentId);
	
	Optional<HumanFoodEntry> findFirstByShipmentId(String shipmentId);
	
	@Transactional
	@Modifying
	@Query(value = "update HumanFoodEntry set consolidateReviewScore = :score, manualReviewRequired = :reviewRequired, " + 
	"finalDisposition = :disposition, finalDispositionDate = :dispositionDate where shipmentId = :shipmentId")
	public int updateAnalyzeRiskResults(@Param("shipmentId") String shipmentId, @Param("score") Integer score, 
			@Param("reviewRequired") boolean reviewRequired,
			@Param("disposition") String disposition, @Param("dispositionDate") LocalDateTime dispositionDate);
}
