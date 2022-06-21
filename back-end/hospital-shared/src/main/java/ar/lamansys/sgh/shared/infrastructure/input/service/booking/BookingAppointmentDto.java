package ar.lamansys.sgh.shared.infrastructure.input.service.booking;

import lombok.*;

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
