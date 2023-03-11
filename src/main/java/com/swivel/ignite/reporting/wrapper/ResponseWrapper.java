package com.swivel.ignite.reporting.wrapper;

import com.swivel.ignite.reporting.dto.BaseDto;
import com.swivel.ignite.reporting.dto.response.ResponseDto;
import com.swivel.ignite.reporting.enums.ResponseStatusType;
import lombok.Getter;

/**
 * Response wrapper
 */
@Getter
public class ResponseWrapper implements BaseDto {
    private final ResponseStatusType status;
    private final String message;
    private final ResponseDto data;
    private final String displayMessage;

    /**
     * @param status         status
     * @param message        dev message
     * @param data           data
     * @param displayMessage display message
     */
    public ResponseWrapper(ResponseStatusType status, String message, ResponseDto data, String displayMessage) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.displayMessage = displayMessage;
    }

    @Override
    public String toLogJson() {
        return toJson();
    }
}
