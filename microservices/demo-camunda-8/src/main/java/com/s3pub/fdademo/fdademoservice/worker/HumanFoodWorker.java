package com.s3pub.fdademo.fdademoservice.worker;


import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.VariablesAsType;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.s3pub.fdademo.fdademoservice.ProcessConstants;
import com.s3pub.fdademo.fdademoservice.dto.HumanFoodEntryDTO;
import com.s3pub.fdademo.fdademoservice.service.FoodService;

@Component
public class HumanFoodWorker {

  private static final Logger LOG = LoggerFactory.getLogger(HumanFoodWorker.class);

  private final FoodService foodService;

  public HumanFoodWorker(FoodService svc) {
    this.foodService = svc;
  }

  @JobWorker(type = "priorNoticeComplianceCheck", autoComplete = true)
  public void priorNoticeComplianceCheck(final JobClient client, final ActivatedJob job) {
	  Map<String, Object> variables = job.getVariablesAsMap();
    LOG.info("Invoking priorNoticeComplianceCheck with variables: {}", variables);

    foodService.priorNoticeComplianceCheck((String)variables.get("shipmentId"), 
    		(String) variables.get("arrivalDate"), 
    		(String) variables.get("submissionDate"),
    		"priorNoticeComplianceCheck");
  }
  
  @JobWorker(type = "registeredManufacturerCheck", autoComplete = true)
  public void registeredManufacturerCheck( final JobClient client, final ActivatedJob job) {
	  Map<String, Object> variables = job.getVariablesAsMap();
	  LOG.info("Invoking registeredManufacturerCheck with variables: {}", variables);
	  foodService.registeredManufacturerCheck((String) variables.get("shipmentId"), (String) variables.get("manufacturerFEINumber"),
			  (String) variables.get("manufacturerLegalName"), (String) variables.get("manufacturerAddress1"), 
			  (String) variables.get("manufacturerCity"), (String) variables.get("manufacturerCountry"),
			  "registeredManufacturerCheck");
  }
  

  @JobWorker(type = "analyzeRisk", autoComplete = true)
  public Map<String, Object> analyzeRisk( final JobClient client, final ActivatedJob job) {
	  Map<String, Object> variables = job.getVariablesAsMap();
	  LOG.info("Invoking analyzeRisk with variables: {}", variables);
	  return foodService.analyzeRisk((String) variables.get(ProcessConstants.SHIPMENT_ID_TAG));
	  
  }
  
  @JobWorker(type = "sendDisposition", autoComplete = true)
  public void sendDisposition (@VariablesAsType HumanFoodEntryDTO entry) throws JsonProcessingException {
	  LOG.info("Invoking sendDisposition with variables: {}", entry);
	  foodService.sendDisposition(entry);
  }
  
  @JobWorker(type = "saveEntryTask", autoComplete = true)
  public void saveEntryTask (final ActivatedJob job, @VariablesAsType HumanFoodEntryDTO entry) {
	  LOG.info("Invoking saveEntryTask with variables: {}", entry);
	  if (entry.getProcessInstanceId() == null || entry.getProcessInstanceId().trim().isEmpty()) {
		  entry.setProcessInstanceId(Long.toUnsignedString(job.getProcessInstanceKey()));
	  }
	  foodService.saveEntryTask(entry);
  }
  
}
