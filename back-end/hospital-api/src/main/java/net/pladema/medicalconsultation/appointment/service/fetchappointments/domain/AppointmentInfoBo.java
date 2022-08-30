package net.pladema.medicalconsultation.appointment.service.fetchappointments.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AppointmentInfoBo {

	private Integer id;

	private LocalDate dateTypeId;

	private LocalTime hour;

	private AppointmentStatusBo status;

	private boolean overturn;
	private String phoneNumber;
	private String phonePrefix;

	private PatientBo patient;

	private DoctorBo doctor;

	private MedicalCoverageBo medicalCoverage;

	private ClinicalSpecialtyBo clinicalSpecialty;

	private InstitutionBo institution;
	public String getPhone() {
		if ("Sin información".equals(phoneNumber))
			return phoneNumber;
		if (phonePrefix == null)
			return String.format("%s", phoneNumber);
		if (phoneNumber == null)
			return  String.format("%s",phonePrefix);
		return String.format("%s-%s",phonePrefix, phoneNumber);
	}
}
