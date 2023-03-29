package com.swivel.ignite.reporting.service;

import com.swivel.ignite.reporting.dto.response.TuitionListResponseDto;
import com.swivel.ignite.reporting.exception.TuitionServiceHttpClientErrorException;
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

import java.io.IOException;
import java.util.Objects;

/**
 * Tuition Microservice
 */
@Slf4j
@Service
public class TuitionService {

    private static final String AUTH_HEADER = "Authorization";
    private static final String FAILED_TO_GET_TUITION_LIST = "Failed to get tuition list";
    private final RestTemplate restTemplate;
    private final String getTuitionListUrl;

    public TuitionService(@Value("${tuition.baseUrl}") String baseUrl,
                          @Value("${tuition.tuitionListUrl}") String tuitionListUrl,
                          RestTemplate restTemplate) {
        this.getTuitionListUrl = baseUrl + tuitionListUrl;
        this.restTemplate = restTemplate;
    }

    /**
     * This method is used to get tuition list from tuition microservice
     *
     * @return tuition list response
     * @throws IOException
     */
    public TuitionListResponseDto getTuitionList(String token) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTH_HEADER, token);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        try {
            log.debug("Calling tuition service to get tuition list. url: {},", getTuitionListUrl);
            ResponseEntity<TuitionListResponseWrapper> result = restTemplate.exchange(getTuitionListUrl, HttpMethod.GET,
                    entity, TuitionListResponseWrapper.class);
            String responseBody = Objects.requireNonNull(result.getBody()).getData().toLogJson();
            log.debug("Getting tuition list was successful. statusCode: {}, response: {}",
                    result.getStatusCode(), responseBody);
            return Objects.requireNonNull(result.getBody()).getData();
        } catch (HttpClientErrorException e) {
            throw new TuitionServiceHttpClientErrorException(e.getStatusCode(), FAILED_TO_GET_TUITION_LIST,
                    e.getResponseBodyAsString(), e);
        }
    }
}
