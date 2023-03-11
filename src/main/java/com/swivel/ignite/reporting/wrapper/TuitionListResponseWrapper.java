package com.swivel.ignite.reporting.wrapper;

import com.swivel.ignite.reporting.dto.BaseDto;
import com.swivel.ignite.reporting.dto.response.TuitionListResponseDto;
import com.swivel.ignite.reporting.enums.ResponseStatusType;
import lombok.Getter;
import lombok.Setter;

/**
 * Tuition list response wrapper
 */
@Getter
@Setter
public class TuitionListResponseWrapper implements BaseDto {

    private ResponseStatusType status;
    private String message;
    private TuitionListResponseDto data;
    private String displayMessage;

    @Override
    public String toLogJson() {
        return toJson();
    }
}
