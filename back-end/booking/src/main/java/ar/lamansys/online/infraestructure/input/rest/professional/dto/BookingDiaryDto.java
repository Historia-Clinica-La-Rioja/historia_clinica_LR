package ar.lamansys.online.infraestructure.input.rest.professional.dto;

import ar.lamansys.online.domain.professional.DiaryListBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
@ToString
public class BookingDiaryDto {
    private final Integer id;
    private final Integer doctorsOfficeId;
    private final String doctorsOfficeDescription;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Short appointmentDuration;
    private final LocalTime from;
    private final LocalTime to;
    private final Integer openingHoursId;


    public BookingDiaryDto(DiaryListBo diary) {
        this.id = diary.getId();
        this.doctorsOfficeId = diary.getDoctorsOfficeId();
        this.doctorsOfficeDescription = diary.getDoctorsOfficeDescription();
        this.startDate = diary.getStartDate();
        this.endDate = diary.getEndDate();
        this.appointmentDuration = diary.getAppointmentDuration();
        this.from = diary.getFrom();
        this.to = diary.getTo();
        this.openingHoursId = diary.getOpeningHoursId();
    }
}
