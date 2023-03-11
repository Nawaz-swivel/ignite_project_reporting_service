package com.swivel.ignite.reporting.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Tuition DTO for response
 */
@Getter
@Setter
public class TuitionResponseDto extends ResponseDto {

    private final Set<String> studentIds = new HashSet<>();
    private String tuitionId;
    private String name;
    private String location;
}
