package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommercialMedicationPrescriptionBo {

	private Short presentationUnitQuantity;

	private Short medicationPackQuantity;

}
