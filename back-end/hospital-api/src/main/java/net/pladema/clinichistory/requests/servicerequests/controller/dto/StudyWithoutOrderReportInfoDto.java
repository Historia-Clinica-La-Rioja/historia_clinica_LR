package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEDocumentDataDto;
import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StudyWithoutOrderReportInfoDto {
	private String snomed;
	private String imageId;
	private HCEDocumentDataDto hceDocumentDataDto;
	private Boolean status;
	private Boolean seeStudy;
	private Boolean viewReport;
}
