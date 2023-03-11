package com.swivel.ignite.reporting.repository;

import com.swivel.ignite.reporting.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Report Repository
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, String> {

    /**
     * This method finds a report by tuition id, month, and isPaid
     *
     * @param tuitionId tuition id
     * @param month     month
     * @param isPaid    is paid
     * @return Report/ null
     */
    Optional<Report> findByTuitionIdAndMonthAndIsPaid(String tuitionId, String month, boolean isPaid);
}
