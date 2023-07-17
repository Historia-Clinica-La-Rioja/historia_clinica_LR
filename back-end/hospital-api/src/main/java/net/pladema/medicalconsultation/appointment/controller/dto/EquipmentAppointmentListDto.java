package net.pladema.medicalconsultation.appointment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import net.pladema.establishment.controller.dto.InstitutionBasicInfoDto;

@Builder
@ToString
@Getter
@AllArgsConstructor
public class EquipmentAppointmentListDto {

	private final Integer id;

	private final AppointmentBasicPatientDto patient;

	private final String date;

	private final String hour;

	private final boolean overturn;

	private final Integer healthInsuranceId;

	private final String medicalCoverageName;

	private final String medicalCoverageAffiliateNumber;

	private final Short appointmentStateId;

	private final boolean isProtected;

	private final InstitutionBasicInfoDto derivedTo;

	private final Short reportStatusId;

}
