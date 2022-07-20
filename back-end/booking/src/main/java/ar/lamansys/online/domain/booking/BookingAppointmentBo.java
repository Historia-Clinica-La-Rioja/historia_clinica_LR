package ar.lamansys.online.domain.booking;

import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingProfessionalDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BookingAppointmentBo {
    private final Integer diaryId;
    private final String day;
    private final String hour;
    private final Integer openingHoursId;
	private final String phonePrefix;
    private final String phoneNumber;
    private final Integer coverageId;
    private final Integer snomedId;
    private final Integer specialtyId;
	private final BookingProfessionalDto professional;
}
