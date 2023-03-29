package com.swivel.ignite.reporting.enums;

import lombok.Getter;

/**
 * Enum values for Error Response
 */
@Getter
public enum ErrorResponseStatusType {

    MISSING_REQUIRED_FIELDS(4000, "Missing required fields"),
    REPORT_NOT_FOUND(4001, "Report not found"),
    INVALID_MONTH(4002, "Invalid Month"),
    INTERNAL_SERVER_ERROR(5000, "Internal Server Error"),
    STUDENT_INTERNAL_SERVER_ERROR(5001, "Student Service - Internal Server Error"),
    TUITION_INTERNAL_SERVER_ERROR(5002, "Tuition Service - Internal Server Error");

    private final int code;
    private final String message;

    ErrorResponseStatusType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
