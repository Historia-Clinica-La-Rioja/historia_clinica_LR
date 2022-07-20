package ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto;


import lombok.Getter;

@Getter
public class PublicAppointmentListDto {

    private final Integer id;
	private final String date;
	private final String hour;
	private final boolean overturn;
	private final String phone;
    private final PublicAppointmentPatientDto patient;

	private final PublicAppointmentDoctorDto doctor;
	private final PublicAppointmentStatus status;
	private final PublicAppointmentMedicalCoverage medicalCoverage;
	private final PublicAppointmentInstitution institution;
	private final PublicAppointmentClinicalSpecialty clinicalSpecialty;

	public PublicAppointmentListDto(Integer id,
									String date, String hour,
									boolean overturn, String phone,
									PublicAppointmentPatientDto patient,
									PublicAppointmentDoctorDto doctor, PublicAppointmentStatus status,
									PublicAppointmentMedicalCoverage medicalCoverage,
									PublicAppointmentInstitution institution,
									PublicAppointmentClinicalSpecialty clinicalSpecialty) {
		this.id = id;
		this.date = date;
		this.hour = hour;
		this.overturn = overturn;
		this.phone = phone;
		this.patient = patient;
		this.doctor = doctor;
		this.status = status;
		this.medicalCoverage = medicalCoverage;
		this.institution = institution;
		this.clinicalSpecialty = clinicalSpecialty;
	}
}
