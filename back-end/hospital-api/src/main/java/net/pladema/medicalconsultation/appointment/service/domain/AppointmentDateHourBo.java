package net.pladema.medicalconsultation.appointment.service.domain;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@ToString
public class AppointmentDateHourBo {

    private Integer appointmentId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

}
