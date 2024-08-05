package com.s3pub.fdademo.fdademoservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DemoService {
	  private static final Logger LOG = LoggerFactory.getLogger(DemoService.class);

  public Boolean recordReviewDecision(String businessKey) {
	  LOG.info("Business Key: {}", businessKey);
    return true;
  }
}
