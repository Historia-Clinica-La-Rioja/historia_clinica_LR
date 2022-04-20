package net.pladema.medicalconsultation.appointment.service.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentAssignedForPatientVo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class AppointmentAssignedBo {

	private String professionalName;

	private String license;

	private Integer professionalId;

	private List<String> specialties;

	private LocalDate date;

	private LocalTime hour;

	private String office;

	public AppointmentAssignedBo(AppointmentAssignedForPatientVo appointmentAssignedForPatientVo) {

		this.license = appointmentAssignedForPatientVo.getLicense();

		this.professionalId = appointmentAssignedForPatientVo.getPersonId();

		this.date = appointmentAssignedForPatientVo.getDate();

		this.hour = appointmentAssignedForPatientVo.getHour();

		this.office = appointmentAssignedForPatientVo.getOffice();
	}
}
