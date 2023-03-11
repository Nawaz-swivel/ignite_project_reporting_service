package com.swivel.ignite.reporting.service;

import com.swivel.ignite.reporting.dto.response.StudentsIdListResponseDto;
import com.swivel.ignite.reporting.exception.PaymentServiceHttpClientErrorException;
import com.swivel.ignite.reporting.wrapper.StudentsIdListResponseWrapper;
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
 * Payment Microservice
 */
@Slf4j
@Service
public class PaymentService {


    private static final String FAILED_TO_GET_PAID_STUDENTS_INFO = "Failed to get paid students info";
    private final RestTemplate restTemplate;
    private final String getPaidStudentsInfoUrl;

    public PaymentService(@Value("${payment.baseUrl}") String baseUrl,
                          @Value("${payment.paidStudentsInfoUrl}") String paidStudentsInfoUrl,
                          RestTemplate restTemplate) {
        this.getPaidStudentsInfoUrl = baseUrl + paidStudentsInfoUrl;
        this.restTemplate = restTemplate;
    }

    /**
     * This method is used to get paid students from payment microservice
     *
     * @param tuitionId tuition id
     * @param month     month
     * @return students id list
     * @throws IOException
     */
    public StudentsIdListResponseDto getPaidStudents(String tuitionId, String month) throws IOException {
        Map<String, String> uriParam = new HashMap<>();
        uriParam.put("tuitionId", tuitionId);
        uriParam.put("month", month);

        UriComponents builder = UriComponentsBuilder.fromHttpUrl(getPaidStudentsInfoUrl).build();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        try {
            log.debug("Calling payment service to get paid students info. url: {},", getPaidStudentsInfoUrl);
            ResponseEntity<StudentsIdListResponseWrapper> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
                    entity, StudentsIdListResponseWrapper.class, uriParam);
            String responseBody = Objects.requireNonNull(result.getBody()).getData().toLogJson();
            log.debug("Getting student info by student id was successful. statusCode: {}, response: {}",
                    result.getStatusCode(), responseBody);
            return Objects.requireNonNull(result.getBody()).getData();
        } catch (HttpClientErrorException e) {
            throw new PaymentServiceHttpClientErrorException(e.getStatusCode(), FAILED_TO_GET_PAID_STUDENTS_INFO,
                    e.getResponseBodyAsString(), e);
        }
    }
}
