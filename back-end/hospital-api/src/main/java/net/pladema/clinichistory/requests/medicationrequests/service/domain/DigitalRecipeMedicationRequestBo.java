package net.pladema.clinichistory.requests.medicationrequests.service.domain;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class DigitalRecipeMedicationRequestBo extends MedicationRequestBo {

	public short getDocumentType() {
		return DocumentType.DIGITAL_RECIPE;
	}

	public DigitalRecipeMedicationRequestBo(MedicationRequestBo medicationRequestBo, Integer key, LocalDate value) {
		super(medicationRequestBo, key, value);
	}

}
