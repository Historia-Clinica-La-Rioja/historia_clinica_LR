package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ESourceType;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.requests.service.domain.EDiagnosticReportStatus;
import net.pladema.clinichistory.requests.servicerequests.domain.enums.EStudyType;

import javax.annotation.Nullable;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class StudyOrderWorkListDto {

	private Integer studyId;

	private StudyOrderBasicPatientDto patientDto;

	private List<SnomedDto> snomed;

	private EStudyType studyTypeId;

	private Boolean requiresTransfer;

	private ESourceType sourceTypeId;

	@Nullable
	private DateTimeDto deferredDate;

	private EDiagnosticReportStatus status;

	private DateTimeDto createdDate;

	private StudyOrderPatientLocationDto patientLocation;

	private String emergencyCareReason;
}
