package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEDocumentDataDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TranscribedOrderReportInfoDto {
	private String imageId;
	private HCEDocumentDataDto hceDocumentDataDto;
	private String professionalName;
	private String healthCondition;
	private String snomed;
	private Boolean status;
	private LocalDateTime creationDate;
	private Boolean seeStudy;
	private Boolean viewReport;
}
