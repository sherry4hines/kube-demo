package com.s3pub.fdademo.fdademoservice;

public class ProcessConstants {

	private ProcessConstants() {
		super();
	}

	public static final String HUMAN_FOOD_BPMN_PROCESS_ID = "Human_Food_Entry_Process";
	public static final String HUMAN_FOOD_BPMN_PROCESS2_ID = "Human_Food_Entry_Process2";
	public static final String BPMN_PROCESS_ID = "camunda-process";
	public static final String RISK_SUM_REQUIRE_MANUAL_REVIEW_TAG = "manualReviewRequired";
	public static final String RISK_SUM_CONSOL_REVIEW_SCORE = "consolidateReviewScore";
	public static final String RISK_SUM_FDA_DISPOSITION = "finalDisposition";
	public static final String RISK_SUM_FDA_DISPOSITION_DATE = "finalDispositionDate";
	public static final String SHIPMENT_ID_TAG = "shipmentId";
	public static final String CONTENT_TYPE_TAG = "Content-Type";

	public static final String ACCESS_TOKEN_TAG = "access_token";
	public static final String REFRESH_TOKEN_TAG = "refresh_token";
	public static final String AUTHORIZATION_TAG = "Authorization";
	public static final String BEARER_TAG = "Bearer ";
	
	public static final String FINAL_DISPOSITION_MPRO_ISSUED = "MPro Issued";
	public static final String FINAL_DISPOSITION_ENTRY_DENIED = "Entry Denied";
	public static final String FINAL_DISPOSITION_PENDING = "Pending Manual Review";

	public static final String REVIEWER_GROUP_TAG = "Reviewers";
	public static final String SPECIALIZED_REVIEWER_GROUP_TAG = "SpecializedReviewers";
	
	public static final String KAFKA_FDA_DISPOSITION_TOPIC = "cbpFdaDisposition";
	public static final String EXCP_EMPTY_ARRAY_PASSED = "An empty array was passed to service for processing";
	public static final String RULE_ID_TAG = "ruleId";
	public static final String PROCESS_INSTANCE_ID = "processInstanceId";
}
