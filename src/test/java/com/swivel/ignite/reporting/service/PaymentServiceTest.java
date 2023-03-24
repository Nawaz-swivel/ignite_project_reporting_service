package com.swivel.ignite.reporting.service;

import com.swivel.ignite.reporting.dto.response.StudentsIdListResponseDto;
import com.swivel.ignite.reporting.enums.Month;
import com.swivel.ignite.reporting.exception.PaymentServiceHttpClientErrorException;
import com.swivel.ignite.reporting.wrapper.StudentsIdListResponseWrapper;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class tests {@link PaymentService} class
 */
class PaymentServiceTest {

    private static final String STUDENT_ID = "sid-123456789";
    private static final String TUITION_ID = "tid-123456789";
    private static final String BASE_URL = "http://localhost:8083/ignite-payment-service";
    private static final String PAID_STUDENTS_INFO_URL = "/api/v1/payment/get/all/{tuitionId}/{month}";
    private PaymentService paymentService;
    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        initMocks(this);
        paymentService = new PaymentService(BASE_URL, PAID_STUDENTS_INFO_URL, restTemplate);
    }

    /**
     * Start of tests for getPaidStudents method
     */
    @Test
    void Should_ReturnStudentsIdListResponseDto_When_GettingPaidStudentsIsSuccessful() throws IOException {
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class),
                anyMap())).thenReturn(getSampleStudentsIdListResponseEntity());
        assertEquals(STUDENT_ID, paymentService.getPaidStudents(TUITION_ID, Month.MAY.getMonthString()).getStudentIds()
                .get(0));
    }

    @Test
    void Should_ThrowPaymentServiceHttpClientErrorException_When_GettingPaidStudentsIsFailed() {
        String month = Month.MAY.getMonthString();

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class),
                anyMap())).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        PaymentServiceHttpClientErrorException exception = assertThrows(PaymentServiceHttpClientErrorException.class,
                () -> paymentService.getPaidStudents(TUITION_ID, month));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value() + " Failed to get paid students info",
                exception.getMessage());
    }

    /**
     * This method returns a sample StudentsIdListResponseEntity
     *
     * @return StudentsIdListResponseEntity
     */
    private ResponseEntity<StudentsIdListResponseWrapper> getSampleStudentsIdListResponseEntity() {
        return new ResponseEntity<>(getSampleStudentsIdListResponseWrapper(), HttpStatus.OK);
    }

    /**
     * This method returns a sample StudentsIdListResponseWrapper
     *
     * @return StudentsIdListResponseWrapper
     */
    private StudentsIdListResponseWrapper getSampleStudentsIdListResponseWrapper() {
        StudentsIdListResponseWrapper responseWrapper = new StudentsIdListResponseWrapper();
        responseWrapper.setData(getSampleStudentsIdListResponseDto());
        return responseWrapper;
    }

    /**
     * This method returns a sample StudentsIdListResponseDto
     *
     * @return StudentsIdListResponseDto
     */
    private StudentsIdListResponseDto getSampleStudentsIdListResponseDto() {
        List<String> studentIds = new ArrayList<>();
        studentIds.add(STUDENT_ID);

        StudentsIdListResponseDto responseDto = new StudentsIdListResponseDto();
        responseDto.setStudentIds(studentIds);
        return responseDto;
    }
}
