package com.swivel.ignite.reporting.dto.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class test {@link ReportRequestDto} class
 */
class ReportRequestDtoTest {

    private static final String TUITION_ID = "tid-123456789";
    private static final String MONTH = "JANUARY";

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
