package ar.lamansys.online.infraestructure.input.rest.booking.dto;

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
