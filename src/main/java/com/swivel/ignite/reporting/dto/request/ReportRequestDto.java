package com.swivel.ignite.reporting.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for report request
 */
@Getter
@Setter
public class ReportRequestDto extends RequestDto {

    private String tuitionId;
    private String month;

    @Override
    public String toLogJson() {
        return toJson();
    }

    @Override
    public boolean isRequiredAvailable() {
        return isNonEmpty(tuitionId) && isNonEmpty(month);
    }
}
