package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEDocumentDataDto;
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
	
	@NotNull(message = "value.mandatory")
	private String professionalName;
	
	@NotNull(message = "value.mandatory")
	private String healthCondition;
	
	@NotNull(message = "value.mandatory")
	private String snomed;

	@NotNull(message = "value.mandatory")
	private Boolean status;
	
	@NotNull(message = "value.mandatory")
	private LocalDateTime creationDate;

	@NotNull(message = "value.mandatory")
	private Boolean isAvailableInPACS;

	@NotNull(message = "value.mandatory")
	private Boolean viewReport;
}