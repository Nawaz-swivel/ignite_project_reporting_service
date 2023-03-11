package com.swivel.ignite.reporting.service;

import com.swivel.ignite.reporting.dto.response.StudentsIdListResponseDto;
import com.swivel.ignite.reporting.dto.response.TuitionListResponseDto;
import com.swivel.ignite.reporting.dto.response.TuitionResponseDto;
import com.swivel.ignite.reporting.entity.Report;
import com.swivel.ignite.reporting.enums.Month;
import com.swivel.ignite.reporting.exception.ReportNotFoundException;
import com.swivel.ignite.reporting.exception.ReportingServiceException;
import com.swivel.ignite.reporting.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class tests {@link ReportService} class
 */
class ReportServiceTest {

    private static final String STUDENT_ID = "sid-123456789";
    private static final String TUITION_ID = "tid-123456789";
    private static final String REPORT_ID = "rid-123456789";
    private static final String ERROR = "ERROR";
    private ReportService reportService;
    @Mock
    private ReportRepository reportRepository;
    @Mock
    private RegistrationService registrationService;
    @Mock
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        initMocks(this);
        reportService = new ReportService(reportRepository, registrationService, paymentService);
    }

    /**
     * Start of tests for updateReport method
     */
    @Test
    void Should_UpdateReport() throws IOException {
        doNothing().when(reportRepository).deleteAll();
        when(registrationService.getTuitionList()).thenReturn(getSampleTuitionListResponseDto());
        when(paymentService.getPaidStudents(anyString(), anyString())).thenReturn(getSampleStudentsIdListResponseDto());
        reportService.updateReport();
        verify(reportRepository, times(48)).save(any(Report.class));
    }

    @Test
    void Should_ThrowReportingServiceException_When_UpdatingReportForErrorCreatingPaidReportList() throws IOException {
        doNothing().when(reportRepository).deleteAll();
        when(registrationService.getTuitionList()).thenReturn(getSampleTuitionListResponseDto());
        doThrow(new DataAccessException(ERROR) {
        }).when(reportRepository).save(any(Report.class));
        ReportingServiceException exception = assertThrows(ReportingServiceException.class, () ->
                reportService.updateReport());
        assertEquals("Error creating paid report list", exception.getMessage());
    }

    @Test
    void Should_ThrowReportingServiceException_When_UpdatingReportForErrorCreatingUnPaidReportList() throws IOException {
        Report r = new Report();
        doNothing().when(reportRepository).deleteAll();
        when(registrationService.getTuitionList()).thenReturn(getSampleTuitionListResponseDto());
        when(reportRepository.save(any(Report.class))).thenReturn(r, r, r, r, r, r, r, r, r, r, r, r)
                .thenThrow(new DataAccessException(ERROR) {
                });
        ReportingServiceException exception = assertThrows(ReportingServiceException.class, () ->
                reportService.updateReport());
        assertEquals("Error creating unpaid report list", exception.getMessage());
    }

    @Test
    void Should_ThrowReportingServiceException_When_UpdatingReportForFailedToUpdatePaidReportList() throws IOException {
        Report r = new Report();
        doNothing().when(reportRepository).deleteAll();
        when(registrationService.getTuitionList()).thenReturn(getSampleTuitionListResponseDto());
        when(paymentService.getPaidStudents(anyString(), anyString())).thenReturn(getSampleStudentsIdListResponseDto());
        when(reportRepository.save(any(Report.class))).thenReturn(r, r, r, r, r, r, r, r, r, r, r, r, r, r, r, r, r, r,
                r, r, r, r, r, r).thenThrow(new DataAccessException(ERROR) {
        });
        ReportingServiceException exception = assertThrows(ReportingServiceException.class, () ->
                reportService.updateReport());
        assertEquals("Failed to update paid report list", exception.getMessage());
    }

    @Test
    void Should_ThrowReportingServiceException_When_UpdatingReportForFailedToUpdateUnPaidReportList() throws IOException {
        Report r = new Report();
        doNothing().when(reportRepository).deleteAll();
        when(registrationService.getTuitionList()).thenReturn(getSampleTuitionListResponseDto());
        when(paymentService.getPaidStudents(anyString(), anyString())).thenReturn(getSampleStudentsIdListResponseDto());
        when(reportRepository.save(any(Report.class))).thenReturn(r, r, r, r, r, r, r, r, r, r, r, r, r, r, r, r, r, r,
                r, r, r, r, r, r, r, r, r, r, r, r, r, r, r, r, r, r).thenThrow(new DataAccessException(ERROR) {
        });
        ReportingServiceException exception = assertThrows(ReportingServiceException.class, () ->
                reportService.updateReport());
        assertEquals("Failed to update unpaid report list", exception.getMessage());
    }

    @Test
    void Should_ThrowReportingServiceException_When_UpdatingReportIsFailed() {
        doThrow(new DataAccessException(ERROR) {
        }).when(reportRepository).deleteAll();
        ReportingServiceException exception = assertThrows(ReportingServiceException.class, () ->
                reportService.updateReport());
        assertEquals("Failed to update report", exception.getMessage());
    }

    /**
     * Start of tests for getByTuitionIdMonthPaid method
     */
    @Test
    void Should_ReturnReport_When_GettingByTuitionIdMonthPaidIsSuccessful() {
        when(reportRepository.findByTuitionIdAndMonthAndIsPaid(anyString(), anyString(), anyBoolean()))
                .thenReturn(Optional.of(getSampleReport()));
        assertEquals(REPORT_ID, reportService.getByTuitionIdMonthPaid(TUITION_ID, Month.MARCH.getMonthString(), true)
                .getId());
    }

    @Test
    void Should_ThrowReportNotFoundException_When_GettingByTuitionIdMonthPaidForReportNotFound() {
        String month = Month.MAY.getMonthString();
        when(reportRepository.findByTuitionIdAndMonthAndIsPaid(anyString(), anyString(), anyBoolean()))
                .thenReturn(Optional.empty());
        ReportNotFoundException exception = assertThrows(ReportNotFoundException.class, () ->
                reportService.getByTuitionIdMonthPaid(TUITION_ID, month, true));
        assertEquals("Report not found for getting by tuitionId, month, isPaid", exception.getMessage());
    }

    @Test
    void Should_ThrowReportingServiceException_When_GettingByTuitionIdMonthPaidIsFailed() {
        String month = Month.MAY.getMonthString();
        when(reportRepository.findByTuitionIdAndMonthAndIsPaid(anyString(), anyString(), anyBoolean()))
                .thenThrow(new DataAccessException(ERROR) {
                });
        ReportingServiceException exception = assertThrows(ReportingServiceException.class, () ->
                reportService.getByTuitionIdMonthPaid(TUITION_ID, month, true));
        assertEquals("Failed to get report by tuitionId and month", exception.getMessage());
    }

    /**
     * This class returns a sample TuitionListResponseDto
     *
     * @return TuitionListResponseDto
     */
    private TuitionListResponseDto getSampleTuitionListResponseDto() {
        TuitionListResponseDto dto = new TuitionListResponseDto();
        dto.getTuitionList().add(getSampleTuitionResponseDto());
        return dto;
    }

    /**
     * This class returns a sample TuitionResponseDto
     *
     * @return TuitionResponseDto
     */
    private TuitionResponseDto getSampleTuitionResponseDto() {
        TuitionResponseDto dto = new TuitionResponseDto();
        dto.setTuitionId(TUITION_ID);
        dto.getStudentIds().add(STUDENT_ID);
        return dto;
    }

    /**
     * This class returns a sample StudentsIdListResponseDto
     *
     * @return StudentsIdListResponseDto
     */
    private StudentsIdListResponseDto getSampleStudentsIdListResponseDto() {
        List<String> studentIds = new ArrayList<>();
        studentIds.add(STUDENT_ID);
        return new StudentsIdListResponseDto(studentIds);
    }

    /**
     * This method returns a sample Report
     *
     * @return Report
     */
    private Report getSampleReport() {
        Report report = new Report();
        report.setId(REPORT_ID);
        return report;
    }
}
