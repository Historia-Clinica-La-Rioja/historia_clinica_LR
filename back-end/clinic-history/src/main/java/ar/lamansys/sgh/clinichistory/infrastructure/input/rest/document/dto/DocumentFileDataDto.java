package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.document.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DocumentFileDataDto {

    private Long id;

    private String filename;

	private SignatureStatusTypeDto signatureStatus;

}
