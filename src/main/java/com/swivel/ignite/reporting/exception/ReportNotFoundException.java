package com.swivel.ignite.reporting.exception;

/**
 * ReportNotFoundException
 */
public class ReportNotFoundException extends RuntimeException {

    /**
     * ReportNotFoundException with error message.
     *
     * @param errorMessage error message
     */
    public ReportNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * ReportNotFoundException with error message and throwable error
     *
     * @param errorMessage error message
     * @param error        error
     */
    public ReportNotFoundException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
