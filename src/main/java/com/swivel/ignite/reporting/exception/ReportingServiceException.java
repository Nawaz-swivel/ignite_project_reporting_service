package com.swivel.ignite.reporting.exception;

/**
 * Reporting Service Exception
 */
public class ReportingServiceException extends RuntimeException {

    /**
     * Reporting Service Exception with error message.
     *
     * @param errorMessage error message
     */
    public ReportingServiceException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Reporting Service Exception with error message and throwable error
     *
     * @param errorMessage error message
     * @param error        error
     */
    public ReportingServiceException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
