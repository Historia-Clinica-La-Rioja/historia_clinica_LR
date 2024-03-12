package net.pladema.electronicjointsignature.documentlist.infrastructure.input.dto;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EElectronicSignatureStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
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

	private EDocumentType documentType;

	private String patientCompleteName;

	private String responsibleProfessionalCompleteName;

	private DateTimeDto documentCreationDate;

	private EElectronicSignatureStatus signatureStatus;

	private DateDto statusDate;

	private List<String> problems;

}
