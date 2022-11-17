package net.pladema.medicalconsultation.appointment.service.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDailyAmountBo {

    private Integer spontaneous;

    private Integer programmedAvailable;

    private Integer programmed;

	private Integer holiday;

    private LocalDate date;

}
