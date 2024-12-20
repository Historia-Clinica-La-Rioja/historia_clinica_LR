package net.pladema.clinichistory.requests.medicationrequests.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidatedMedicationRequestBo {

	private Integer medicationRequestId;

	private String externalMedicationRequestId;

}
