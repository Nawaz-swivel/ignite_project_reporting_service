package com.swivel.ignite.reporting.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

/**
 * Registration Microservice client error exception
 */
public class RegistrationServiceHttpClientErrorException extends HttpClientErrorException {

    public final transient JsonNode responseBody;
    public final Exception exception;

    public RegistrationServiceHttpClientErrorException(HttpStatus statusCode, String statusText, String responseBody,
                                                       Exception e) throws IOException {
        super(statusCode, statusText);
        this.responseBody = getJsonObject(responseBody);
        this.exception = e;
    }

    private JsonNode getJsonObject(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(jsonString);
    }
}
