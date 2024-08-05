package com.s3pub.fdademo.fdademoservice.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s3pub.fdademo.fdademoservice.ProcessConstants;
import com.s3pub.fdademo.fdademoservice.domain.HumanFoodEntry;
import com.s3pub.fdademo.fdademoservice.domain.Manufacturer;
import com.s3pub.fdademo.fdademoservice.domain.ScreenReview;
import com.s3pub.fdademo.fdademoservice.dto.CompleteUserTaskDTO;
import com.s3pub.fdademo.fdademoservice.dto.FDADispositionNotice;
import com.s3pub.fdademo.fdademoservice.dto.HumanFoodEntryDTO;
import com.s3pub.fdademo.fdademoservice.dto.ScreenReviewDTO;
import com.s3pub.fdademo.fdademoservice.exception.InvalidRequestException;
import com.s3pub.fdademo.fdademoservice.exception.ResourceNotFoundException;
import com.s3pub.fdademo.fdademoservice.mapper.HumanFoodEntryMapper;
import com.s3pub.fdademo.fdademoservice.mapper.ScreenReviewMapper;
import com.s3pub.fdademo.fdademoservice.repo.HumanFoodEntryRepository;
import com.s3pub.fdademo.fdademoservice.repo.ManufacturerRepository;
import com.s3pub.fdademo.fdademoservice.repo.ScreenReviewRepository;

@Service
@Transactional(
		  propagation = Propagation.REQUIRED, 
		  readOnly = false, 
		  timeout = 30)
public class FoodService {

	  private static final Logger LOG = LoggerFactory.getLogger(FoodService.class);

	private static final int NUMBER_OF_DAYS_NOTICE = 4;
	private static final int PRIOR_NOTICE_CHECK_WEIGHT = 1;
	private static final boolean PRIOR_NOTICE_CHECK_REQUIRES_MANUAL_REVIEW = false;
	private static final int REGISTERED_MANUFACTURER_CHECK_WEIGHT = 2;
	private static final int RISK_SCORE_THRESHOLD = 80;
	
	@Autowired
	HumanFoodEntryMapper entryMapper;
	
	@Autowired
	HumanFoodEntryRepository entryRepo;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	ScreenReviewRepository reviewRepo;
	
	@Autowired
	ManufacturerRepository manufacturerRepo;
	
	@Autowired
	ScreenReviewMapper reviewMapper;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	public ScreenReviewDTO priorNoticeComplianceCheck(String shipmentId, String arrivalDate, String submissionDate, String reviewCheck) {
		LOG.info("Performing Prior Notice Compliance Check for shipment ID: {}", shipmentId);
		Calendar arrived = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		LocalDateTime dt = LocalDateTime.parse(arrivalDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		arrived.setTimeInMillis(dt.toInstant(ZoneOffset.UTC).toEpochMilli());
		Calendar submitted = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		dt = LocalDateTime.parse(submissionDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		submitted.setTimeInMillis(dt.toInstant(ZoneOffset.UTC).toEpochMilli());
		submitted.add(Calendar.DAY_OF_MONTH, NUMBER_OF_DAYS_NOTICE);
		ScreenReview review = new ScreenReview();
		Optional<ScreenReview> found = reviewRepo.findFirstByShipmentIdAndReviewCheck(shipmentId, reviewCheck);
		if (found.isEmpty()) {
			LOG.warn("Screen Review was not found for shipment ID: {}, check: {}", shipmentId, reviewCheck);
			review.setShipmentId(shipmentId);
			review.setAutomatedCheck(true);
			review.setCheckWeight(PRIOR_NOTICE_CHECK_WEIGHT);
			review.setRequireManualReview(PRIOR_NOTICE_CHECK_REQUIRES_MANUAL_REVIEW);
			review.setReviewCheck(reviewCheck);
			review.setReviewerRequiredRole("");
			review.setCreateDate(LocalDateTime.now(ZoneId.of("UTC")));
			review.setCreateUser("system");
		} else {
			review = found.get();
		}
		LOG.debug("Submitted: {}, Arrived: {}", submitted, arrived  );
		review.setCheckPassed(submitted.before(arrived));
		ScreenReview saved = reviewRepo.save(review);
		return reviewMapper.toDto(saved);
	}
	
	public ScreenReviewDTO registeredManufacturerCheck(String shipmentId, 
			String feiNumber, String companyName, 
			String address, String city, String country, String reviewCheck) {
		LOG.info("Performing Registered Manufacturer Check for shimentId: {}, FEI: {}", shipmentId, feiNumber);
		Optional<Manufacturer> manuf;
		boolean registered = false;
		boolean reqManualReview = false;
		if (feiNumber != null && feiNumber.trim().length() == 10) {
			 manuf = manufacturerRepo.findByFeiNumber(feiNumber);
			 if (manuf.isPresent() 
				 && manuf.get().getManufacturerLegalName().equalsIgnoreCase(companyName)) {
				 registered = true;
			 } else {
				 LOG.warn("Invalid FEI Number: {}", feiNumber);
			 }
			 if (registered 
				 && (!manuf.get().getManufacturerCountry().equalsIgnoreCase(country)
				    || !manuf.get().getManufacturerCity().equalsIgnoreCase(city)
				    || !manuf.get().getManufacturerAddress1().equalsIgnoreCase(address))
				 ) {
				 reqManualReview = true;
			 }
		} else { 
			throw new InvalidRequestException("This automated review requires a Manufacturer FEI number");
		}
		ScreenReview review = new ScreenReview();
		Optional<ScreenReview> found = reviewRepo.findFirstByShipmentIdAndReviewCheck(shipmentId, reviewCheck);
		if (found.isEmpty()) {
			LOG.warn("Screen Review was not found for shipment ID: {}, check: {}", shipmentId, reviewCheck);
			review.setShipmentId(shipmentId);
			review.setAutomatedCheck(true);
			review.setCheckWeight(REGISTERED_MANUFACTURER_CHECK_WEIGHT);
			review.setRequireManualReview(reqManualReview);
			review.setReviewerRequiredRole(ProcessConstants.REVIEWER_GROUP_TAG);
			review.setReviewCheck(reviewCheck);
			review.setCreateDate(LocalDateTime.now(ZoneId.of("UTC")));
			review.setCreateUser("system");
		} else {
			review = found.get();
		}
		review.setCheckPassed(registered);
		
		ScreenReview saved = reviewRepo.save(review);
		return reviewMapper.toDto(saved);
	}
	
	public Map<String, Object> analyzeRisk(String shipmentId) { 
		Map<String, Object> results = new HashMap<>();
		boolean requireManualReview = false;
		int score = 0;
		int riskTotal = 0;
		int reviewCnt = 0;
		String disposition = ProcessConstants.FINAL_DISPOSITION_PENDING;
		LocalDateTime dispositionDate = LocalDateTime.now(ZoneId.of("UTC"));
		
		List<ScreenReview> reviews = reviewRepo.findAllByShipmentId(shipmentId);
		for (ScreenReview rvw : reviews ) {
			if (rvw.getRequireManualReview() == null) {
				rvw.setRequireManualReview(false);
			}
			if (rvw.getCheckPassed() == null) {
				rvw.setCheckPassed(false);
			}
			if (rvw.getRequireManualReview().booleanValue()) {
				requireManualReview = true;
			} else {
				int value = rvw.getCheckPassed().booleanValue() ? 100 : 0;
				riskTotal = riskTotal + (rvw.getCheckWeight() * value);
				reviewCnt = reviewCnt + rvw.getCheckWeight();
			}
		}
		if (reviewCnt > 0) {
			score = riskTotal / reviewCnt;
		}
		if (!requireManualReview) {
			if (score > RISK_SCORE_THRESHOLD) {
				disposition = ProcessConstants.FINAL_DISPOSITION_MPRO_ISSUED;
			} else {
				disposition = ProcessConstants.FINAL_DISPOSITION_ENTRY_DENIED;
			}
		}
		results.put(ProcessConstants.RISK_SUM_CONSOL_REVIEW_SCORE , score);
		results.put(ProcessConstants.RISK_SUM_FDA_DISPOSITION, disposition);
		results.put(ProcessConstants.RISK_SUM_FDA_DISPOSITION_DATE, dispositionDate);
		results.put(ProcessConstants.RISK_SUM_REQUIRE_MANUAL_REVIEW_TAG, requireManualReview);
		entryRepo.updateAnalyzeRiskResults(shipmentId, score, requireManualReview, disposition, dispositionDate);
		return results;
	}

	public void completeManualReview(Long processInstanceId, ScreenReviewDTO dto) throws JsonProcessingException {
		Optional<ScreenReview> found = reviewRepo.findById(dto.getReviewId());
		if (found.isPresent()) {
			found.get().setCheckPassed(dto.getCheckPassed());
			ScreenReview saved = reviewRepo.save(found.get());
			JSONObject obj = new JSONObject();
			obj.put("checkPassed", saved.getCheckPassed());
//TODO
//			CompleteUserTaskDTO dto = CompleteUserTaskDTO.builder()
//					.build();
//			commonService.completeUserTask(processInstanceId, saved.getReviewCheck(), obj);
			LOG.info("Completed Entry: {}", saved);
		} else {
			LOG.error("Something went wrong, entry for review id {} was not available", dto.getReviewId());
			throw new InvalidRequestException("Unable to complete manual review for id " + dto.getReviewId());
		}
	}
	
	public void sendDisposition(HumanFoodEntryDTO dto) throws JsonProcessingException {
		Optional<HumanFoodEntry> found = entryRepo.findFirstByShipmentId(dto.getShipmentId());
		if (found.isPresent()) {
			FDADispositionNotice notice = FDADispositionNotice.builder()
					.createDate(LocalDateTime.now(ZoneId.of("UTC")))
					.createUser("SYSTEM")
					.finalDisposition(found.get().getFinalDisposition())
					.finalDispositionDate(found.get().getFinalDispositionDate())
					.shipmentId(found.get().getShipmentId())
					.build();
			String msg = objectMapper.writeValueAsString(notice);
			CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(ProcessConstants.KAFKA_FDA_DISPOSITION_TOPIC, msg);
		    future.whenComplete((result, ex) -> {
		         if (ex == null) {
		             LOG.info("Sent message: {}, offset: {}", msg,  
		                 result.getRecordMetadata().offset());
		         } else {
		             LOG.error("Unable to send message: {}, Error: {}", msg, 
		                  ex.getMessage());
		         }
		     });
		 } else {
			LOG.error("Entry for shipment id {} not found!", dto.getShipmentId());
		}
	}
	
	public void saveEntryTask(HumanFoodEntryDTO entry) {
		if (entry.getCreateDate() == null) {
			entry.setCreateDate(LocalDateTime.now(ZoneId.of("UTC")));
		}
		entryRepo.save(entryMapper.toEntity(entry));
	}

	public HumanFoodEntryDTO getEntryByShipmentId(String shipmentId) {
		Optional<HumanFoodEntry> entry = entryRepo.findFirstByShipmentId(shipmentId);
		if (entry.isPresent()) {
			return entryMapper.toDto(entry.get());
		} else {
			throw new ResourceNotFoundException("HumanFoodEntry", "shipmentId", shipmentId);
		}
	}
}
