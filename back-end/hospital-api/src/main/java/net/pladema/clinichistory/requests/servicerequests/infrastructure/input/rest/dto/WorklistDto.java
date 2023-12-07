package net.pladema.clinichistory.requests.servicerequests.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.pladema.establishment.controller.dto.InstitutionBasicInfoDto;
import net.pladema.establishment.service.domain.InstitutionBasicInfoBo;

@Getter
@Setter
@RequiredArgsConstructor
public class WorklistDto {

	private Integer patientId;

	private Short patientIdentificationTypeId;

	private String patientIdentificationNumber;

	private String patientFullName;

	private Short statusId;

	private DateTimeDto actionTime;

	private Integer appointmentId;

	private InstitutionBasicInfoDto completionInstitution;

	public WorklistDto(Integer patientId, Short patientIdentificationTypeId, String patientIdentificationNumber, String patientFullName, Short statusId, Integer appointmentId, DateTimeDto actionTime,
					   InstitutionBasicInfoBo completionInstitution) {
		this.patientId = patientId;
		this.patientIdentificationTypeId = patientIdentificationTypeId;
		this.patientIdentificationNumber = patientIdentificationNumber;
		this.patientFullName = patientFullName;
		this.statusId = statusId;
		this.appointmentId = appointmentId;
		this.actionTime = actionTime;
		this.completionInstitution = new InstitutionBasicInfoDto(completionInstitution.getId(), completionInstitution.getName());
	}

}
