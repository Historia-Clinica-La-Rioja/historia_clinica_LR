package ar.lamansys.sgh.publicapi.domain.prescription;

import lombok.Getter;

@Getter
public class ChangePrescriptionStateMedicationBo {
	Integer prescriptionLine;
	Short prescriptionStateId;
	DispensedMedicationBo dispensedMedicationBo;

	public ChangePrescriptionStateMedicationBo(Integer prescriptionLine, Short prescriptionStateId, DispensedMedicationBo dispensedMedicationBo) {
		this.prescriptionLine = prescriptionLine;
		this.prescriptionStateId = prescriptionStateId;
		this.dispensedMedicationBo = dispensedMedicationBo;
	}
}
