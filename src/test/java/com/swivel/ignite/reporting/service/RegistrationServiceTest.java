package com.swivel.ignite.reporting.service;

import com.swivel.ignite.reporting.dto.response.StudentResponseDto;
import com.swivel.ignite.reporting.dto.response.TuitionListResponseDto;
import com.swivel.ignite.reporting.dto.response.TuitionResponseDto;
import com.swivel.ignite.reporting.exception.RegistrationServiceHttpClientErrorException;
import com.swivel.ignite.reporting.wrapper.StudentResponseWrapper;
import com.swivel.ignite.reporting.wrapper.TuitionListResponseWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class tests {@link RegistrationService} class
 */
class RegistrationServiceTest {

    private static final String STUDENT_ID = "sid-123456789";
    private static final String TUITION_ID = "tid-123456789";
    private static final String BASE_URL = "http://localhost:8082/ignite-registration-service";
    private static final String TUITION_LIST_URL = "/api/v1/tuition/get/all";
    private static final String STUDENT_INFO_URL = "/api/v1/student/get/{studentId}";
    private RegistrationService registrationService;
    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        initMocks(this);
        registrationService = new RegistrationService(BASE_URL, TUITION_LIST_URL, STUDENT_INFO_URL, restTemplate);
    }

    /**
     * Start of tests for getTuitionList method
     */
    @Test
    void Should_ReturnTuitionListResponseDto_When_GettingTuitionListIsSuccessful() throws IOException {
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(getSampleTuitionListResponseEntity());
        assertEquals(TUITION_ID, registrationService.getTuitionList().getTuitionList().get(0).getTuitionId());
    }

    @Test
    void Should_ThrowRegistrationServiceHttpClientErrorException_When_GettingTuitionListIsFailed() {
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        RegistrationServiceHttpClientErrorException exception =
                assertThrows(RegistrationServiceHttpClientErrorException.class, () -> registrationService
                        .getTuitionList());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value() + " Failed to get tuition list",
                exception.getMessage());
    }

    /**
     * Start of tests for getStudentInfo method
     */
    @Test
    void Should_ReturnStudentResponseDto_When_GettingStudentInfoIsSuccessful() throws IOException {
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class),
                anyMap())).thenReturn(getSampleStudentResponseEntity());
        assertEquals(STUDENT_ID, registrationService.getStudentInfo(STUDENT_ID).getStudentId());
    }

    @Test
    void Should_ThrowRegistrationServiceHttpClientErrorException_When_GettingStudentInfoIsFailed() {
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class),
                anyMap())).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        RegistrationServiceHttpClientErrorException exception =
                assertThrows(RegistrationServiceHttpClientErrorException.class, () -> registrationService
                        .getStudentInfo(STUDENT_ID));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value() + " Failed to get student info",
                exception.getMessage());
    }

    /**
     * This method returns a sample ResponseEntity
     *
     * @return ResponseEntity
     */
    private ResponseEntity<StudentResponseWrapper> getSampleStudentResponseEntity() {
        return new ResponseEntity<>(getSampleStudentResponseWrapper(), HttpStatus.OK);
    }

    /**
     * This method returns a sample ResponseEntity
     *
     * @return ResponseEntity
     */
    private ResponseEntity<TuitionListResponseWrapper> getSampleTuitionListResponseEntity() {
        return new ResponseEntity<>(getSampleTuitionListResponseWrapper(), HttpStatus.OK);
    }

    /**
     * This method returns a sample StudentResponseWrapper
     *
     * @return StudentResponseWrapper
     */
    private StudentResponseWrapper getSampleStudentResponseWrapper() {
        StudentResponseWrapper responseWrapper = new StudentResponseWrapper();
        responseWrapper.setData(getSampleStudentResponseDto());
        return responseWrapper;
    }

    /**
     * This method returns a sample TuitionListResponseWrapper
     *
     * @return TuitionListResponseWrapper
     */
    private TuitionListResponseWrapper getSampleTuitionListResponseWrapper() {
        TuitionListResponseWrapper responseWrapper = new TuitionListResponseWrapper();
        responseWrapper.setData(getSampleTuitionListResponseDto());
        return responseWrapper;
    }

    /**
     * This method returns a sample StudentResponseDto
     *
     * @return StudentResponseDto
     */
    private StudentResponseDto getSampleStudentResponseDto() {
        StudentResponseDto responseDto = new StudentResponseDto();
        responseDto.setStudentId(STUDENT_ID);
        return responseDto;
    }

    /**
     * This method returns a sample TuitionListResponseDto
     *
     * @return TuitionListResponseDto
     */
    private TuitionListResponseDto getSampleTuitionListResponseDto() {
        TuitionListResponseDto responseDto = new TuitionListResponseDto();
        responseDto.getTuitionList().add(getSampleTuitionResponseDto());
        return responseDto;
    }

    /**
     * This method returns a sample TuitionResponseDto
     *
     * @return TuitionResponseDto
     */
    private TuitionResponseDto getSampleTuitionResponseDto() {
        TuitionResponseDto responseDto = new TuitionResponseDto();
        responseDto.setTuitionId(TUITION_ID);
        return responseDto;
    }
}
