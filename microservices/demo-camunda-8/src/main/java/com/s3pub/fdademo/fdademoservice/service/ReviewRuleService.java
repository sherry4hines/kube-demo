package com.s3pub.fdademo.fdademoservice.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import com.s3pub.fdademo.fdademoservice.ProcessConstants;
import com.s3pub.fdademo.fdademoservice.domain.ReviewRule;
import com.s3pub.fdademo.fdademoservice.dto.ReviewRuleDTO;
import com.s3pub.fdademo.fdademoservice.exception.InvalidRequestException;
import com.s3pub.fdademo.fdademoservice.mapper.ReviewRuleMapper;
import com.s3pub.fdademo.fdademoservice.repo.ReviewRuleRepository;

import jakarta.validation.Valid;

@Service
@Transactional(
		  propagation = Propagation.REQUIRED, 
		  readOnly = false, 
		  timeout = 30)
public class ReviewRuleService {

private static final Logger LOG = LoggerFactory.getLogger(ReviewRuleService.class);

	  @Autowired
	  private ReviewRuleRepository ruleRepo;
	  
	  @Autowired
	  private ReviewRuleMapper ruleMapper;
	  
	  public ReviewRuleDTO saveReviewRule(@Valid ReviewRuleDTO input) {
		  LOG.info("SaveReviewRule invoked with options: {}", input);
		  ReviewRule rule;
		  Optional<ReviewRule> found = ruleRepo.findByReviewCheck(input.getReviewCheck());
		  if (found.isPresent()) {
			  rule = found.get();
			  BeanUtils.copyProperties(input, rule, "createDate", "createUser");
		  } else {
			  rule = ruleMapper.toEntity(input);
			  rule.setCreateDate(LocalDateTime.now(ZoneId.of("UTC")));
			  if (rule.getCreateUser() == null || rule.getCreateUser().trim().isEmpty()) {
				  rule.setCreateUser("anonymous");
			  }
		  }
		  rule.setLupdateDate(LocalDateTime.now(ZoneId.of("UTC")));
		  if (rule.getLupdateUser() == null || rule.getLupdateUser().trim().isEmpty()) {
			  rule.setLupdateUser("anonymous");
		  }
		  ReviewRule saved = ruleRepo.save(rule);
		  return ruleMapper.toDto(saved);
	  }
	  
	  public List<ReviewRuleDTO> saveReviewRules(List<ReviewRuleDTO> entries) {
		  List<ReviewRuleDTO> savedEntries = new ArrayList<>();
		  if (entries == null || entries.isEmpty()) {
			  throw new InvalidRequestException(ProcessConstants.EXCP_EMPTY_ARRAY_PASSED);
		  }
		  for (ReviewRuleDTO dto : entries) {
			  savedEntries.add(saveReviewRule(dto));
		  }
		  return savedEntries;
	  }
	  
	  public void inactivateReviewRule(Long ruleId) {
		  ruleRepo.inactivateReviewRule(ruleId);
	  }
	  
	  public List<ReviewRuleDTO> findRulesForProduct(String productCategory, String productCode) {
		  if (productCode == null || productCode.trim().isEmpty()) {
			  throw new InvalidRequestException("This request requires a product code");
		  }
		  if (productCategory == null || productCategory.trim().isEmpty()) {
			  throw new InvalidRequestException("This request requires a product category code");
		  }
		  List<ReviewRule> rules = ruleRepo.findAllRulesByProductCategoryAndCode(productCategory, productCode);
		  List<ReviewRuleDTO> ruleDtos = new ArrayList<>();
		  for (ReviewRule rl : rules) {
			  ruleDtos.add(ruleMapper.toDto(rl));
		  }
		  return ruleDtos;
	  }
}
