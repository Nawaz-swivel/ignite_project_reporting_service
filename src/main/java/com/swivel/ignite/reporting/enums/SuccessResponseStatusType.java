package com.swivel.ignite.reporting.enums;

import lombok.Getter;

/**
 * Enum values for Success Response
 */
@Getter
public enum SuccessResponseStatusType {

    READ_REPORT(200, "Successfully read the report");

    private final int code;
    private final String message;

    SuccessResponseStatusType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
