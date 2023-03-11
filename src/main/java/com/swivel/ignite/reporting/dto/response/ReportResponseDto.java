package com.swivel.ignite.reporting.dto.response;

import com.swivel.ignite.reporting.entity.Report;
import lombok.Getter;

/**
 * Report DTO for response
 */
@Getter
public class ReportResponseDto extends ResponseDto {

    private final String tuitionId;
    private final String month;
    private final Report paidReport;
    private final Report unpaidReport;

    public ReportResponseDto(Report paidReport, Report unpaidReport) {
        this.tuitionId = paidReport.getTuitionId();
        this.month = paidReport.getMonth();
        this.paidReport = paidReport;
        this.unpaidReport = unpaidReport;
    }
}
