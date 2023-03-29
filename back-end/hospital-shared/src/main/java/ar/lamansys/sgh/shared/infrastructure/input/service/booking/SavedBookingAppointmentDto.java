package ar.lamansys.sgh.shared.infrastructure.input.service.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class SavedBookingAppointmentDto {
	private final Integer bookingPersonId;
	private final Integer appointmentId;
	private final String uuid;
}
