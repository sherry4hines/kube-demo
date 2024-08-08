package com.s3pub.fdademo.fdademoservice.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s3pub.fdademo.fdademoservice.ProcessConstants;
import com.s3pub.fdademo.fdademoservice.config.JwtTokenVerifier;
import com.s3pub.fdademo.fdademoservice.dto.AssignUserTaskDTO;
import com.s3pub.fdademo.fdademoservice.dto.CompleteUserTaskDTO;
import com.s3pub.fdademo.fdademoservice.dto.UserTaskSearchDTO;
import com.s3pub.fdademo.fdademoservice.exception.InvalidRequestException;

@Service
public class CommonService {
	private static final Logger LOG = LoggerFactory.getLogger(CommonService.class);

	@Value("${keycloak.get-token-url}")
	String keycloakTokenUrl;

	@Value("${operate.base.url}")
	private String operateBaseUrl;

	@Value("${operate.client}")
	private String operateClient;

	@Value("${operate.client.secret}")
	private String operateClientSecret;

	@Value("${service.api.username}")
	private String apiUsername;

	@Value("${service.api.password}")
	private String apiUserPassword;

	@Value("${tasklist.base.url}")
	private String tasklistBaseUrl;

	@Value("${tasklist.client}")
	private String tasklistClient;

	@Value("${tasklist.client.secret}")
	private String tasklistClientSecret;

	@Value("${zeebe.base.url}")
	private String zeebeBaseUrl;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private JwtTokenVerifier verifier;

	RestTemplate restTemplate = new RestTemplate();

	Map<String, Map<String, String>> serviceTokens = new HashMap<>();

	// Retrieve user task by process instance id and task definition id
	public String getUserTask(Long processInstanceId, String taskDefinitionId) throws InterruptedException {
		String findTaskUrl = tasklistBaseUrl + "/v1/tasks/search";
		String task = "";
		JSONObject body = new JSONObject();
		body.put("processInstanceKey", Long.toString(processInstanceId));
		body.put("taskDefinitionId", taskDefinitionId);
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(new URI(findTaskUrl))
					.header(ProcessConstants.CONTENT_TYPE_TAG, MediaType.APPLICATION_JSON_VALUE)
					.header(ProcessConstants.AUTHORIZATION_TAG,
							ProcessConstants.BEARER_TAG + getApiToken(tasklistClient, tasklistClientSecret))
					.timeout(Duration.of(5, ChronoUnit.SECONDS))
					.POST(HttpRequest.BodyPublishers.ofString(body.toString())).build();
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			LOG.info("GetUserTaskId Response: {}", response);
			task = response.body();
		} catch (URISyntaxException | IOException e) {
			String errmesg = String.format("Error retrieving User Task for %s %s: %s", processInstanceId,
					taskDefinitionId, e.getMessage());
			LOG.error(errmesg);
			throw new InvalidRequestException(errmesg);
		}
		return task;
	}

	public String getProcessInstance(Long processInstanceId) throws InterruptedException {
		String findProcInstUrl = operateBaseUrl + "/v1/process-instances/search";
		LOG.info("Get Process Instance URL: {}", findProcInstUrl);
		JSONObject obj = new JSONObject();
		JSONObject filterOptions = new JSONObject();
		filterOptions.put("key", processInstanceId);
		obj.put("filter", filterOptions);
		LOG.info("Filter: {}", obj);
		String process = "";
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(new URI(findProcInstUrl))
					.header(ProcessConstants.AUTHORIZATION_TAG, ProcessConstants.BEARER_TAG + getApiToken(operateClient, operateClientSecret))
					.header(ProcessConstants.CONTENT_TYPE_TAG, MediaType.APPLICATION_JSON_VALUE)
					.timeout(Duration.of(5, ChronoUnit.SECONDS))
					.POST(HttpRequest.BodyPublishers.ofString(obj.toString())).build();
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			LOG.info("GetProcessInstance Response: {}", response);
			if (response.statusCode() == 200) {
				process = response.body();
			} else {
				throw new InvalidRequestException("Unable to retrieve process");
			}
			LOG.info("Process: {}", process);
		} catch (URISyntaxException | IOException e) {
			String errmesg = String.format("Error retrieving Process Instance for %s: %s", processInstanceId,
					e.getMessage());
			LOG.error(errmesg);
			throw new InvalidRequestException(errmesg);
		}
		return process;
	}

	public void completeUserTask(CompleteUserTaskDTO dto) throws InterruptedException {
		String completeTaskUrl = tasklistBaseUrl + "/v1/tasks/" + dto.getTaskId() + "/complete";
		JSONObject vars = new JSONObject(dto.getVariables());
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(new URI(completeTaskUrl))
					.header(ProcessConstants.AUTHORIZATION_TAG, ProcessConstants.BEARER_TAG + getApiToken(tasklistClient, tasklistClientSecret))
					.header(ProcessConstants.CONTENT_TYPE_TAG, MediaType.APPLICATION_JSON_VALUE)
					.timeout(Duration.of(5, ChronoUnit.SECONDS))
					.method(HttpMethod.PATCH.name(), HttpRequest.BodyPublishers.ofString(vars.toString())).build();
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			LOG.debug("CompleteUserTask Response: {}", response.statusCode());
		} catch (URISyntaxException | IOException e) {
			String errmesg = String.format("Error completing task for %s: %s", dto.getTaskId(),
					e.getMessage());
			LOG.error(errmesg);
			throw new InvalidRequestException(errmesg);
		}
	}

	public void assignUserTask(AssignUserTaskDTO dto) throws InterruptedException {
		String assignTaskUrl = tasklistBaseUrl + "/v1/tasks/" + dto.getTaskId() + "/assign";
		JSONObject body = new JSONObject();
		body.put("assignee", dto.getAssignee());
		body.put("allowOverride", false);
		body.put("action", "assign");
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(new URI(assignTaskUrl))
					.header(ProcessConstants.AUTHORIZATION_TAG, ProcessConstants.BEARER_TAG + getApiToken(tasklistClient, tasklistClientSecret))
					.header(ProcessConstants.CONTENT_TYPE_TAG, MediaType.APPLICATION_JSON_VALUE)
					.timeout(Duration.of(5, ChronoUnit.SECONDS))
					.method(HttpMethod.PATCH.name(), HttpRequest.BodyPublishers.ofString(body.toString())).build();
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			LOG.debug("AssignUserTask Response: {}", response);
		} catch (URISyntaxException | IOException e) {
			String errmesg = String.format("Error assigning task for %s: %s", dto.getTaskId(),
					e.getMessage());
			LOG.error(errmesg);
			throw new InvalidRequestException(errmesg);
		}
	}

	public void unassignUserTask(AssignUserTaskDTO dto) throws JsonProcessingException, InterruptedException {
		String unassignTaskUrl = tasklistBaseUrl + "/v1/tasks/" + dto.getTaskId() + "/unassign";
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(new URI(unassignTaskUrl))
					.header(ProcessConstants.AUTHORIZATION_TAG, ProcessConstants.BEARER_TAG + getApiToken(tasklistClient, tasklistClientSecret))
					.header(ProcessConstants.CONTENT_TYPE_TAG, MediaType.APPLICATION_JSON_VALUE)
					.timeout(Duration.of(5, ChronoUnit.SECONDS))
					.method(HttpMethod.PATCH.name(), HttpRequest.BodyPublishers.noBody()).build();
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			LOG.debug("UnassignUserTask Response: {}", response.statusCode());
		} catch (URISyntaxException | IOException e) {
			String errmesg = String.format("Error unassigning task for %s: %s", dto.getTaskId(),
					e.getMessage());
			LOG.error(errmesg);
			throw new InvalidRequestException(errmesg);
		}
	}

	private String getApiToken(String client, String secret) {
		Map<String, String> svctokens = null;
		boolean tokenIsValid = false;

	/*	if (serviceTokens.containsKey(client)) {
			svctokens = serviceTokens.get(client);
			tokenIsValid = verifier.isTokenValid(svctokens.get(ProcessConstants.ACCESS_TOKEN_TAG));
			LOG.info("Is current token valid? {}", tokenIsValid);
		}
	*/
		if (!serviceTokens.containsKey(client)) {
			MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
			formData.put("grant_type", Collections.singletonList("password"));
			formData.put("client_id", Collections.singletonList(client));
			formData.put("client_secret", Collections.singletonList(secret));
			formData.put("username", Collections.singletonList(apiUsername));
			formData.put("password", Collections.singletonList(apiUserPassword));
			LOG.info("Authorization URL: {}", keycloakTokenUrl);
			LOG.info("Admin Authorization request: {}", formData);
			ResponseEntity<Map<String, String>> result = (ResponseEntity<Map<String, String>>) restTemplate
					.postForEntity(keycloakTokenUrl, formData, serviceTokens.getClass());
			svctokens = result.getBody();
			LOG.info("{} Service Tokens: {}", client, svctokens);
			serviceTokens.put(client, svctokens);
		}
		if (svctokens != null && svctokens.get(ProcessConstants.ACCESS_TOKEN_TAG)!= null) {
			return svctokens.get(ProcessConstants.ACCESS_TOKEN_TAG);
		} else {
			LOG.warn("Access token unavailable");
			return "";
		}
	}

	public String getUserToken() {
		// Get Principal
		Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return jwt.getTokenValue();
	}

	public String findUserTasks(UserTaskSearchDTO search) throws InterruptedException {
		String findTaskUrl = tasklistBaseUrl + "/v1/tasks/search";
		LOG.info("Finding tasks url: {}", findTaskUrl);
		String task = "";
		String token = getUserToken();
		try {
			HttpClient client = HttpClient.newHttpClient();
			String body = objectMapper.writeValueAsString(search);
			HttpRequest request = HttpRequest.newBuilder().uri(new URI(findTaskUrl))
					.header(ProcessConstants.CONTENT_TYPE_TAG, MediaType.APPLICATION_JSON_VALUE)
					.header(ProcessConstants.AUTHORIZATION_TAG, ProcessConstants.BEARER_TAG + token).timeout(Duration.of(5, ChronoUnit.SECONDS))
					.POST(HttpRequest.BodyPublishers.ofString(body)).build();
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			LOG.info("GetUserTaskId Response: {}", response);
			task = response.body();
		} catch (URISyntaxException | IOException e) {
			String errmesg = String.format("Error finding tasks: %s", 
					e.getMessage());
			LOG.error(errmesg);
			throw new InvalidRequestException(errmesg);
		}
		return task;
	}
}
