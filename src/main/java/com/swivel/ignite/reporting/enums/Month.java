package com.swivel.ignite.reporting.enums;

import com.swivel.ignite.reporting.exception.ReportingServiceException;
import lombok.Getter;

/**
 * Month Enum
 */
@Getter
public enum Month {
    JANUARY(0, "JANUARY"),
    FEBRUARY(1, "FEBRUARY"),
    MARCH(2, "MARCH"),
    APRIL(3, "APRIL"),
    MAY(4, "MAY"),
    JUNE(5, "JUNE"),
    JULY(6, "JULY"),
    AUGUST(7, "AUGUST"),
    SEPTEMBER(8, "SEPTEMBER"),
    OCTOBER(9, "OCTOBER"),
    NOVEMBER(10, "NOVEMBER"),
    DECEMBER(11, "DECEMBER");

    private final int monthInt;
    private final String monthString;

    Month(int monthInt, String monthString) {
        this.monthInt = monthInt;
        this.monthString = monthString;
    }

    /**
     * This method is used to get month in int from string
     *
     * @param monthString month in string
     * @return month in int
     */
    public static int getMonthInt(String monthString) {
        for (Month m : Month.values()) {
            if (m.getMonthString().equals(monthString))
                return m.getMonthInt();
        }
        throw new ReportingServiceException("Invalid month string");
    }

    /**
     * This method is used to check if a month is valid from its given string value
     *
     * @param monthString month in string
     * @return true/false
     */
    public static boolean isMonthValid(String monthString) {
        for (Month m : Month.values()) {
            if (m.getMonthString().equals(monthString))
                return true;
        }
        return false;
    }
}
