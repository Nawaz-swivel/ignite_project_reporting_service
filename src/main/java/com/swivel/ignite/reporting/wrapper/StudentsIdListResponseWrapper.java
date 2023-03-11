package com.swivel.ignite.reporting.wrapper;

import com.swivel.ignite.reporting.dto.BaseDto;
import com.swivel.ignite.reporting.dto.response.StudentsIdListResponseDto;
import com.swivel.ignite.reporting.enums.ResponseStatusType;
import lombok.Getter;
import lombok.Setter;

/**
 * Student id list response wrapper
 */
@Getter
@Setter
public class StudentsIdListResponseWrapper implements BaseDto {

    private ResponseStatusType status;
    private String message;
    private StudentsIdListResponseDto data;
    private String displayMessage;

    @Override
    public String toLogJson() {
        return toJson();
    }
}
