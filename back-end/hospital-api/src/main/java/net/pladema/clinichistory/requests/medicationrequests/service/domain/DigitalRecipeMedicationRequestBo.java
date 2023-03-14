package net.pladema.clinichistory.requests.medicationrequests.service.domain;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DigitalRecipeMedicationRequestBo extends MedicationRequestBo {

	public short getDocumentType() {
		return DocumentType.DIGITAL_RECIPE;
	}

}
