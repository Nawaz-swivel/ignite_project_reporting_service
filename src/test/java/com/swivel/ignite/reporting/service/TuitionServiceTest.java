//package com.swivel.ignite.reporting.service;
//
//import com.swivel.ignite.reporting.dto.response.TuitionListResponseDto;
//import com.swivel.ignite.reporting.dto.response.TuitionResponseDto;
//import com.swivel.ignite.reporting.exception.TuitionServiceHttpClientErrorException;
//import com.swivel.ignite.reporting.wrapper.TuitionListResponseWrapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//import static org.mockito.MockitoAnnotations.initMocks;
//
///**
// * This class tests {@link TuitionService} class
// */
//class TuitionServiceTest {
//
//    private static final String TUITION_ID = "tid-123456789";
//    private static final String BASE_URL = "http://localhost:8080/ignite-tuition-service";
//    private static final String TUITION_LIST_URL = "/api/v1/tuition/get/all";
//    private TuitionService tuitionService;
//    @Mock
//    private RestTemplate restTemplate;
//
//    @BeforeEach
//    void setUp() {
//        initMocks(this);
//        tuitionService = new TuitionService(BASE_URL, TUITION_LIST_URL, restTemplate);
//    }
//
//    /**
//     * Start of tests for getTuitionList method
//     */
//    @Test
//    void Should_ReturnTuitionListResponseDto_When_GettingTuitionListIsSuccessful() throws IOException {
//        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
//                .thenReturn(getSampleResponseEntity());
//        assertEquals(TUITION_ID, tuitionService.getTuitionList().getTuitionList().get(0).getTuitionId());
//    }
//
//    @Test
//    void Should_ThrowTuitionServiceHttpClientErrorException_When_GettingTuitionListIsFailed() {
//        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
//                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
//        TuitionServiceHttpClientErrorException exception =
//                assertThrows(TuitionServiceHttpClientErrorException.class, () -> tuitionService
//                        .getTuitionList());
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value() + " Failed to get tuition list",
//                exception.getMessage());
//    }
//
//    /**
//     * This method returns a sample ResponseEntity
//     *
//     * @return ResponseEntity
//     */
//    private ResponseEntity<TuitionListResponseWrapper> getSampleResponseEntity() {
//        return new ResponseEntity<>(getSampleTuitionListResponseWrapper(), HttpStatus.OK);
//    }
//
//    /**
//     * This method returns a sample TuitionListResponseWrapper
//     *
//     * @return TuitionListResponseWrapper
//     */
//    private TuitionListResponseWrapper getSampleTuitionListResponseWrapper() {
//        TuitionListResponseWrapper responseWrapper = new TuitionListResponseWrapper();
//        responseWrapper.setData(getSampleTuitionListResponseDto());
//        return responseWrapper;
//    }
//
//    /**
//     * This method returns a sample TuitionListResponseDto
//     *
//     * @return TuitionListResponseDto
//     */
//    private TuitionListResponseDto getSampleTuitionListResponseDto() {
//        TuitionListResponseDto responseDto = new TuitionListResponseDto();
//        responseDto.getTuitionList().add(getSampleTuitionResponseDto());
//        return responseDto;
//    }
//
//    /**
//     * This method returns a sample TuitionResponseDto
//     *
//     * @return TuitionResponseDto
//     */
//    private TuitionResponseDto getSampleTuitionResponseDto() {
//        TuitionResponseDto responseDto = new TuitionResponseDto();
//        responseDto.setTuitionId(TUITION_ID);
//        return responseDto;
//    }
//}
