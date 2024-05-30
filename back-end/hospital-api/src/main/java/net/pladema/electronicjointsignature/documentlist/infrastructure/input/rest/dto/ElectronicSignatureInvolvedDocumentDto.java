package net.pladema.electronicjointsignature.documentlist.infrastructure.input.rest.dto;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EElectronicSignatureStatus;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ElectronicSignatureInvolvedDocumentDto {

	private Long documentId;

	private Short documentTypeId;

	private String patientCompleteName;

	private String responsibleProfessionalCompleteName;

	private DateTimeDto documentCreationDate;

	private EElectronicSignatureStatus signatureStatus;

	private List<String> problems;

}
