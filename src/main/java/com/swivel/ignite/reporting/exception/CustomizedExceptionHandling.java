package com.swivel.ignite.reporting.exception;

import com.swivel.ignite.reporting.enums.ErrorResponseStatusType;
import com.swivel.ignite.reporting.enums.ResponseStatusType;
import com.swivel.ignite.reporting.wrapper.ErrorResponseWrapper;
import com.swivel.ignite.reporting.wrapper.ResponseWrapper;
import com.swivel.ignite.reporting.wrapper.RestErrorResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomizedExceptionHandling extends ResponseEntityExceptionHandler {

    private static final String ERROR_MESSAGE = "Oops!! Something went wrong. Please try again.";

    /**
     * This method returns an error response for reporting service exceptions
     *
     * @param exception exception
     * @return error response
     */
    @ExceptionHandler(ReportingServiceException.class)
    public ResponseEntity<ResponseWrapper> handleReportingServiceException(ReportingServiceException exception) {
        ResponseWrapper responseWrapper = new ErrorResponseWrapper(ResponseStatusType.ERROR, ErrorResponseStatusType
                .INTERNAL_SERVER_ERROR.getMessage(), null, ERROR_MESSAGE, ErrorResponseStatusType
                .INTERNAL_SERVER_ERROR.getCode());
        log.error(exception.getMessage());
        return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * This method returns an error response for report not found exceptions
     *
     * @param exception exception
     * @return error response
     */
    @ExceptionHandler(ReportNotFoundException.class)
    public ResponseEntity<ResponseWrapper> handleReportNotFoundException(ReportNotFoundException exception) {
        ResponseWrapper responseWrapper = new ErrorResponseWrapper(ResponseStatusType.ERROR, ErrorResponseStatusType
                .REPORT_NOT_FOUND.getMessage(), null, ERROR_MESSAGE, ErrorResponseStatusType
                .REPORT_NOT_FOUND.getCode());
        log.error(exception.getMessage());
        return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
    }

    /**
     * This method returns an error response for student microservice call exceptions
     *
     * @param exception exception
     * @return error response
     */
    @ExceptionHandler(StudentServiceHttpClientErrorException.class)
    public ResponseEntity<ResponseWrapper> handleStudentServiceHttpClientErrorException(
            StudentServiceHttpClientErrorException exception) {
        ResponseWrapper responseWrapper = new RestErrorResponseWrapper(ResponseStatusType.ERROR,
                ErrorResponseStatusType.STUDENT_INTERNAL_SERVER_ERROR.getMessage(), exception.responseBody,
                ERROR_MESSAGE, ErrorResponseStatusType.STUDENT_INTERNAL_SERVER_ERROR.getCode());
        log.error(exception.getMessage());
        return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * This method returns an error response for tuition microservice call exceptions
     *
     * @param exception exception
     * @return error response
     */
    @ExceptionHandler(TuitionServiceHttpClientErrorException.class)
    public ResponseEntity<ResponseWrapper> handleTuitionServiceHttpClientErrorException(
            TuitionServiceHttpClientErrorException exception) {
        ResponseWrapper responseWrapper = new RestErrorResponseWrapper(ResponseStatusType.ERROR,
                ErrorResponseStatusType.TUITION_INTERNAL_SERVER_ERROR.getMessage(), exception.responseBody,
                ERROR_MESSAGE, ErrorResponseStatusType.TUITION_INTERNAL_SERVER_ERROR.getCode());
        log.error(exception.getMessage());
        return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
