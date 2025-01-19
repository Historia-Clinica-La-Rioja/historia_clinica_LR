package net.pladema.clinichistory.requests.transcribed.infrastructure.input.rest.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEDocumentDataDto;
import java.util.List;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StudyTranscribedOrderReportInfoDto {
	
	@Nullable
	private String imageId;
	
	@Nullable
	private HCEDocumentDataDto hceDocumentDataDto;
	
	@NotNull(message = "${value.mandatory}")
	private String professionalName;
	
	@NotNull(message = "${value.mandatory}")
	private String healthCondition;
	
	@NotNull(message = "${value.mandatory}")
	@Deprecated
	private String snomed;

	@NotNull(message = "${value.mandatory}")
	private List<String> diagnosticReports;

	@NotNull(message = "${value.mandatory}")
	private Boolean status;
	
	@NotNull(message = "${value.mandatory}")
	private LocalDateTime creationDate;

	@NotNull(message = "${value.mandatory}")
	private Boolean isAvailableInPACS;

	@NotNull(message = "${value.mandatory}")
	private Boolean viewReport;

	@NotNull(message = "${value.mandatory}")
	private Short reportStatus;

	@Nullable
	private DateDto appointmentDate;
	@Nullable
	private TimeDto appointmentHour;
	@Nullable
	private String localViewerUrl;
	@Nullable
	private String DeriveTo;
}
