package net.pladema.medicalconsultation.appointment.service.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentAssignedForPatientVo;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class AppointmentAssignedBo {

	private Integer id;

	private String professionalName;

	private String license;

	private Integer professionalId;

	private String clinicalSpecialtyName;

	private LocalDate date;

	private LocalTime hour;

	private String office;

	private boolean hasAssociatedReference;

	private Short associatedReferenceClosureTypeId;

	public AppointmentAssignedBo(AppointmentAssignedForPatientVo appointmentAssignedForPatientVo) {

		this.id = appointmentAssignedForPatientVo.getId();

		this.license = appointmentAssignedForPatientVo.getLicense();

		this.professionalId = appointmentAssignedForPatientVo.getPersonId();

		this.date = appointmentAssignedForPatientVo.getDate();

		this.hour = appointmentAssignedForPatientVo.getHour();

		this.office = appointmentAssignedForPatientVo.getOffice();

		this.clinicalSpecialtyName = appointmentAssignedForPatientVo.getClinicalSpecialtyName();
	}

	public void setRespectiveProfessionalName(String firstName, String middleNames, String lastName, String otherLastNames, String nameSelfDetermination, boolean includeNameSelfDetermination) {
		String fullName = lastName;
		if(!(otherLastNames == null || otherLastNames.isBlank()))
			fullName += " " + otherLastNames;
		if(includeNameSelfDetermination && !(nameSelfDetermination == null || nameSelfDetermination.isBlank())) {
			fullName = nameSelfDetermination + " " + fullName;
		} else {
			if(!(middleNames == null || middleNames.isBlank()))
				fullName = middleNames + " " + fullName;
			fullName = firstName + " " + fullName;
		}
		this.professionalName = fullName;
	}
}
