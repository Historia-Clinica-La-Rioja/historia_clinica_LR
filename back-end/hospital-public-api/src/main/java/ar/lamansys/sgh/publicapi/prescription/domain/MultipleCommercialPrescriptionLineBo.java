package ar.lamansys.sgh.publicapi.prescription.domain;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class MultipleCommercialPrescriptionLineBo {

	private Integer prescriptionLineNumber;

	private String prescriptionLineStatus;

	private PrescriptionProblemBo prescriptionProblem;

	private GenericMedicationBo genericMedication;

	private List<CommercialMedicationBo> commercialMedications;

	private Double unitDosis;

	private Double dayDosis;

	private Double duration;

	private String presentation;

	private Integer presentationQuantity;

	private Double quantity;

	private Integer medicationStatementId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MultipleCommercialPrescriptionLineBo that = (MultipleCommercialPrescriptionLineBo) o;
		return Objects.equals(prescriptionLineNumber, that.prescriptionLineNumber) && Objects.equals(prescriptionLineStatus, that.prescriptionLineStatus) && Objects.equals(prescriptionProblem, that.prescriptionProblem) && Objects.equals(genericMedication, that.genericMedication) && Objects.equals(commercialMedications, that.commercialMedications) && Objects.equals(unitDosis, that.unitDosis) && Objects.equals(dayDosis, that.dayDosis) && Objects.equals(duration, that.duration) && Objects.equals(presentation, that.presentation) && Objects.equals(presentationQuantity, that.presentationQuantity);
	}

}
