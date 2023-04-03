package com.swivel.ignite.reporting.dto.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class test {@link ReportRequestDto} class
 */
class ReportRequestDtoTest {

    private static final String TUITION_ID = "tid-123456789";
    private static final String MONTH = "JANUARY";

    /**
     * Start of tests for isRequiredAvailable method
     */
    @Test
    void Should_ReturnTrue_When_RequiredFieldsAreAvailable() {
        ReportRequestDto requestDto = getSampleReportRequestDto();
        assertTrue(requestDto.isRequiredAvailable());
    }

    @Test
    void Should_ReturnFalse_When_TuitionIdIsNull() {
        ReportRequestDto requestDto = getSampleReportRequestDto();
        requestDto.setTuitionId(null);
        assertFalse(requestDto.isRequiredAvailable());
    }

    @Test
    void Should_ReturnFalse_When_TuitionIdIsEmpty() {
        ReportRequestDto requestDto = getSampleReportRequestDto();
        requestDto.setTuitionId("");
        assertFalse(requestDto.isRequiredAvailable());
    }

    @Test
    void Should_ReturnFalse_When_MonthIdIsNull() {
        ReportRequestDto requestDto = getSampleReportRequestDto();
        requestDto.setMonth(null);
        assertFalse(requestDto.isRequiredAvailable());
    }

    @Test
    void Should_ReturnFalse_When_MonthIsEmpty() {
        ReportRequestDto requestDto = getSampleReportRequestDto();
        requestDto.setMonth("");
        assertFalse(requestDto.isRequiredAvailable());
    }

    /**
     * Start of tests for toLogJson method
     */
    @Test
    void Should_LogToJson_When_LoggingToJson() {
        ReportRequestDto requestDto = getSampleReportRequestDto();
        assertEquals("{\"tuitionId\":\"tid-123456789\",\"month\":\"JANUARY\",\"requiredAvailable\":true}",
                requestDto.toLogJson());
    }

    /**
     * This method returns a sample ReportRequestDto
     *
     * @return ReportRequestDto
     */
    private ReportRequestDto getSampleReportRequestDto() {
        ReportRequestDto requestDto = new ReportRequestDto();
        requestDto.setTuitionId(TUITION_ID);
        requestDto.setMonth(MONTH);
        return requestDto;
    }
}
