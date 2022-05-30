package ar.lamansys.online.infraestructure.input.rest.booking.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookingAppointmentDto {
    private Integer coverageId;
    private String day;
    private Integer diaryId;
    private String hour;
    private Integer openingHoursId;
    private String phoneNumber;
    private Integer snomedId;
    private Integer specialtyId;
}
