package com.s3pub.fdademo.fdademoservice.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.s3pub.fdademo.fdademoservice.domain.ScreenReview;

@Repository
public interface ScreenReviewRepository  extends JpaRepository<ScreenReview, Long>, JpaSpecificationExecutor<ScreenReview> {

	List<ScreenReview> findAllByShipmentId(String shipmentId);

	Optional<ScreenReview> findFirstByShipmentIdAndReviewCheck(String shipmentId, String reviewCheck);
	
	int deleteAllByShipmentId(String string);
}
