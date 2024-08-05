package com.s3pub.fdademo.fdademoservice.worker;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.VariablesAsType;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DemoWorker {

  private static final Logger LOG = LoggerFactory.getLogger(DemoWorker.class);

  @JobWorker(type = "recordReviewDecision", autoComplete = true)
  public void recordReviewDecision(@VariablesAsType JSONObject variables) {
    LOG.info("Invoking recordReviewDecision with variables: {}", variables);
  }
}
