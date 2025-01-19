package ar.lamansys.sgh.clinichistory.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCommercialStatementCommercialPrescriptionBo {

	private Integer medicationStatementId;

	private Short presentationUnitQuantity;

	private Short medicationPackQuantity;

}
