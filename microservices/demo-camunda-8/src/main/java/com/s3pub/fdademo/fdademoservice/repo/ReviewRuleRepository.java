package com.s3pub.fdademo.fdademoservice.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.s3pub.fdademo.fdademoservice.domain.ReviewRule;

import jakarta.transaction.Transactional;

public interface ReviewRuleRepository   extends JpaRepository<ReviewRule, Long>, JpaSpecificationExecutor<ReviewRule> {

	List<ReviewRule> findAllByProductCategory(String productCategory);
	List<ReviewRule> findAllByProductCode(String productCode);

	int deleteAllByProductCode(String productCode);

	Optional<ReviewRule> findByReviewCheck(String reviewCheck);

	@Query(value = "select rl from ReviewRule rl where rl.productCategory = :ctgy or rl.productCode = :code")
	List<ReviewRule> findAllRulesByProductCategoryAndCode(@Param("ctgy") String ctgy, @Param("code") String code);
	
	@Transactional
	@Modifying
	@Query(value = "update ReviewRule set inactiveDate = current_timestamp where ruleId = :ruleId ")
	void inactivateReviewRule(@Param("ruleId") Long ruleId);
}
