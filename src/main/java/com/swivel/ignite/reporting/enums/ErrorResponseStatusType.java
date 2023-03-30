package com.swivel.ignite.reporting.enums;

import lombok.Getter;

/**
 * Enum values for Error Response
 */
@Getter
public enum ErrorResponseStatusType {

    REPORT_NOT_FOUND(404, "Report not found"),
    INVALID_MONTH(400, "Invalid Month"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    STUDENT_INTERNAL_SERVER_ERROR(500, "Student Service - Internal Server Error"),
    TUITION_INTERNAL_SERVER_ERROR(500, "Tuition Service - Internal Server Error");

    private final int code;
    private final String message;

    ErrorResponseStatusType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
