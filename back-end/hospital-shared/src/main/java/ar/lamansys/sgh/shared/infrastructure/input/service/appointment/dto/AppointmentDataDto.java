package ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.institution.InstitutionInfoDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class AppointmentDataDto {

	private Integer appointmentId;

	private Short state;

	private InstitutionInfoDto institution;

	private DateDto date;

	private TimeDto hour;

	private String phonePrefix;

	private String phoneNumber;

	private String professionalFullName;

	private String patientEmail;

	private String authorFullName;

	private DateTimeDto createdOn;

}
