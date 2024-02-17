package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.document.dto;

import java.io.Serializable;

import ar.lamansys.sgh.shared.infrastructure.input.service.DocumentTypeDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SourceTypeDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class DocumentFileSummaryDto implements Serializable {

	private Integer sourceId;

	private SourceTypeDto sourceType;

	private DocumentTypeDto documentType;

	private DateTimeDto startDate;

	private DocumentFileDataDto document;

}
