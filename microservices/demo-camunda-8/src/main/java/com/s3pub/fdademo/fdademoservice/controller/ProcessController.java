package com.s3pub.fdademo.fdademoservice.controller;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.ZeebeFuture;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.s3pub.fdademo.fdademoservice.ProcessConstants;
import com.s3pub.fdademo.fdademoservice.dto.AssignUserTaskDTO;
import com.s3pub.fdademo.fdademoservice.dto.CompleteUserTaskDTO;
import com.s3pub.fdademo.fdademoservice.dto.HumanFoodEntryDTO;
import com.s3pub.fdademo.fdademoservice.dto.ProcessVariables;
import com.s3pub.fdademo.fdademoservice.dto.ScreenReviewDTO;
import com.s3pub.fdademo.fdademoservice.dto.UserTaskSearchDTO;
import com.s3pub.fdademo.fdademoservice.service.CommonService;
import com.s3pub.fdademo.fdademoservice.service.FoodService;

@RestController
@RequestMapping("/process")
@CrossOrigin
public class ProcessController {

  private static final Logger LOG = LoggerFactory.getLogger(ProcessController.class);
  private static final String PROCESS_START_LABEL = "Starting process: {} with variables: {}";
  private static final String PROCESS_RESULTS = "Results: {}";
  private final ZeebeClient zeebe;
  
  private final FoodService foodService;
  private final CommonService commonService;

  public ProcessController(
		  ZeebeClient client,
		  FoodService foodService,
		  CommonService commonService
		  ) {
    this.zeebe = client;
    this.foodService = foodService;
    this.commonService = commonService;
  }
  
  /* When returning objects defined by external API, 
   * will return json string and not externally 
   * defined object
   * 
   * Fetches Camunda 8 active User Task Record 
   * by instance key and task name
   */
  @GetMapping("/get-user-task/{instanceId}/{taskName}")
  public String getUserTask(
		  @PathVariable("instanceId") Long instanceId,
		  @PathVariable("taskName") String taskName) {
	  LOG.info("Received process/get-user-task Instance ID: {}, task name: {}", instanceId, taskName);
		return commonService.getUserTask(instanceId, taskName);
  }
  
  @PostMapping("/find-user-tasks")
  public String findUserTasks(
		  @RequestBody UserTaskSearchDTO search) {
	  LOG.info("Received process/find-user-tasks request: {}", search);
	  return commonService.findUserTasks(search);
  }
  
  @GetMapping("/get-instance/{instanceId}")
  public String getProcessInstance(
		  @PathVariable("instanceId") Long instanceId) {
	  LOG.info("Received process/get-instance Instance ID: {}", instanceId);
		return commonService.getProcessInstance(instanceId);
  }
  
  @GetMapping("/find-shipment-entry/{shipmentId}")
  public HumanFoodEntryDTO findShipmentEntryById(
		  @PathVariable("shipmentId") String shipmentId) {
	  return foodService.getEntryByShipmentId(shipmentId);
  }
  
  @PostMapping("/assign-user-task")
  public void assignUserTask(
		  @RequestBody AssignUserTaskDTO assignment
		  ) throws JsonProcessingException, NumberFormatException {
	  LOG.info("Assign Demo Reviewer: {}", assignment);
	  commonService.assignUserTask(assignment);
  }
  
  @PostMapping("/complete-user-task")
  public void completeUserTask(
		  @RequestBody CompleteUserTaskDTO assignment
		  ) throws NumberFormatException {
	  LOG.info("Complete User Task Request: {}", assignment);
	  commonService.completeUserTask(assignment);
  }
  
  @PostMapping("/complete-manual-review")
  public void completeManualReview(@RequestBody  ScreenReviewDTO entry ) throws JsonProcessingException, NumberFormatException {
	  LOG.info("Complete Manual Reviews with variables: {}", entry);
	  HumanFoodEntryDTO hfe = foodService.getEntryByShipmentId(entry.getShipmentId());
	  foodService.completeManualReview(Long.parseLong(hfe.getProcessInstanceId()), entry);
  }

  @PostMapping("/start/demo")
  public String startProcessInstanceDemo(
		  @RequestBody ProcessVariables variables) {

    LOG.info(PROCESS_START_LABEL, ProcessConstants.BPMN_PROCESS_ID, variables);

    ZeebeFuture<ProcessInstanceEvent> future = zeebe
        .newCreateInstanceCommand()
        .bpmnProcessId(ProcessConstants.BPMN_PROCESS_ID)
        .latestVersion()
        .variables(variables)
        .send();
    ProcessInstanceEvent event = future.join();
    JSONObject results = new JSONObject();
    results.put(ProcessConstants.PROCESS_INSTANCE_ID, event.getProcessInstanceKey());
    LOG.info(PROCESS_RESULTS, results);
    return results.toString();
  }

  @PostMapping("/start/human-food-process")
  public JSONObject startProcessInstanceHFP(@RequestBody HumanFoodEntryDTO entry) {

    LOG.info(PROCESS_START_LABEL, ProcessConstants.HUMAN_FOOD_BPMN_PROCESS_ID, entry);

    ZeebeFuture<ProcessInstanceEvent> future = zeebe
        .newCreateInstanceCommand()
        .bpmnProcessId(ProcessConstants.HUMAN_FOOD_BPMN_PROCESS_ID)
        .latestVersion()
        .variables(entry)
        .send();
    ProcessInstanceEvent event = future.join();
    JSONObject results = new JSONObject();
    results.put(ProcessConstants.PROCESS_INSTANCE_ID, Long.toUnsignedString(event.getProcessInstanceKey()));
    LOG.info(PROCESS_RESULTS, results);
    return results;

  }

  @PostMapping("/start/alt-human-food-process")
  public JSONObject startProcessInstanceHFP2(@RequestBody HumanFoodEntryDTO entry)  {

    LOG.info(PROCESS_START_LABEL, ProcessConstants.HUMAN_FOOD_BPMN_PROCESS2_ID, entry);

    ZeebeFuture<ProcessInstanceEvent> future = zeebe
        .newCreateInstanceCommand()
        .bpmnProcessId(ProcessConstants.HUMAN_FOOD_BPMN_PROCESS2_ID)
        .latestVersion()
        .variables(entry)
        .send();
    ProcessInstanceEvent event = future.join();

    JSONObject results = new JSONObject();
    results.put(ProcessConstants.PROCESS_INSTANCE_ID, Long.toUnsignedString(event.getProcessInstanceKey()));
    LOG.info(PROCESS_RESULTS, results);
    return results;

  }

  @PostMapping("/message/{messageName}/{correlationKey}")
  public void publishMessage(
      @PathVariable String messageName,
      @PathVariable String correlationKey,
      @RequestBody ProcessVariables variables) {

    LOG.info(
        "Publishing message `{}` with correlation key `{}` and variables: {}",
        messageName,
        correlationKey,
        variables);

    zeebe
        .newPublishMessageCommand()
        .messageName(messageName)
        .correlationKey(correlationKey)
        .variables(variables)
        .send();
  }

}
