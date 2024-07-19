package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEDocumentDataDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StudyWithoutOrderReportInfoDto {

	@NotNull(message = "value.mandatory")
	private String snomed;

	@Nullable
	private String imageId;

	@Nullable
	private HCEDocumentDataDto hceDocumentDataDto;

	@NotNull(message = "value.mandatory")
	private Boolean status;

	@NotNull(message = "value.mandatory")
	private Boolean isAvailableInPACS;

	@NotNull(message = "value.mandatory")
	private Boolean viewReport;
	@Nullable
	private DateDto appointmentDate;
	@Nullable
	private TimeDto appointmentHour;
}
