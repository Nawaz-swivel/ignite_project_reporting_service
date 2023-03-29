package com.swivel.ignite.reporting.service;

import com.swivel.ignite.reporting.dto.response.TuitionResponseDto;
import com.swivel.ignite.reporting.entity.Report;
import com.swivel.ignite.reporting.enums.Month;
import com.swivel.ignite.reporting.exception.ReportNotFoundException;
import com.swivel.ignite.reporting.exception.ReportingServiceException;
import com.swivel.ignite.reporting.repository.ReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;


/**
 * Report Service
 */
@Service
@Slf4j
public class ReportService {

    private static final String CRON = "* * * * *";

    private final ReportRepository reportRepository;
    private final StudentService studentService;
    private final TuitionService tuitionService;
    private final PaymentService paymentService;

    @Autowired
    public ReportService(ReportRepository reportRepository, StudentService studentService, TuitionService tuitionService,
                         PaymentService paymentService) {
        this.reportRepository = reportRepository;
        this.studentService = studentService;
        this.tuitionService = tuitionService;
        this.paymentService = paymentService;
    }


    /**
     * This method is used to update the report in DB, every minute
     *
     * @throws IOException
     */
    @Scheduled(cron = CRON)
    public void updateReport(String token) throws IOException {
        log.debug("Report data update service started..");
        try {
            reportRepository.deleteAll();
            List<TuitionResponseDto> tuitionList = tuitionService.getTuitionList(token).getTuitionList();
            for (TuitionResponseDto t : tuitionList) {
                String tuitionId = t.getTuitionId();
                List<Report> paidReportList = createPaidReportList(tuitionId);
                List<Report> unpaidReportList = createUnpaidReportList(tuitionId);

                for (String studentId : t.getStudentIds()) {
                    updatePaidReportList(tuitionId, studentId, paidReportList, token);
                    updateUnpaidReportList(tuitionId, studentId, unpaidReportList, token);
                }
            }
        } catch (DataAccessException e) {
            throw new ReportingServiceException("Failed to update report", e);
        }
        log.debug("Report data update service finished..");
    }

    /**
     * This method is used to get report by tuition id, month, isPaid
     *
     * @param tuitionId tuition id
     * @param month     month
     * @param isPaid    isPaid
     * @return Report
     */
    public Report getByTuitionIdMonthPaid(String tuitionId, String month, boolean isPaid) {
        try {
            Optional<Report> optionalReport = reportRepository.findByTuitionIdAndMonthAndIsPaid(tuitionId, month, isPaid);
            if (!optionalReport.isPresent())
                throw new ReportNotFoundException("Report not found for getting by tuitionId, month, isPaid");
            return optionalReport.get();
        } catch (DataAccessException e) {
            throw new ReportingServiceException("Failed to get report by tuitionId and month", e);
        }
    }

    /**
     * This method is used to create paid report list
     *
     * @param tuitionId tuition id
     * @return report list
     */
    private List<Report> createPaidReportList(String tuitionId) {
        try {
            List<Report> paidReportList = new ArrayList<>();
            for (Month m : Month.values()) {
                Report paidReport = new Report(tuitionId, m.getMonthString(), true);
                reportRepository.save(paidReport);
                paidReportList.add(paidReport);
            }
            return paidReportList;
        } catch (DataAccessException e) {
            throw new ReportingServiceException("Error creating paid report list", e);
        }
    }

    /**
     * This method is used to create unpaid report list
     *
     * @param tuitionId tuition id
     * @return report list
     */
    private List<Report> createUnpaidReportList(String tuitionId) {
        try {
            List<Report> unpaidReportList = new ArrayList<>();
            for (Month m : Month.values()) {
                Report unpaidReport = new Report(tuitionId, m.getMonthString(), false);
                reportRepository.save(unpaidReport);
                unpaidReportList.add(unpaidReport);
            }
            return unpaidReportList;
        } catch (DataAccessException e) {
            throw new ReportingServiceException("Error creating unpaid report list", e);
        }
    }

    /**
     * This method is used to get the student joined month to the tuition
     *
     * @param studentId student id
     * @return tuition joined month
     */
    private int studentTuitionJoinedMonth(String studentId, String token) {
        try {
            Date tuitionJoinedOn = studentService.getStudentInfo(studentId, token).getTuitionJoinedOn();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tuitionJoinedOn);
            return calendar.get(Calendar.MONTH);
        } catch (IOException e) {
            throw new ReportingServiceException("Failed to get tuition joined month of student for id: " +
                    studentId, e);
        }
    }

    /**
     * This method is used to update paid report list
     *
     * @param tuitionId      tuition id
     * @param studentId      student id
     * @param paidReportList paid report list
     */
    private void updatePaidReportList(String tuitionId, String studentId, List<Report> paidReportList, String token) {
        try {
            for (Report paidReport : paidReportList) {
                String month = paidReport.getMonth();
                List<String> paidStudentIdsForTheMonth = paymentService.getPaidStudents(tuitionId, month, token)
                        .getStudentIds();
                if (paidStudentIdsForTheMonth.contains(studentId)) {
                    paidReport.addStudentId(studentId);
                    reportRepository.save(paidReport);
                }
            }
        } catch (DataAccessException | IOException e) {
            throw new ReportingServiceException("Failed to update paid report list", e);
        }
    }

    /**
     * This method is used to update unpaid report list
     *
     * @param tuitionId        tuition id
     * @param studentId        student id
     * @param unpaidReportList unpaid report list
     */
    private void updateUnpaidReportList(String tuitionId, String studentId, List<Report> unpaidReportList, String token) {
        try {
            for (Report unpaidReport : unpaidReportList) {
                String month = unpaidReport.getMonth();
                List<String> paidStudentIdsForTheMonth = paymentService.getPaidStudents(tuitionId, month, token)
                        .getStudentIds();
                if (paidStudentIdsForTheMonth.contains(studentId)) {
                    unpaidReport.removeStudentId(studentId);
                    reportRepository.save(unpaidReport);
                }
                if (!paidStudentIdsForTheMonth.contains(studentId)
                        && studentTuitionJoinedMonth(studentId, token) <= Month.getMonthInt(month)) {
                    unpaidReport.addStudentId(studentId);
                    reportRepository.save(unpaidReport);
                }
            }
        } catch (DataAccessException | IOException e) {
            throw new ReportingServiceException("Failed to update unpaid report list", e);
        }
    }
}
