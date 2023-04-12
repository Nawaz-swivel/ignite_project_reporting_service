package com.swivel.ignite.reporting.exception;

import com.swivel.ignite.reporting.enums.ErrorResponseStatusType;
import com.swivel.ignite.reporting.enums.ResponseStatusType;
import com.swivel.ignite.reporting.wrapper.ErrorResponseWrapper;
import com.swivel.ignite.reporting.wrapper.ResponseWrapper;
import com.swivel.ignite.reporting.wrapper.RestErrorResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomizedExceptionHandling extends ResponseEntityExceptionHandler {

    private static final String ERROR_MESSAGE = "Oops!! Something went wrong. Please try again.";

    @ExceptionHandler(ReportingServiceException.class)
    public ResponseEntity<ResponseWrapper> handleReportingServiceException(ReportingServiceException exception,
                                                                           WebRequest request) {
        ResponseWrapper responseWrapper = new ErrorResponseWrapper(ResponseStatusType.ERROR, ErrorResponseStatusType
                .INTERNAL_SERVER_ERROR.getMessage(), null, ERROR_MESSAGE, ErrorResponseStatusType
                .INTERNAL_SERVER_ERROR.getCode());
        return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ReportNotFoundException.class)
    public ResponseEntity<ResponseWrapper> handleReportNotFoundException(ReportNotFoundException exception,
                                                                         WebRequest request) {
        ResponseWrapper responseWrapper = new ErrorResponseWrapper(ResponseStatusType.ERROR, ErrorResponseStatusType
                .REPORT_NOT_FOUND.getMessage(), null, ERROR_MESSAGE, ErrorResponseStatusType
                .REPORT_NOT_FOUND.getCode());
        return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StudentServiceHttpClientErrorException.class)
    public ResponseEntity<ResponseWrapper> handleStudentServiceHttpClientErrorException(
            StudentServiceHttpClientErrorException exception, WebRequest request) {
        ResponseWrapper responseWrapper = new RestErrorResponseWrapper(ResponseStatusType.ERROR,
                ErrorResponseStatusType.STUDENT_INTERNAL_SERVER_ERROR.getMessage(), exception.responseBody,
                ERROR_MESSAGE, ErrorResponseStatusType.STUDENT_INTERNAL_SERVER_ERROR.getCode());
        return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TuitionServiceHttpClientErrorException.class)
    public ResponseEntity<ResponseWrapper> handleTuitionServiceHttpClientErrorException(
            TuitionServiceHttpClientErrorException exception, WebRequest request) {
        ResponseWrapper responseWrapper = new RestErrorResponseWrapper(ResponseStatusType.ERROR,
                ErrorResponseStatusType.TUITION_INTERNAL_SERVER_ERROR.getMessage(), exception.responseBody,
                ERROR_MESSAGE, ErrorResponseStatusType.TUITION_INTERNAL_SERVER_ERROR.getCode());
        return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
