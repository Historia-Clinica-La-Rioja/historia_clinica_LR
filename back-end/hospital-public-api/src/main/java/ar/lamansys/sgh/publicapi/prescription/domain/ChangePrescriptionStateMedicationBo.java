package ar.lamansys.sgh.publicapi.prescription.domain;

import lombok.Getter;

@Getter
public class ChangePrescriptionStateMedicationBo {
	private final Integer prescriptionLine;
	private final Short prescriptionStateId;
	private final DispensedMedicationBo dispensedMedicationBo;
	private final String observations;

	private final String pharmacyName;

	private final String pharmacistName;

	public ChangePrescriptionStateMedicationBo(Integer prescriptionLine,
											   Short prescriptionStateId, DispensedMedicationBo dispensedMedicationBo,
											   String observations,
											   String pharmacyName,
											   String pharmacistName) {
		this.prescriptionLine = prescriptionLine;
		this.prescriptionStateId = prescriptionStateId;
		this.dispensedMedicationBo = dispensedMedicationBo;
		this.observations = observations;
		this.pharmacyName = pharmacyName;
		this.pharmacistName = pharmacistName;
	}
}
