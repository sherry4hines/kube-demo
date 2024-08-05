package com.s3pub.fdademo.fdademoservice.worker;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.s3pub.fdademo.fdademoservice.ProcessConstants;
import com.s3pub.fdademo.fdademoservice.domain.ScreenReview;
import com.s3pub.fdademo.fdademoservice.dto.ReviewRuleDTO;
import com.s3pub.fdademo.fdademoservice.dto.ScreenReviewDTO;
import com.s3pub.fdademo.fdademoservice.mapper.ScreenReviewMapper;
import com.s3pub.fdademo.fdademoservice.repo.ScreenReviewRepository;
import com.s3pub.fdademo.fdademoservice.service.ReviewRuleService;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import io.camunda.zeebe.spring.client.annotation.VariablesAsType;

@Component
public class ReviewRuleWorker {

	  private static final Logger LOG = LoggerFactory.getLogger(ReviewRuleWorker.class);

	  private final ReviewRuleService ruleService;
	  
	  private final ScreenReviewRepository reviewRepo;

	  private final ScreenReviewMapper reviewMapper;

	  public ReviewRuleWorker(ReviewRuleService svc, 
			  ScreenReviewRepository reviewRepo, 
			  ScreenReviewMapper reviewMapper) {
	    this.ruleService = svc;
	    this.reviewRepo = reviewRepo;
	    this.reviewMapper = reviewMapper;
	  }

	  @JobWorker(type = "saveReviewRule", autoComplete = true )
	  public Map<String, Object> saveReviewRule(@VariablesAsType ReviewRuleDTO rule) {
	    LOG.info("Invoking saveReviewRule with variables: {}", rule);

	    ReviewRuleDTO saved = ruleService.saveReviewRule(rule);
	    Map<String, Object> results = new HashMap<>();
	    results.put(ProcessConstants.RULE_ID_TAG, saved.getRuleId());
	    return results;
	  }

	  
	  @JobWorker(type = "createRequiredReviews", autoComplete = true )
	  public Map<String, Object> createRequiredReviews(
			  @Variable String productCategory, 
			  @Variable String productCode,
			  @Variable String shipmentId ) {
		  List<ScreenReviewDTO> reviews = new ArrayList<>();
		  List<ReviewRuleDTO> reviewRules = ruleService.findRulesForProduct(productCategory, productCode);
		  for (ReviewRuleDTO rule : reviewRules) {
			  ScreenReview rvw = ScreenReview.builder()
					  .shipmentId(shipmentId)
					  .checkPassed(false)
					  .checkWeight(rule.getCheckWeight())
					  .createDate(LocalDateTime.now(ZoneId.of("UTC")))
					  .createUser("system")
					  .requireManualReview(rule.getRequireManualReview())
					  .reviewCheck(rule.getReviewCheck())
					  .reviewerRequiredRole(rule.getReviewerRequiredRole())
					  .automatedCheck(rule.getAutomatedCheck())
					  .build();
			  ScreenReview saved = reviewRepo.save(rvw);
			  reviews.add(reviewMapper.toDto(saved));
		  }
		  Map<String, Object> results = new HashMap<>();
		  results.put("reviews", reviews);
		  return results;
	  }

}
