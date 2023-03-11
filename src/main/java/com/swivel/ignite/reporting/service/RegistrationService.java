package com.swivel.ignite.reporting.service;

import com.swivel.ignite.reporting.dto.response.StudentResponseDto;
import com.swivel.ignite.reporting.dto.response.TuitionListResponseDto;
import com.swivel.ignite.reporting.exception.RegistrationServiceHttpClientErrorException;
import com.swivel.ignite.reporting.wrapper.StudentResponseWrapper;
import com.swivel.ignite.reporting.wrapper.TuitionListResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Registration Microservice
 */
@Slf4j
@Service
public class RegistrationService {


    private static final String FAILED_TO_GET_STUDENT_INFO = "Failed to get student info";
    private static final String FAILED_TO_GET_TUITION_LIST = "Failed to get tuition list";
    private final RestTemplate restTemplate;
    private final String getTuitionListUrl;
    private final String getStudentInfoUrl;

    public RegistrationService(@Value("${registration.baseUrl}") String baseUrl,
                               @Value("${registration.tuitionListUrl}") String tuitionListUrl,
                               @Value("${registration.studentInfoUrl}") String studentInfoUrl,
                               RestTemplate restTemplate) {
        this.getTuitionListUrl = baseUrl + tuitionListUrl;
        this.getStudentInfoUrl = baseUrl + studentInfoUrl;
        this.restTemplate = restTemplate;
    }

    /**
     * This method is used to get all tuition list from registration microservice
     *
     * @return tuition list response
     * @throws IOException
     */
    public TuitionListResponseDto getTuitionList() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        try {
            log.debug("Calling registration service to get tuition list. url: {},", getTuitionListUrl);
            ResponseEntity<TuitionListResponseWrapper> result = restTemplate.exchange(getTuitionListUrl, HttpMethod.GET,
                    entity, TuitionListResponseWrapper.class);
            String responseBody = Objects.requireNonNull(result.getBody()).getData().toLogJson();
            log.debug("Getting tuition list was successful. statusCode: {}, response: {}",
                    result.getStatusCode(), responseBody);
            return Objects.requireNonNull(result.getBody()).getData();
        } catch (HttpClientErrorException e) {
            throw new RegistrationServiceHttpClientErrorException(e.getStatusCode(), FAILED_TO_GET_TUITION_LIST,
                    e.getResponseBodyAsString(), e);
        }
    }

    /**
     * This method is used to get student info from registration microservice
     *
     * @param studentId student id
     * @return student response
     * @throws IOException
     */
    public StudentResponseDto getStudentInfo(String studentId) throws IOException {
        Map<String, String> uriParam = new HashMap<>();
        uriParam.put("studentId", studentId);

        UriComponents builder = UriComponentsBuilder.fromHttpUrl(getStudentInfoUrl).build();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        try {
            log.debug("Calling registration service to get student info. url: {},", getStudentInfoUrl);
            ResponseEntity<StudentResponseWrapper> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
                    entity, StudentResponseWrapper.class, uriParam);
            String responseBody = Objects.requireNonNull(result.getBody()).getData().toLogJson();
            log.debug("Getting student info by student id was successful. statusCode: {}, response: {}",
                    result.getStatusCode(), responseBody);
            return Objects.requireNonNull(result.getBody()).getData();
        } catch (HttpClientErrorException e) {
            throw new RegistrationServiceHttpClientErrorException(e.getStatusCode(), FAILED_TO_GET_STUDENT_INFO,
                    e.getResponseBodyAsString(), e);
        }
    }
}
