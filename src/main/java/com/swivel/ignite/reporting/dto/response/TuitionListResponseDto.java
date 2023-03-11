package com.swivel.ignite.reporting.dto.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Tuition List DTO for response
 */
@Getter
public class TuitionListResponseDto extends ResponseDto {

    private final List<TuitionResponseDto> tuitionList = new ArrayList<>();
}
