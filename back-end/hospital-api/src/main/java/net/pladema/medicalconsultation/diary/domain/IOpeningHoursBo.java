package net.pladema.medicalconsultation.diary.domain;

import java.time.LocalTime;

public interface IOpeningHoursBo {
    Short getDayWeekId();

    LocalTime getFrom();

    LocalTime getTo();

    default boolean overlap(IOpeningHoursBo other) {
        return getDayWeekId().equals(other.getDayWeekId())
                && getFrom().isBefore(other.getTo())
                && getTo().isAfter(other.getFrom());
    }
}
