package ar.lamansys.sgh.clinichistory.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommercialPrescriptionDataBo {

	private Integer medicationStatementId;

	private Short presentationUnitQuantity;

	private Short medicationPackQuantity;

	private String suggestedCommercialMedicationPt;

}
