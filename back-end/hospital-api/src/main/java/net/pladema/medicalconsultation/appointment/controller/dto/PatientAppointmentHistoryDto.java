package net.pladema.medicalconsultation.appointment.controller.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PatientAppointmentHistoryDto {

	private DateTimeDto dateTime;

	private String institution;

	private String city;

	private String doctorName;

	private String clinicalSpecialty;

	private List<SnomedDto> practices;

	private String service;

	private Short statusId;

}
