package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class StudyOrderWorkListDto {

	private Integer studyId;

	private StudyOrderBasicPatientDto patientDto;

	private SnomedDto snomed;

	private Short studyTypeId;

	private boolean requiresTransfer;

	private Short sourceTypeId;

	@Nullable
	private DateTimeDto deferredDate;

	private Integer status;

}
