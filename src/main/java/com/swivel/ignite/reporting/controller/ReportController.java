package com.swivel.ignite.reporting.controller;

import com.swivel.ignite.reporting.dto.response.ReportResponseDto;
import com.swivel.ignite.reporting.entity.Report;
import com.swivel.ignite.reporting.enums.ErrorResponseStatusType;
import com.swivel.ignite.reporting.enums.Month;
import com.swivel.ignite.reporting.enums.SuccessResponseStatusType;
import com.swivel.ignite.reporting.service.ReportService;
import com.swivel.ignite.reporting.wrapper.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Report Controller
 */
@RestController
@RequestMapping("api/v1/report")
@Slf4j
public class ReportController extends Controller {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * This method is used to get report by tuition id and month
     *
     * @param tuitionId tuition id
     * @param month     month
     * @return success(report)/ error response
     */
    @GetMapping(path = "/get/{tuitionId}/{month}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper> getReportByTuitionIdMonth(@PathVariable(name = "tuitionId") String tuitionId,
                                                                     @PathVariable(name = "month") String month,
                                                                     HttpServletRequest request) throws IOException {
        String token = request.getHeader(AUTH_HEADER);
        if (!Month.isMonthValid(month)) {
            log.error("Month is invalid for getting a report by tuitionId and month");
            return getBadRequestResponse(ErrorResponseStatusType.INVALID_MONTH);
        }
        reportService.updateReport(token);
        Report paidReport = reportService.getByTuitionIdMonthPaid(tuitionId, month, true);
        Report unpaidReport = reportService.getByTuitionIdMonthPaid(tuitionId, month, false);
        ReportResponseDto responseDto = new ReportResponseDto(paidReport, unpaidReport);
        log.debug("Successfully returned report by tuition id: {}, month: {}", tuitionId, month);
        return getSuccessResponse(SuccessResponseStatusType.READ_REPORT, responseDto);
    }
}
