package ar.lamansys.sgh.shared.infrastructure.input.service.booking;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private String appointmentDataEmail;
    private BookingAppointmentDto bookingAppointmentDto;
    private BookingPersonDto bookingPersonDto;
}
