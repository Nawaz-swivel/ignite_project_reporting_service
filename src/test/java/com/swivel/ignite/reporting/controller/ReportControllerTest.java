package com.swivel.ignite.reporting.controller;

import com.swivel.ignite.reporting.entity.Report;
import com.swivel.ignite.reporting.enums.ErrorResponseStatusType;
import com.swivel.ignite.reporting.enums.Month;
import com.swivel.ignite.reporting.enums.SuccessResponseStatusType;
import com.swivel.ignite.reporting.exception.ReportNotFoundException;
import com.swivel.ignite.reporting.exception.ReportingServiceException;
import com.swivel.ignite.reporting.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * This class tests {@link ReportController} class
 */
class ReportControllerTest {

    private static final String TUITION_ID = "tid-123456789";
    private static final Month MONTH_JANUARY = Month.JANUARY;
    private static final String MONTH_INVALID = "INVALID_MONTH";
    private static final String SUCCESS_STATUS = "SUCCESS";
    private static final String ERROR_STATUS = "ERROR";
    private static final String SUCCESS_MESSAGE = "Successfully returned the data.";
    private static final String ERROR_MESSAGE = "Oops!! Something went wrong. Please try again.";
    private static final String ERROR = "ERROR";
    private static final String GET_REPORT_BY_TUITION_ID_MONTH_URI = "/api/v1/report/get/{tuitionId}/{month}";
    private MockMvc mockMvc;
    @Mock
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        initMocks(this);
        ReportController reportController = new ReportController(reportService);
        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
    }

    /**
     * Start of tests for get report by tuition id month
     * Api context: /api/v1/report/get/{tuitionId}/{month}
     */
    @Test
    void Should_ReturnOk_When_GettingReportByTuitionIdMonthIsSuccessful() throws Exception {
        doNothing().when(reportService).updateReport(anyString());
        when(reportService.getByTuitionIdMonthPaid(anyString(), anyString(), anyBoolean()))
                .thenReturn(getSampleReport());

        String uri = GET_REPORT_BY_TUITION_ID_MONTH_URI.replace("{tuitionId}", TUITION_ID)
                .replace("{month}", MONTH_JANUARY.getMonthString());
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status").value(SUCCESS_STATUS))
                .andExpect(jsonPath("$.message").value(SuccessResponseStatusType.READ_REPORT.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(SuccessResponseStatusType.READ_REPORT.getCode()))
                .andExpect(jsonPath("$.data.tuitionId").value(TUITION_ID))
                .andExpect(jsonPath("$.displayMessage").value(SUCCESS_MESSAGE));
    }

    @Test
    void Should_ReturnBadRequest_When_GettingReportByTuitionIdMonthForInvalidMonth() throws Exception {
        String uri = GET_REPORT_BY_TUITION_ID_MONTH_URI.replace("{tuitionId}", TUITION_ID)
                .replace("{month}", MONTH_INVALID);
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INVALID_MONTH.getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INVALID_MONTH.getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    @Test
    void Should_ReturnBadRequest_When_GettingReportByTuitionIdMonthForReportNotFound() throws Exception {
        doNothing().when(reportService).updateReport(anyString());
        when(reportService.getByTuitionIdMonthPaid(anyString(), anyString(), anyBoolean()))
                .thenThrow(new ReportNotFoundException(ERROR));

        String uri = GET_REPORT_BY_TUITION_ID_MONTH_URI.replace("{tuitionId}", TUITION_ID)
                .replace("{month}", MONTH_JANUARY.getMonthString());
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.REPORT_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.REPORT_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

//    @Test
//    void Should_ReturnStudentInternalServerError_When_GettingReportByTuitionIdMonthForFailedToGetStudentInfoFromStudentMicroservice()
//            throws Exception {
//        doThrow(new StudentServiceHttpClientErrorException(ERROR)).when(reportService).updateReport(anyString());
//
//        String uri = GET_REPORT_BY_TUITION_ID_MONTH_URI.replace("{tuitionId}", TUITION_ID)
//                .replace("{month}", MONTH_JANUARY.getMonthString());
//        mockMvc.perform(MockMvcRequestBuilders.get(uri)
//                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.STUDENT_INTERNAL_SERVER_ERROR
//                        .getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.STUDENT_INTERNAL_SERVER_ERROR
//                        .getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
//    }
//
//    @Test
//    void Should_ReturnTuitionInternalServerError_When_GettingReportByTuitionIdMonthForFailedToGetTuitionListFromTuitionMicroservice()
//            throws Exception {
//        doThrow(new TuitionServiceHttpClientErrorException(ERROR)).when(reportService).updateReport(anyString());
//
//        String uri = GET_REPORT_BY_TUITION_ID_MONTH_URI.replace("{tuitionId}", TUITION_ID)
//                .replace("{month}", MONTH_JANUARY.getMonthString());
//        mockMvc.perform(MockMvcRequestBuilders.get(uri)
//                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.TUITION_INTERNAL_SERVER_ERROR
//                        .getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.TUITION_INTERNAL_SERVER_ERROR
//                        .getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
//    }

    @Test
    void Should_ReturnInternalServerError_When_GettingReportByTuitionIdMonthIsFailed() throws Exception {
        doNothing().when(reportService).updateReport(anyString());
        when(reportService.getByTuitionIdMonthPaid(anyString(), anyString(), anyBoolean()))
                .thenThrow(new ReportingServiceException(ERROR));

        String uri = GET_REPORT_BY_TUITION_ID_MONTH_URI.replace("{tuitionId}", TUITION_ID)
                .replace("{month}", MONTH_JANUARY.getMonthString());
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
                        .getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
                        .getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    /**
     * This method returns a sample report
     *
     * @return Report
     */
    private Report getSampleReport() {
        Report report = new Report();
        report.setTuitionId(TUITION_ID);
        return report;
    }
}
