package net.pladema.medicalconsultation.appointment.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;

import javax.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class UpdateAppointmentDateBo {

	private Integer appointmentId;

	private LocalTime time;

	private LocalDate date;

	private Integer openingHoursId;

	private EAppointmentModality modality;

	private String patientEmail;

	private Short recurringAppointmentTypeId;

}
