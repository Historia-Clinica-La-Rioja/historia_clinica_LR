package ar.lamansys.sgh.publicapi.infrastructure.input.rest.appointment.dto;


import lombok.Getter;

@Getter
public class PublicAppointmentListDto {

    private final Integer id;

    private final PublicAppointmentPatientDto patient;

    private final String date;

    private final String hour;

	private final PublicAppointmentMedicalCoverage medicalCoverage;

    private final String status;


	public PublicAppointmentListDto(Integer id, PublicAppointmentPatientDto patient,
									String date, String hour,
									PublicAppointmentMedicalCoverage medicalCoverage,
									String status) {
		this.id = id;
		this.patient = patient;
		this.date = date;
		this.hour = hour;
		this.medicalCoverage = medicalCoverage;
		this.status = status;
	}
}
