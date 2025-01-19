package ar.lamansys.sgh.publicapi.prescription.domain;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class PrescriptionLineV2Bo {

	private Integer prescriptionLineNumber;

	private String prescriptionLineStatus;

	private PrescriptionProblemBo prescriptionProblem;

	private GenericMedicationBo genericMedication;

	private SuggestedCommercialMedicationBo suggestedCommercialMedication;

	private List<CommercialMedicationBo> commercialMedications;

	private PrescriptionDosageBo prescriptionDosage;

	private Integer medicationStatementId;

	private String observation;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PrescriptionLineV2Bo that = (PrescriptionLineV2Bo) o;
		return Objects.equals(prescriptionLineNumber, that.prescriptionLineNumber) &&
				Objects.equals(prescriptionLineStatus, that.prescriptionLineStatus) &&
				Objects.equals(prescriptionProblem, that.prescriptionProblem) &&
				Objects.equals(genericMedication, that.genericMedication) &&
				Objects.equals(commercialMedications, that.commercialMedications) &&
				Objects.equals(prescriptionDosage, that.prescriptionDosage) &&
				Objects.equals(medicationStatementId, that.medicationStatementId) &&
				Objects.equals(observation, that.observation) &&
				Objects.equals(suggestedCommercialMedication, that.suggestedCommercialMedication);
	}

	@Override
	public int hashCode() {
		return Objects.hash(prescriptionLineNumber, prescriptionLineStatus, prescriptionProblem, genericMedication, commercialMedications, prescriptionDosage, medicationStatementId, observation, suggestedCommercialMedication);
	}

}
