package com.swivel.ignite.reporting.dto.response;

import com.swivel.ignite.reporting.dto.BaseDto;

/**
 * ResponseDto - All responseDto classes are needed to extend this class.
 */
public class ResponseDto implements BaseDto {

    @Override
    public String toLogJson() {
        return toJson();
    }
}
